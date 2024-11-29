package org.esfe.controladores;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Usuario;
import org.esfe.repositorio.IProyectoRepository;
import org.esfe.servicios.implementaciones.ProyectoService;
import org.esfe.servicios.interfaces.IProyectoService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/Proyectos")
public class ProyectoController {
    private static String UPLOADED_FOLDER = "C:/imagenes_proyectos/"; // Ruta en el servidor donde se guardarán las imágenes

    public ProyectoController(IProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IProyectoRepository proyectoRepository;


    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("sort") Optional<String> sort,
                        @RequestParam(value = "estado", required = false) String estado,
                        @AuthenticationPrincipal UserDetails userDetails) {

        // Definir valores predeterminados si los parámetros no están presentes
        int currentPage = page.orElse(1) - 1;  // Página actual (inicia en 0)
        int pageSize = size.orElse(5);  // Tamaño de la página
        String sortField = sort.orElse("nombre");  // Ordenar por nombre por defecto

        // Crear el Pageable para paginación y ordenamiento
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(sortField).ascending());

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener todos los proyectos del usuario sin filtro de estado o sort
        List<Proyecto> proyectosFiltrados = proyectoService.getProyectosByUsuario(usuario);

        // Si el estado no es null ni vacío, filtrar los proyectos por estado
        if (estado != null && !estado.isEmpty()) {
            proyectosFiltrados = proyectosFiltrados.stream()
                    .filter(proyecto -> estado.equals(proyecto.getEstado()))
               .collect(Collectors.toList());
        }

        // Ordenar los proyectos según el campo seleccionado (por nombre, fechaFin o presupuesto)
        switch (sortField) {
            case "fechaFin":
                proyectosFiltrados.sort(Comparator.comparing(Proyecto::getFechaFin));
                break;
            case "presupuesto":
                proyectosFiltrados.sort(Comparator.comparing(Proyecto::getPresupuesto));
                break;
            case "nombre":
            default:
                proyectosFiltrados.sort(Comparator.comparing(Proyecto::getNombre));
                break;
        }

        // Paginar los proyectos filtrados y ordenados
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), proyectosFiltrados.size());
        Page<Proyecto> paginatedProyectos = new PageImpl<>(proyectosFiltrados.subList(start, end), pageable, proyectosFiltrados.size());

        // Verificar si hay proyectos para mostrar
        if (paginatedProyectos != null && paginatedProyectos.hasContent()) {
            model.addAttribute("proyectos", paginatedProyectos);
            int totalPages = paginatedProyectos.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("currentPage", currentPage);
            }
        } else {
            model.addAttribute("error", "No se encontraron proyectos con los filtros seleccionados.");
        }

        // Añadir al modelo los parámetros de estado y ordenamiento para mantenerlos en la vista
        model.addAttribute("estado", estado);
        model.addAttribute("sort", sortField);

        // Retornar la vista con los proyectos
        return "Proyecto/index";  // El nombre de la vista a devolver
    }



    @GetMapping("/create")
    public String create(Proyecto proyecto, Model model) {
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
        model.addAttribute("currentView", "crearProyecto");
        return "Proyecto/create"; // Asegúrate de que esta ruta coincida con la vista Thymeleaf
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Proyecto proyecto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @AuthenticationPrincipal UserDetails userDetails) {

        // Determinar si es una edición o una creación
        boolean esEdicion = proyecto.getProyectoId() != null;

        if (!esEdicion) {
            // Validaciones específicas para creación
            if (proyecto.getFechaInicio() != null && proyecto.getFechaInicio().before(new Date())) {
                result.rejectValue("fechaInicio", "error.proyecto", "La fecha de inicio no puede ser una fecha pasada");
            }
            if (proyecto.getFechaFin() != null && proyecto.getFechaFin().before(new Date())) {
                result.rejectValue("fechaFin", "error.proyecto", "La fecha final no puede ser una fecha pasada");
            }
        } else {
            // Validaciones específicas para edición
            Optional<Proyecto> proyectoExistente = proyectoService.buscarPorId(proyecto.getProyectoId());
            if (proyectoExistente.isPresent()) {
                Date fechaInicioExistente = proyectoExistente.get().getFechaInicio();
                if (proyecto.getFechaFin() != null && proyecto.getFechaFin().before(fechaInicioExistente)) {
                    result.rejectValue("fechaFin", "error.proyecto",
                            "La fecha final no puede ser anterior");
                }
            }
        }

        // Si hay errores de validación, permanecer en la misma vista
        if (result.hasErrors()) {
            model.addAttribute("proyecto", proyecto);
            model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
            model.addAttribute("esEdicion", esEdicion); // Indica si es edición o creación
            if (result.hasErrors()) {
                model.addAttribute("proyecto", proyecto);
                model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
                return esEdicion ? "Proyecto/edit" : "Proyecto/create"; // Retorna la vista sin redirigir
            }
        }

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        proyecto.setUsuario(usuario);

        // Manejar la imagen solo si se subió un archivo
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();

            if (contentType != null && contentType.startsWith("image/")) {
                try {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path path = Paths.get("src/main/resources/static/images/" + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    proyecto.setImagen("/images/" + fileName);
                } catch (IOException e) {
                    model.addAttribute("errorImagen", "No se pudo guardar la imagen.");
                    model.addAttribute("proyecto", proyecto);
                    model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
                    model.addAttribute("esEdicion", esEdicion);
                    return "Proyecto/edit";
                }
            } else {
                model.addAttribute("errorImagen", "Solo se permiten archivos de imagen.");
                model.addAttribute("proyecto", proyecto);
                model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
                model.addAttribute("esEdicion", esEdicion);
                return "Proyecto/edit";
            }
        } else if (esEdicion) {
            // Si no se sube una nueva imagen, mantener la existente
            Optional<Proyecto> proyectoExistente = proyectoService.buscarPorId(proyecto.getProyectoId());
            proyectoExistente.ifPresent(p -> proyecto.setImagen(p.getImagen()));
        }

        // Guardar el proyecto (creación o actualización)
        proyectoService.crearOEditar(proyecto);

        // Mostrar mensaje de éxito
        attributes.addFlashAttribute("msg", esEdicion ?
                "Proyecto actualizado correctamente" : "Proyecto creado correctamente");

        // Redirigir a la lista de proyectos si todo es correcto
        return "redirect:/Proyectos";
    }



    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        model.addAttribute("proyecto", proyecto);
        return "Proyecto/details";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
        return "Proyecto/edit";
    }

    // Método para obtener las prioridades ordenadas
    private List<String> obtenerPrioridadesOrdenadas() {
        return Arrays.asList("Alta", "Intermedia", "Baja");
    }

    @GetMapping("remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        model.addAttribute("proyecto", proyecto);
        return "Proyecto/delete";
    }

    @PostMapping("/delete")
    public String delete(Proyecto proyecto, RedirectAttributes attributes) {
        proyectoService.eliminarPorid(proyecto.getProyectoId());
        attributes.addFlashAttribute("msg", "Proyecto ha sido eliminada correctamente");
        return "redirect:/Proyectos";
    }



    @GetMapping("/completados")
    public String mostrarProyectosCompletados(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener proyectos completados del usuario
        List<Proyecto> proyectosCompletados = proyectoService.getProyectosCompletadosPorUsuario(usuario);

        // Agregar proyectos completados al modelo
        model.addAttribute("proyectosCompletados", proyectosCompletados);

        return "Proyecto/completados"; // Asegúrate de que esta vista existe
    }


    @GetMapping("/order")
    public String mostrarProyectosCompletadosOrdenados(
            @RequestParam(value = "filterBy", defaultValue = "nombre") String filterBy,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener los proyectos completados ordenados según el criterio
        List<Proyecto> proyectosCompletados;

        switch (filterBy) {
            case "fechaFin":
                proyectosCompletados = proyectoService.getProyectosCompletadosPorUsuarioOrderByFechaFin(usuario);
                break;
            case "presupuesto":
                proyectosCompletados = proyectoService.getProyectosCompletadosPorUsuarioOrderByPresupuesto(usuario);
                break;
            case "nombre":
            default:
                proyectosCompletados = proyectoService.getProyectosCompletadosPorUsuarioOrderByNombre(usuario);
                break;
        }

        // Agregar los proyectos ordenados al modelo
        model.addAttribute("proyectosCompletados", proyectosCompletados);
        model.addAttribute("filterBy", filterBy);  // Para recordar el filtro actual en la vista
        return "Proyecto/completados";  // Asegúrate de que esta vista existe
    }

    @GetMapping("/order/cancelados")
    public String mostrarProyectosCancelados(@RequestParam(value = "filterBy", defaultValue = "fechaInicio") String filterBy, Model model) {
        List<Proyecto> proyectosCancelados;

        // Filtrar solo los proyectos cancelados
        proyectosCancelados = proyectoRepository.findByEstado("Cancelado");

        // Ordenar los proyectos según el filtro recibido
        switch (filterBy) {
            case "nombre":
                proyectosCancelados.sort(Comparator.comparing(Proyecto::getNombre));
                break;
            case "presupuesto":
                proyectosCancelados.sort(Comparator.comparing(Proyecto::getPresupuesto).reversed());
                break;
            case "fechaInicio":
            default:
                proyectosCancelados.sort(Comparator.comparing(Proyecto::getFechaInicio).reversed());
                break;
        }

        model.addAttribute("proyectosCancelados", proyectosCancelados);
        model.addAttribute("filterBy", filterBy);  // Para mantener el valor seleccionado
        return "Proyecto/cancelados";  // La vista que muestra los proyectos cancelados
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        Usuario usuario = usuarioService.findByCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "dashboard";
    }

    @ModelAttribute("usuario")
    public Usuario getUsuario(Principal principal) {
        return usuarioService.findByCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping("/updateEstado/{id}")
    public String actualizarEstado(@PathVariable("id") Integer id,
                                   @RequestParam("nuevoEstado") String nuevoEstado,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Buscar el proyecto por ID
            Proyecto proyecto = proyectoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

            // Validar el nuevo estado
            List<String> estadosValidos = Arrays.asList("Activo", "Completado", "Cancelado");
            if (!estadosValidos.contains(nuevoEstado)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Estado seleccionado no es válido.");
                return "redirect:/Proyectos";
            }

            // Actualizar y guardar el estado del proyecto
            proyecto.setEstado(nuevoEstado);
            proyectoService.guardar(proyecto);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("successMessage", "El estado del proyecto se actualizó correctamente.");
        } catch (Exception e) {
            // Capturar errores
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el estado: " + e.getMessage());
        }

        return "redirect:/Proyectos";
    }




}