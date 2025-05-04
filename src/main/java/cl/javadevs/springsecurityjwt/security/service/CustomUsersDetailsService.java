package cl.javadevs.springsecurityjwt.security.service;

import cl.javadevs.springsecurityjwt.models.Rol;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUsersDetailsService implements UserDetailsService {
    private IUsuariosRepository usuariosRepo;

    @Autowired
    public CustomUsersDetailsService(IUsuariosRepository usuariosRepo) {
        this.usuariosRepo = usuariosRepo;
    }


    //Método para traernos una lista de autoridades por medio de una lista de roles
    public Collection<GrantedAuthority> mapToAuthorities(List<Rol> roles) {
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
    }


    //Método para traernos un usuario con todos sus datos por medio de sus username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuariosRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new CustomUserDetails(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                        .collect(Collectors.toList()));
    }



    // Clase interna para representar los detalles del usuario
    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        private final String nombre;
        private final String apellido;

        public CustomUserDetails(String username, String password, String nombre, String apellido, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            this.nombre = nombre;
            this.apellido = apellido;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }
    }
}
