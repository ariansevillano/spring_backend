package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.barbero.request.DtoBarbero;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoResponse;
import cl.javadevs.springsecurityjwt.exceptions.BarberoNoEncontradoException;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.repositories.IBarberoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarberoService {

    private IBarberoRepository barberoRepository;
    private AuthenticationManager authenticationManager;
    @Autowired
    public BarberoService(IBarberoRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    public void crear(DtoBarbero dtoBarbero){
        Barbero barbero = new Barbero();
        barbero.setNombre(dtoBarbero.getNombre());
        barbero.setEstado(1);
        barberoRepository.save(barbero);
    }

    public List<DtoBarberoResponse> readAll(){
        List<Barbero> barberos = barberoRepository.findAll();
        return barberos.stream().filter(barbero ->
                barbero.getEstado() == 1)
                .map(barbero -> {
                    DtoBarberoResponse dto = new DtoBarberoResponse();
                    dto.setBarbero_id(barbero.getBarbero_id());
                    dto.setNombre(barbero.getNombre());
                    return dto;
                }).toList();
        }

    public Optional<Barbero> readOne(Long id){
        return Optional.ofNullable(barberoRepository.findById(id)
                .orElseThrow(()-> new BarberoNoEncontradoException("Barbero no encontrado con el id: "+id)));
    }

    public void update(Long id,DtoBarbero dtoBarbero){
        Barbero barbero = barberoRepository.findById(id)
                        .orElseThrow(() -> new BarberoNoEncontradoException("Barbero no encontrada con id: " + id));
        barbero.setNombre(dtoBarbero.getNombre());
        barberoRepository.save(barbero);}

    public void deshabilitar(Long id) {
        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new BarberoNoEncontradoException("Barbero no encontrado con el id: " + id));
        barbero.setEstado(0);
        barberoRepository.save(barbero);
    }
}
