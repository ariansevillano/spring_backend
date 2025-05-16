package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBarberoRepository extends JpaRepository<Barbero, Long> {
    Optional<Barbero> findByEstado(Integer estado);

    Optional<Barbero> findByNombre(String nombre);
}
