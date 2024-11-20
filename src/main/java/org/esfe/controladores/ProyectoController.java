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
                        @AuthenticationPrincipal UserDetails userDetails) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        String sortField = sort.orElse("prioridad"); // Campo de ordenación predeterminado

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(sortField).ascending());

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener y filtrar los proyectos activos del usuario autenticado
        List<Proyecto> proyectosActivos = proyectoService.getProyectosByUsuario(usuario)
                .stream()
                .filter(proyecto -> "Activo".equals(proyecto.getEstado()))
                .collect(Collectors.toList());

        // Paginar los proyectos activos del usuario
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), proyectosActivos.size());
        Page<Proyecto> paginatedProyectos = new PageImpl<>(proyectosActivos.subList(start, end), pageable, proyectosActivos.size());

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
            model.addAttribute("error", "No se encontraron proyectos activos.");
        }

        return "Proyecto/index"; // Asegúrate de que la vista se llame correctamente
    }

    @GetMapping("/create")
    public String create(Proyecto proyecto, Model model) {
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
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
        boolean esEdicion = proyecto.getProyecto_id() != null;

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
            Optional<Proyecto> proyectoExistente = proyectoService.buscarPorId(proyecto.getProyecto_id());
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
            Optional<Proyecto> proyectoExistente = proyectoService.buscarPorId(proyecto.getProyecto_id());
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
        proyectoService.eliminarPorid(proyecto.getProyecto_id());
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


    @GetMapping("/activos")
    public String mostrarProyectosEnProgreso(Model model) {
        // Obtener los proyectos con estado "en progreso"
        List<Proyecto> proyectosActivos = proyectoService.obtenerProyectosActivos();

        // Agregar los proyectos en progreso al modelo
        model.addAttribute("proyectosActivos", proyectosActivos);

        // Retornar la vista para mostrar los proyectos en progreso
        return "proyecto/activos";  // Vista que deberás crear para proyectos en progreso
    }

    @GetMapping("/cancelados")
    public String mostrarProyectosCancelados(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener proyectos completados del usuario
        List<Proyecto> proyectosCancelados = proyectoService.getProyectosCanceladossPorUsuario(usuario);

        // Agregar proyectos completados al modelo
        model.addAttribute("proyectosCancelados", proyectosCancelados);

        return "Proyecto/cancelados"; // Asegúrate de que esta vista existe
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

}