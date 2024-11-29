package org.esfe.controladores;

import ch.qos.logback.core.joran.conditional.IfAction;
import org.esfe.modelos.Informe;

import org.esfe.modelos.Miembro;
import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Tarea;
import org.esfe.servicios.implementaciones.InformeService;
import org.esfe.servicios.implementaciones.TareaService;
import org.esfe.servicios.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/informes")
public class InformeController {
    @Autowired
    private IInformeServices informeServices;
    @Autowired
    private ITareaService tareaService;
    @Autowired
    private IInformeServices tareaServices;
    @Autowired
    private IEstadoServices estadoServices;
    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private IMiembroService miembroService;
    @Autowired
    private IUsuarioService usuarioService;
    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("sort") Optional<String> sort) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.unsorted());
        Page<Informe> informes = informeServices.buscartodoslospaginados(pageable);
        model.addAttribute("informes", informes);

        int totalPages = informes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", currentPage);

        }

        // Carga de todos los informes para el carrusel
        List<Informe> allInformes = informeServices.ObtenerTodos();
        model.addAttribute("allInformes", allInformes);



        return "informe/index";
    }





    @GetMapping("/create")
    public String create(Informe  informe, Model model )
    {
        model.addAttribute("estados", estadoServices.ObtenerTodos());
        model.addAttribute("proyectos", proyectoService.ObtenerTodos());
        model.addAttribute("miembros", miembroService.obtenerTodos());
        model.addAttribute("usuarios", usuarioService.ObtenerTodos());
        return "informe/create";
    }

    @PostMapping("/save")
    public String save(Informe  informe, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute("estados", estadoServices.ObtenerTodos());
            model.addAttribute("proyectos", proyectoService.ObtenerTodos());
            model.addAttribute("miembros", miembroService.obtenerTodos());
            model.addAttribute("usuarios", usuarioService.ObtenerTodos());
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "informe/create";}

        informeServices.crearOEditar(informe);
        attributes.addFlashAttribute("msg", "Informe creado correctamente");
        return "redirect:/informes";
    }


    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Informe informe = informeServices.buscarPorId(id).get();
        model.addAttribute("informe", informe);
        return "informe/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        model.addAttribute("estados", estadoServices.ObtenerTodos());
        model.addAttribute("proyectos", proyectoService.ObtenerTodos());
        model.addAttribute("miembros", miembroService.obtenerTodos());
        model.addAttribute("usuarios", usuarioService.ObtenerTodos());
        Informe informe = informeServices.buscarPorId(id).get();
        model.addAttribute("informe", informe);
        return "informe/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Informe informe = informeServices.buscarPorId(id).orElse(null);
        model.addAttribute("informe", informe);
        return  "informe/delete";
    }

    @PostMapping("/delete")
    public String delete(Informe informe, RedirectAttributes attributes){
        informeServices.eliminarPorid(informe.getInforme_id());
        attributes.addFlashAttribute("msg","Informe liminado");
        return  "redirect:/informes";
    }

    @GetMapping("/tareas/index")
    public String mostrarTareas(@RequestParam("id") int id, Model model) {
        model.addAttribute("id", id);
        return "tareas/index";
    }
}
