package org.esfe.controladores;
import org.esfe.modelos.Proyecto;
import org.esfe.servicios.interfaces.IProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;//Si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //Tamaño de la pagina se asigna 5
        Pageable pageable = (Pageable) PageRequest.of(currentPage, pageSize);

        Page<Proyecto> proyectos = proyectoService.buscarTodosLospaginado(pageable);
        model.addAttribute("proyectos", proyectos);
        int totalPages = proyectos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "Proyecto/index";
    }

    @GetMapping("/create")
    public String create(Proyecto proyecto) {
        return ("Proyecto/create");
    }

    @PostMapping("/save")
    public String save(Proyecto proyecto, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute(proyecto);
            attributes.addFlashAttribute("error", "No se pudo crear debido a un error inesperado");
            return "proyecto/create";
        }
        proyectoService.crearOEditar(proyecto);
        attributes.addFlashAttribute("msg", "Proyecto creado correctamente");
        return "redirect:/Proyecto";
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
        return "Proyecto/edit";
    }


    @GetMapping("remove/{id}")
    public String remove (@PathVariable("id")Integer id, Model model){
        Proyecto proyecto = proyectoService.buscarPorId(id).get();
        model.addAttribute("proyecto", proyecto);
        return  "Proyecto/delete";
    }

    @GetMapping("delete")
    public String delete (@PathVariable("id")Integer id, Model model){
        Proyecto proyecto = proyectoService.buscarPorId(id).get();
        model.addAttribute("proyecto", proyecto);
        return  "redirect:/Proyecto";
    }
}
