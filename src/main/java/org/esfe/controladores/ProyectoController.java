package org.esfe.controladores;
import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Tarea;
import org.esfe.servicios.interfaces.IProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/Proyectos")
public class ProyectoController {

    @Autowired
    private IProyectoService proyectoService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("sort") Optional<String> sort) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        String sortField = sort.orElse("prioridad"); // Campo de ordenación predeterminado

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.unsorted()); // Usamos Sort.unsorted() para manejar la ordenación manualmente

        // Obtener los proyectos paginados
        Page<Proyecto> proyectos = proyectoService.buscarTodosLospaginado(pageable);
        if (proyectos != null && proyectos.hasContent()) {
            model.addAttribute("proyectos", proyectos);
            int totalPages = proyectos.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("currentPage",currentPage);
            }
        } else {
            model.addAttribute("error", "No se encontraron proyectos.");
        }

        return "Proyecto/index"; // Asegúrate de que la vista se llame correctamente
    }

    @GetMapping("/create")
    public String create(Proyecto proyecto, Model model) {
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
        return "Proyecto/create"; // Asegúrate de que esta ruta coincida con la vista Thymeleaf
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Proyecto proyecto, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("proyecto", proyecto); // Asegúrate de que el objeto 'proyecto' esté en el modelo
            model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
            attributes.addFlashAttribute("error", "No se pudo crear debido a un error inesperado");
            return "Proyecto/create"; // Redirige de nuevo a la vista de creación si hay errores
        }

        // Aquí es donde guardarías o actualizarías el proyecto
        proyectoService.crearOEditar(proyecto);

        // Mensaje de éxito
        attributes.addFlashAttribute("msg", "Proyecto creado correctamente");

        // Redirige a la lista de proyectos o a la vista deseada
        return "redirect:/Proyectos"; // Asegúrate de que esta ruta esté manejada en el controlador
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id).get();
        model.addAttribute("proyecto", proyecto);
        return "Proyecto/details";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id).get();
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("prioridades", obtenerPrioridadesOrdenadas());
        return "Proyecto/edit";
    }

    // Método para obtener las prioridades ordenadas
    private List<String> obtenerPrioridadesOrdenadas() {
        return Arrays.asList("Alta","Intermedia", "Baja");
    }


    @GetMapping("remove/{id}")
    public String remove (@PathVariable("id")Integer id, Model model){
        Proyecto proyecto = proyectoService.buscarPorId(id).get();
        model.addAttribute("proyecto", proyecto);
        return  "Proyecto/delete";
    }

    @PostMapping("/delete")
    public String delete(Proyecto proyecto, RedirectAttributes attributes){
        proyectoService.eliminarPorid(proyecto.getProyecto_id());
        attributes.addFlashAttribute("msg", "Proyecto ha sido eliminada correctamente");
        return "redirect:/Proyectos";
    }
}
