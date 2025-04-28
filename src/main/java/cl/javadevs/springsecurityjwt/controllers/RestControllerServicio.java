package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.models.Servicio;
import cl.javadevs.springsecurityjwt.services.ServicioService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicio/")
public class RestControllerServicio {
    private ServicioService servicioService;
    @Autowired
    public RestControllerServicio(ServicioService phoneService) {
        this.servicioService = phoneService;
    }

    //Petición para crear un  servicio
    @PostMapping(value = "crear", headers = "Accept=application/json")
    public void crearServicio(@RequestBody Servicio servicio) {
        servicioService.crear(servicio);
    }

    //Petición para obtener todos los servicio en la BD
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public List<Servicio> listarServicio() {
        return servicioService.readAll();
    }

    //Petición para obtener servicio mediante "ID"
    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public Optional<Servicio> obtenerServicioPorId(@PathVariable Long id) {
        return servicioService.readOne(id);
    }

    //Petición para actualizar un servicio
    @PutMapping(value = "actualizar", headers = "Accept=application/json")
    public void actualizarServicio(@RequestBody Servicio servicio) {
        servicioService.update(servicio);
    }

    //Petición para eliminar un servicio por "Id"
    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public void eliminarServicio(@PathVariable Long id) {
        servicioService.delete(id);
    }
}
