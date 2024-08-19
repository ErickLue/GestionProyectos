package org.esfe.controladores;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarFormularioDeLogin() {
        return "Usuario/login";
    }

    @PostMapping("/login")
    public String procesarLogin(String correo, String contraseña, Model model) {
        Usuario usuario = usuarioService.autenticar(correo, contraseña);
        if (usuario != null) {
            return "redirect:/Proyectos";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "Usuario/login";
        }

    }

    @GetMapping("/create")
    public String create(Usuario usuario) {
        return "Usuario/create"; // Asegúrate de que esta ruta coincida con la vista Thymeleaf
    }
    @PostMapping("/save")
    public String save (@ModelAttribute Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()) {
            model.addAttribute("error", "Usuario no creado por un error inesperado");
            return "Usuario/login";
        }
        usuarioService.crearOEditar(usuario);
        return "redirect:/Proyectos";

    }


}

