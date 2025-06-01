package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.horarioRangos.response.DtoHorarioRangoResponse;
import cl.javadevs.springsecurityjwt.dtos.servicio.response.DtoServicioResponse;
import cl.javadevs.springsecurityjwt.exceptions.UsuarioExistenteException;
import cl.javadevs.springsecurityjwt.models.HorarioRango;
import cl.javadevs.springsecurityjwt.models.Servicio;
import cl.javadevs.springsecurityjwt.repositories.IHorarioRangoRepository;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioRangoService {

    private final IHorarioRangoRepository horarioRangoRepository;
    public List<DtoHorarioRangoResponse> readAll() {
        List<HorarioRango> horarioRango = horarioRangoRepository.findAll();
        return horarioRango.stream().map(rango -> {
            DtoHorarioRangoResponse dto = new DtoHorarioRangoResponse();
            dto.setHorarioRango_id(rango.getHorarioRango_id());
            dto.setRango(rango.getRango());
            dto.setTipoHorario(rango.getTipoHorario().getNombre());
            return dto;
        }).toList();
    }

    public DtoHorarioRangoResponse readOne(Long id) {
        HorarioRango horarioRango = horarioRangoRepository.findById(id)
                .orElseThrow(()-> new UsuarioExistenteException("No existe el rango"));
        DtoHorarioRangoResponse dto = new DtoHorarioRangoResponse();
        dto.setHorarioRango_id(horarioRango.getHorarioRango_id());
        dto.setRango(horarioRango.getRango());
        dto.setTipoHorario(horarioRango.getTipoHorario().getNombre());
        return dto;
    }
}
