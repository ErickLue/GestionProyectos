package org.esfe.controladores;

import ch.qos.logback.core.joran.conditional.IfAction;
import org.esfe.modelos.Informe;

import org.esfe.modelos.Miembro;
import org.esfe.modelos.Proyecto;
import org.esfe.servicios.implementaciones.InformeService;
import org.esfe.servicios.interfaces.IEstadoServices;
import org.esfe.servicios.interfaces.IInformeServices;
import org.esfe.servicios.interfaces.IProyectoService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/informes")
public class InformeController {
    @Autowired
    private IInformeServices informeServices;
    @Autowired
    private IEstadoServices estadoServices;
    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private IUsuarioService usuarioService;



    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Informe> informes = informeServices.buscartodoslospaginados(pageable);
        model.addAttribute("informes", informes);

        int totalPages = informes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "informe/index";
    }
    @GetMapping("/create")
    public String create(Informe  informe, Model model )
    {
        model.addAttribute("estados", estadoServices.ObtenerTodos());
        model.addAttribute("proyectos", proyectoService.ObtenerTodos());
        model.addAttribute("usuarios", usuarioService.ObtenerTodos());
        return "informe/create";
    }

    @PostMapping("/save")
    public String save(Informe  informe, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute("estados", estadoServices.ObtenerTodos());
            model.addAttribute("proyectos", proyectoService.ObtenerTodos());
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


}
