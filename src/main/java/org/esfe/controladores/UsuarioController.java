package org.esfe.controladores;

import org.esfe.modelos.Estado;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IEstadoServices;
import org.esfe.servicios.interfaces.IRolService;
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
            return "Usuario/login";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        // Crear un nuevo objeto Usuario
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);

        // Cargar los roles y el estado por defecto
        model.addAttribute("roles", rolService.ObtenerTodos()); // Suponiendo que tienes un método para obtener roles
        model.addAttribute("estado", estadoServices.buscarPorId(1)); // Estado con ID 1

        return "Usuario/create"; // Asegúrate de que esta ruta coincida con la vista Thymeleaf
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Si hay errores en el formulario, vuelve a mostrar el formulario
            model.addAttribute("roles", rolService.ObtenerTodos()); // Recargar roles
            model.addAttribute("estado", estadoServices.buscarPorId(2)); // Estado con ID 1
            model.addAttribute("error", "Usuario no creado por un error inesperado");
            return "Usuario/create"; // Vista de creación del usuario
        }

        // Asignar el estado automáticamente
      //  Optional<Estado> estado = estadoServices.buscarPorId(1); // Obtener el estado con ID 1
        //usuario.setEstado(estado.orElse(null)); // Asignar el estado al usuario

        // Asignar el estado automáticamente
        Optional<Estado> estado = estadoServices.buscarPorId(1); // Obtener el estado con ID 1


        // Crear o editar el usuario
        usuarioService.crearOEditar(usuario);

        // Agregar un mensaje de éxito y redirigir a la vista
        attributes.addFlashAttribute("msg", "Usuario creado correctamente");
        return "redirect:/Proyectos"; // Redirigir a la página de proyectos
    }


}

