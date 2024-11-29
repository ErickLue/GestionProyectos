package org.esfe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {
    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
    //Consulta para obtener el usuario, contraseña y nombre
        users.setUsersByUsernameQuery("select correo, contraseña, status from Usuario where correo = ?");
        // Consulta para obtener los roles del usuario
        users.setAuthoritiesByUsernameQuery(
                "select u.correo, r.nombre " +  //Selecciona el nombre del rol y del estado
                        "from usuario_rol ur " +
                        "inner join Usuario u on u.usuario_id = ur.usuario_id " +  // Une Usuario con usuario_rol
                        "inner join rol r on r.rol_id = ur.rol_id " +  // Une usuario_rol con Rol
                        "where u.correo = ?"
        );
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http.authorizeHttpRequests(authorize -> authorize
                // aperturar el acceso a los recursos estáticos
                .requestMatchers("/assets/**", "/css/**", "/js/**").permitAll()
              // las vistas públicas no requieren autenticación
                .requestMatchers( "/login","/usuarios/create", "/usuarios/save").permitAll()
               // todas las demás vistas requieren autenticación
                .anyRequest().authenticated());
        http.formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/"));

        return http.build();
    }
}
