package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.HorarioBarberoBase;
import cl.javadevs.springsecurityjwt.util.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHorarioBarberoBaseRepository extends JpaRepository<HorarioBarberoBase,Long> {

    List<HorarioBarberoBase> findByDia(DiaSemana dia);
}
