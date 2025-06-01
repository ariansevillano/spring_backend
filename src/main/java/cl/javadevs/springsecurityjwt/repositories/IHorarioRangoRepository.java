package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.HorarioRango;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHorarioRangoRepository extends JpaRepository<HorarioRango,Long> {

}
