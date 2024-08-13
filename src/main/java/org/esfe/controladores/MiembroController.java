package org.esfe.controladores;
import org.esfe.modelos.Miembro;
import org.esfe.servicios.interfaces.IMiembroService;
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
@RequestMapping("/miembros")
public class MiembroController {

    @Autowired
    private IMiembroService miembroService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; //si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //tamaño de la página, se asigna 5
        Pageable pageable = (Pageable) PageRequest.of(currentPage, pageSize);

        Page<Miembro> miembros = miembroService.buscarTodosPaginados(pageable);
        model.addAttribute("miembros", miembros);

        int totalPages = miembros.getTotalPages();
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "miembro/index";
    }

    @GetMapping("/create")
    public String create(Miembro miembro){
        return "miembro/create";
    }

    @PostMapping("/save")
    public String save(Miembro miembro, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(miembro);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "miembro/create";
        }

        miembroService.createOEditar(miembro);
        attributes.addFlashAttribute("msg", "Miembro creado correctamente");
        return "redirect:/miembros";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Miembro miembro = miembroService.buscarPorId(id).get();
        model.addAttribute("miembro", miembro);
        return "miembro/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Miembro miembro = miembroService.buscarPorId(id).get();
        model.addAttribute("miembro", miembro);
        return "miembro/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Miembro miembro = miembroService.buscarPorId(id).get();
        model.addAttribute("miembro", miembro);
        return "miembro/delete";
    }

    @PostMapping("/delete")
    public String delete(Miembro miembro, RedirectAttributes attributes){
        miembroService.eliminarPorId(miembro.getMiembroEquipo_id());
        attributes.addFlashAttribute("msg", "Miembro eliminado correctamente");
        return "redirect:/miembros";
    }
}
