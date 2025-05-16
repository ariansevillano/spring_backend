package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.valoracion.request.DtoValoracion;
import cl.javadevs.springsecurityjwt.dtos.valoracion.response.DtoValoracionResponse;
import cl.javadevs.springsecurityjwt.exceptions.UsuarioExistenteException;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.models.Valoracion;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import cl.javadevs.springsecurityjwt.repositories.IValoracionRepository;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoracionService {

    private IUsuariosRepository usuariosRepository;
    private IValoracionRepository valoracionRepository;

    @Autowired
    public ValoracionService(IValoracionRepository valoracionRepository, IUsuariosRepository usuariosRepository) {
        this.valoracionRepository = valoracionRepository;
        this.usuariosRepository = usuariosRepository;
    }

    public void crear(DtoValoracion dtoValoracion, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                        .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        Valoracion valoracion = new Valoracion();
        valoracion.setValoracion(dtoValoracion.getValoracion());
        valoracion.setUtil(dtoValoracion.getUtil());
        valoracion.setMensaje(dtoValoracion.getMensaje());
        valoracion.setUsuario(usuario);
        valoracionRepository.save(valoracion);
    }

    public List<DtoValoracionResponse> readAll() {
        List<Valoracion> valoracions = valoracionRepository.findAll();
        return valoracions.stream().map(valoracion -> {
            DtoValoracionResponse dto = new DtoValoracionResponse();
            dto.setValoracion_id(valoracion.getValoracion_id());
            dto.setValoracion(valoracion.getValoracion());
            dto.setUtil(valoracion.getUtil());
            dto.setMensaje(valoracion.getMensaje());
            dto.setUsuario_nombre(valoracion.getUsuario().getNombre());
            return dto;
        }).toList();
    }
}
