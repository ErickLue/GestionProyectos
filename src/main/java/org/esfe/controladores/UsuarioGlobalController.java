package org.esfe.controladores;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UsuarioGlobalController {

    private final IUsuarioService usuarioService;

    // Constructor que coincide con el nombre de la clase
    public UsuarioGlobalController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("usuario")
    public Usuario addUsuarioToModel() {
        // Obtener la autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            // Extraer detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            // Buscar al usuario por correo
            return usuarioService.findByCorreo(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        return null; // Si no hay un usuario autenticado, devuelve null
    }
}
