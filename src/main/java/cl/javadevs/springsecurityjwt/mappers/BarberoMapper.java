package cl.javadevs.springsecurityjwt.mappers;

import cl.javadevs.springsecurityjwt.dtos.barbero.request.DtoBarbero;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoResponse;
import cl.javadevs.springsecurityjwt.models.Barbero;

public class BarberoMapper {

    public static Barbero toEntity(DtoBarbero dto){
        Barbero barbero = new Barbero();
        barbero.setNombre(dto.getNombre());
        return barbero;
    }
    public static DtoBarberoResponse toDto(Barbero barbero){
        DtoBarberoResponse dto = new DtoBarberoResponse();
        dto.setBarbero_id(barbero.getBarbero_id());
        dto.setNombre(barbero.getNombre());
        dto.setUrlBarbero(barbero.getUrlBarbero());
        return dto;
    }
}
