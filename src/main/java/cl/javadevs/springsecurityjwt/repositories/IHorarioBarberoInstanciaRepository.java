package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.HorarioBarberoInstancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IHorarioBarberoInstanciaRepository extends JpaRepository<HorarioBarberoInstancia, Long> {
    void deleteByFechaBetween(LocalDate desde, LocalDate hasta);

    List<HorarioBarberoInstancia> findByFechaBetween(LocalDate inicio, LocalDate fin);
}
