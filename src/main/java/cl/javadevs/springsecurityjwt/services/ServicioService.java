package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.exceptions.ServicioNoEncontradoException;
import cl.javadevs.springsecurityjwt.models.Servicio;
import cl.javadevs.springsecurityjwt.repositories.IServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {
    private IServicioRepository servicioRepo;

    @Autowired
    public ServicioService(IServicioRepository servicioRepo) {
        this.servicioRepo = servicioRepo;
    }

    //Creamos un celular
    public void crear(Servicio servicio) {
        servicioRepo.save(servicio);
    }

    //Obtenemos toda una lista de celulares
    public List<Servicio> readAll() {
        return servicioRepo.findAll();
    }

    //Obtenemos un celular por su id
    public Optional<Servicio> readOne(Long id) {
        return Optional.ofNullable(servicioRepo.findById(id)
                .orElseThrow(() -> new ServicioNoEncontradoException("Servicio no encontrado con Id: "+id)));
    }

    //Actualizamos un celular
    public void update(Servicio servicio) {
        servicioRepo.save(servicio);
    }

    //Eliminamos un celular
    public void delete(Long id) {
        if (!servicioRepo.existsById(id)) {
            throw new ServicioNoEncontradoException("No se puede eliminar. Servicio no encontrado con Id: " + id);
        }
        servicioRepo.deleteById(id);
    }
}
