package cl.javadevs.springsecurityjwt.mappers;

import cl.javadevs.springsecurityjwt.dtos.auth.DtoRegistro;
import cl.javadevs.springsecurityjwt.models.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(DtoRegistro dtoRegistro){
        Usuario usuario = new Usuario();
        usuario.setUsername(dtoRegistro.getUsername());
        usuario.setPassword(dtoRegistro.getPassword());
        usuario.setNombre(dtoRegistro.getNombre());
        usuario.setApellido(dtoRegistro.getApellido());
        usuario.setEmail(dtoRegistro.getEmail());
        return usuario;
    }
}
