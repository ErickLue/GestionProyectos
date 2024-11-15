package org.esfe.controladores;


import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IEstadoServices;
import org.esfe.servicios.interfaces.IRolService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IRolService rolService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IEstadoServices estadoServices;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String mostrarFormularioDeLogin() {
        return "Usuario/Login";
    }

    @PostMapping("/login")
    public String procesarLogin(String correo, String contraseña, Model model) {
        Usuario usuario = usuarioService.autenticar(correo, contraseña);
        System.out.println(correo);
        System.out.println(usuario);
        if (usuario != null) {
            return "redirect:/Proyectos";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "usuarios/login";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);

        // Cargar los roles y el estado por defecto
        model.addAttribute("roles", rolService.ObtenerTodos());
        model.addAttribute("estado", estadoServices.buscarPorId(1).orElse(null)); // Estado con ID 1

        return "Usuario/create"; // Asegúrate de que esta ruta coincida con la vista Thymeleaf
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.ObtenerTodos());
            model.addAttribute("estado", estadoServices.buscarPorId(1).orElse(null));
            model.addAttribute("error", "Usuario no creado por un error inesperado");
            return "Usuario/create";
        }

        try {
            usuario.setStatus(1); // Asignar estado activo
            usuarioService.crearOEditar(usuario); // Guardar usuario
            attributes.addFlashAttribute("msg", "Usuario creado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el usuario: " + e.getMessage());
            return "Usuario/create"; // Volver a mostrar el formulario en caso de error
        }

        return "redirect:/Proyectos"; // Redirigir a la vista de proyectos
    }


}

