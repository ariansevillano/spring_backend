package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.exceptions.BarberoNoEncontradoException;
import cl.javadevs.springsecurityjwt.exceptions.ServicioNoEncontradoException;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.repositories.IBarberoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
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

    public void crear(Barbero barbero){
        barbero.setEstado(1);
        barberoRepository.save(barbero);}

    public List<Barbero> readAll(){return barberoRepository.findAll();}

    public Optional<Barbero> readOne(Long id){
        return Optional.ofNullable(barberoRepository.findById(id)
                .orElseThrow(()-> new BarberoNoEncontradoException("Barbero no encontrado con el id: "+id)));
    }

    public void update(Barbero barbero){barberoRepository.save(barbero)}

    public void deshabilitar(Long id) {
        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new BarberoNoEncontradoException("Barbero no encontrado con el id: " + id));
        barbero.setEstado(0);
        barberoRepository.save(barbero);
    }
}
