package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.models.HorarioRango;
import cl.javadevs.springsecurityjwt.models.Reserva;
import cl.javadevs.springsecurityjwt.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva,Long> {
    boolean existsByBarberoAndFechaReservaAndHorarioRango(Barbero barbero, LocalDate fechaReserva, HorarioRango horarioRango);
    List<Reserva> findByFechaReservaAndHorarioRango(LocalDate fecha, HorarioRango horarioRango);
    List<Reserva> findByFechaReserva(LocalDate fecha);
    List<Reserva> findByUsuario(Usuario usuario);
}
