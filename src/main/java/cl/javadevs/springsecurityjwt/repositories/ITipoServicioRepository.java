package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoServicioRepository extends JpaRepository<TipoServicio, Long> {
}
