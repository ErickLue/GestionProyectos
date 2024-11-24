package org.esfe.controladores;

import org.esfe.modelos.Miembro;
import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Tarea;
import org.esfe.servicios.interfaces.IEstadoServices;
import org.esfe.servicios.interfaces.ITareaService;
import org.esfe.servicios.interfaces.IProyectoService;
import org.esfe.servicios.interfaces.IMiembroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    private static final Logger logger = LoggerFactory.getLogger(TareaController.class);

    @Autowired
    private ITareaService tareaService;

    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private IEstadoServices estadoServices;

    @Autowired
    private IMiembroService miembroService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("sort") Optional<String> sort) {
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.unsorted()); // Usamos Sort.unsorted() para manejar la ordenación manualmente

        Page<Tarea> tareas = tareaService.buscarTodosLospaginado(pageable);
        List<Tarea> tareasOrdenadas = tareas.getContent().stream()
                .sorted((t1, t2) -> {
                    List<String> ordenPrioridades = obtenerPrioridadesOrdenadas();
                    return Integer.compare(
                            ordenPrioridades.indexOf(t1.getPrioridad()),
                            ordenPrioridades.indexOf(t2.getPrioridad())
                    );
                })
                .collect(Collectors.toList());


        // Llamar al método calcularDiasLaborables del servicio TareaService
        tareasOrdenadas.forEach(tarea -> tarea.setDiasLaborables(tareaService.calcularDiasLaborables(tarea)));


        model.addAttribute("tareas", new PageImpl<>(tareasOrdenadas, pageable, tareas.getTotalElements()));

        int totalPages = tareas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage",currentPage);
        }

        Map<String, Integer> porcentajePorEstado = tareaService.calcularPorcentajes();
        model.addAttribute("porcentajePorEstado", porcentajePorEstado);

        return "tarea/index";
    }


    @GetMapping("/create")
    public String create(Tarea tarea, Model model) {
        model.addAttribute("proyectos", proyectoService.ObtenerTodos());
        model.addAttribute("miembros", miembroService.obtenerTodos());
        model.addAttribute("estados", estadoServices.ObtenerTodos());
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
        model.addAttribute("estadosTareas", EstadoTarea());
        return "tarea/create";
    }

    @PostMapping("/save")
    public String save(Tarea tarea, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("proyectos", proyectoService.ObtenerTodos());
            model.addAttribute("miembros", miembroService.obtenerTodos());
            model.addAttribute("estados", estadoServices.ObtenerTodos());
            model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
            model.addAttribute("estadosTareas", EstadoTarea());
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "tarea/create";
        }

        tareaService.crearOEditar(tarea);
        attributes.addFlashAttribute("msg", "Tarea creada correctamente.");
        return "redirect:/tareas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id).orElse(null);
        model.addAttribute("tarea", tarea);
        model.addAttribute("proyectos", proyectoService.ObtenerTodos());
        model.addAttribute("miembros", miembroService.obtenerTodos());
        model.addAttribute("estados", estadoServices.ObtenerTodos());
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
        model.addAttribute("estadosTareas", EstadoTarea());
        return "tarea/edit";
    }

    // Método para obtener las prioridades ordenadas
    private List<String> obtenerPrioridadesOrdenadas() {
        return Arrays.asList("Alta","Intermedia", "Baja");
    }

    //Método para obtener las estado tarea
    private List<String> EstadoTarea(){
        return Arrays.asList("Pendiente","En Progreso","Completada","Cancelada");
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id).orElse(null);
        model.addAttribute("tarea", tarea);
        return "tarea/details";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id).orElse(null);
        model.addAttribute("tarea", tarea);
        return "tarea/delete";
    }

    @PostMapping("/delete")
    public String delete(Tarea tarea, RedirectAttributes attributes) {
        tareaService.eliminarPorid(tarea.getTarea_id());
        attributes.addFlashAttribute("msg", "Tarea ha sido eliminada correctamente");
        return "redirect:/tareas";
    }

    // Nuevo método para mostrar el porcentaje de tareas por estado
    @GetMapping("/porcentaje")
    public String mostrarPorcentajeDeTareas(Model model) {
        try {
            Map<String, Integer> porcentajePorEstado = tareaService.calcularPorcentajes();
            model.addAttribute("porcentajePorEstado", porcentajePorEstado);
            return "tarea/porcentaje";
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Error calculating percentages: {}", e.getMessage());
            // You can also add a flash attribute to display an error message to the user
            model.addAttribute("errorMessage", "Error calculating percentages. Please try again later.");
            return "error"; // or return a specific error page
        }
    }

    @GetMapping("/graficas")
    public String graficas(Model model) {
        try {
            // Llama al método que calcula los porcentajes por estado
            Map<String, Integer> porcentajePorEstado = tareaService.calcularPorcentajes();
            model.addAttribute("porcentajePorEstado", porcentajePorEstado);

            return "tarea/graficas"; // Retorna la nueva vista `graficas`
        } catch (Exception e) {
            logger.error("Error al calcular los porcentajes: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al generar las gráficas. Intente de nuevo más tarde.");
            return "error"; // Redirige a una página de error en caso de fallo
        }
    }

    @GetMapping("/filtrar")
    public String filtrarPorEstado(@RequestParam("estado") String estado, Model model) {
        // Crear un mapa para normalizar los estados
        Map<String, String> estadoMap = Map.of(
                "EnProgreso", "En Progreso",
                "Pendiente", "Pendiente",
                "Completada", "Completada",
                "Cancelada", "Cancelada"
        );

        // Obtener el estado normalizado
        estado = estadoMap.getOrDefault(estado, estado); // Si no está en el mapa, usa el estado original

        // Buscar las tareas
        List<Tarea> tareasFiltradas = tareaService.buscarPorEstadoTarea(estado);

        model.addAttribute("tareas", tareasFiltradas);
        model.addAttribute("estadoSeleccionado", estado);
        return "tarea/index";
    }



}
