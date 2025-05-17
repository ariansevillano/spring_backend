package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.servicio.request.DtoServicio;
import cl.javadevs.springsecurityjwt.dtos.servicio.response.DtoServicioResponse;
import cl.javadevs.springsecurityjwt.exceptions.ServicioNoEncontradoException;
import cl.javadevs.springsecurityjwt.exceptions.UsuarioExistenteException;
import cl.javadevs.springsecurityjwt.models.Servicio;
import cl.javadevs.springsecurityjwt.models.TipoServicio;
import cl.javadevs.springsecurityjwt.repositories.IServicioRepository;
import cl.javadevs.springsecurityjwt.repositories.ITipoServicioRepository;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final IServicioRepository servicioRepo;
    private final ITipoServicioRepository tipoServicioRepository;

    public void crear(DtoServicio dtoServicio) {
        Servicio servicio = new Servicio();
        servicio.setNombre(dtoServicio.getNombre());
        servicio.setPrecio(dtoServicio.getPrecio());
        servicio.setDescripcion(dtoServicio.getDescripcion());
        TipoServicio tipoServicio = tipoServicioRepository.findById(dtoServicio.getTipoServicio_id())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.TIPO_SERVICIO_NO_ENCONTRADO));
        servicio.setTipoServicio(tipoServicio);
        servicioRepo.save(servicio);
    }

    public List<DtoServicioResponse> readAll() {
        List<Servicio> servicios = servicioRepo.findAll();
        return servicios.stream().map(servicio -> {
            DtoServicioResponse dto = new DtoServicioResponse();
            dto.setServicio_id(servicio.getServicio_id());
            dto.setNombre(servicio.getNombre());
            dto.setPrecio(servicio.getPrecio());
            dto.setDescripcion(servicio.getDescripcion());
            dto.setNombre_tipoServicio(servicio.getTipoServicio().getNombre());
            return dto;
        }).toList();
    }

    public DtoServicioResponse readOne(Long id) {
        Servicio servicio = servicioRepo.findById(id)
                .orElseThrow(()-> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        DtoServicioResponse dto = new DtoServicioResponse();
        dto.setServicio_id(servicio.getServicio_id());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setNombre_tipoServicio(servicio.getTipoServicio().getNombre());
        return dto;
    }

    public void update(Long id, DtoServicio dtoServicio) {
        Servicio servicio = servicioRepo.findById(id)
                        .orElseThrow(()-> new ServicioNoEncontradoException(MensajeError.SERVICIO_NO_ENCONTRADO));
        servicio.setNombre(dtoServicio.getNombre());
        servicio.setPrecio(dtoServicio.getPrecio());
        servicio.setDescripcion(dtoServicio.getDescripcion());
        TipoServicio tipoServicio = tipoServicioRepository.findById(dtoServicio.getTipoServicio_id())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.TIPO_SERVICIO_NO_ENCONTRADO));
        servicio.setTipoServicio(tipoServicio);
        servicioRepo.save(servicio);
    }

    public void delete(Long id) {
        if (!servicioRepo.existsById(id)) {
            throw new ServicioNoEncontradoException("No se puede eliminar. Servicio no encontrado con Id: " + id);
        }
        servicioRepo.deleteById(id);
    }
}
