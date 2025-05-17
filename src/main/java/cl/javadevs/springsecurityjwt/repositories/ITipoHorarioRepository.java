package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.TipoHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoHorarioRepository extends JpaRepository<TipoHorario, Long> {
}
