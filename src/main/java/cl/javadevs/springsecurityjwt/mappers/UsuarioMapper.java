package cl.javadevs.springsecurityjwt.mappers;

import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoRegistro;
import cl.javadevs.springsecurityjwt.dtos.usuario.response.DtoUsuarioResponse;
import cl.javadevs.springsecurityjwt.models.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(DtoRegistro dtoRegistro){
        Usuario usuario = new Usuario();
        usuario.setUsername(dtoRegistro.getUsername());
        usuario.setPassword(dtoRegistro.getPassword());
        usuario.setNombre(dtoRegistro.getNombre());
        usuario.setApellido(dtoRegistro.getApellido());
        usuario.setEmail(dtoRegistro.getEmail());
        usuario.setCelular(dtoRegistro.getCelular());
        return usuario;
    }

    public static DtoUsuarioResponse toDto(Usuario usuario){
        DtoUsuarioResponse dto = new DtoUsuarioResponse();
        dto.setUsuario_id(usuario.getUsuario_id());
        dto.setUsername(usuario.getUsername());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setCelular(usuario.getCelular());
        dto.setUrlUsuario(usuario.getUrlUsuario());
        return dto;
    }
}
