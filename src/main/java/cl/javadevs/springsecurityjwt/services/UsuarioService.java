package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.cloudinaryImages.service.CloudinaryService;
import cl.javadevs.springsecurityjwt.dtos.usuario.request.DtoUsuario;
import cl.javadevs.springsecurityjwt.dtos.usuario.response.DtoUsuarioResponse;
import cl.javadevs.springsecurityjwt.exceptions.ServicioNoEncontradoException;
import cl.javadevs.springsecurityjwt.exceptions.UsuarioExistenteException;
import cl.javadevs.springsecurityjwt.models.*;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final IUsuariosRepository usuariosRepository;
    private final CloudinaryService cloudinaryService;

    public DtoUsuarioResponse readOne(Long id){
        Usuario usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        DtoUsuarioResponse dto = new DtoUsuarioResponse();
        dto.setUsuario_id(usuario.getUsuario_id());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setCelular(usuario.getCelular());
        dto.setUrlUsuario(usuario.getUrlUsuario());
        return dto;
    }

    public List<DtoUsuarioResponse> readAll() {
        List<Usuario> usuarios = usuariosRepository.findAll();
        return usuarios.stream().map(usuario -> {
            DtoUsuarioResponse dto = new DtoUsuarioResponse();
            dto.setUsuario_id(usuario.getUsuario_id());
            dto.setUsername(usuario.getUsername());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setEmail(usuario.getEmail());
            dto.setCelular(usuario.getCelular());
            dto.setUrlUsuario(usuario.getUrlUsuario());
            return dto;
        }).toList();
    }

    public void update(Long id, DtoUsuario dtoUsuario, MultipartFile imagen) {
        String urlImagen = null;

        Usuario usuario = usuariosRepository.findById(id)
                .orElseThrow(()-> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));

        if (imagen != null){
            urlImagen = cloudinaryService.subirImagen(imagen,"usuarios");
            usuario.setUrlUsuario(urlImagen);
        }
        usuario.setNombre(dtoUsuario.getNombre());
        usuario.setApellido(dtoUsuario.getApellido());
        usuario.setEmail(dtoUsuario.getEmail());
        usuario.setCelular(dtoUsuario.getCelular());
        usuariosRepository.save(usuario);
    }

}
