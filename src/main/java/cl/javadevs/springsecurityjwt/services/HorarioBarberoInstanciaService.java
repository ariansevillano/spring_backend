package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoDisponible;
import cl.javadevs.springsecurityjwt.dtos.horarioInstancia.response.DtoHorarioBarberoInstanciaResponse;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.models.HorarioBarberoInstancia;
import cl.javadevs.springsecurityjwt.models.HorarioRango;
import cl.javadevs.springsecurityjwt.models.Reserva;
import cl.javadevs.springsecurityjwt.repositories.IHorarioBarberoInstanciaRepository;
import cl.javadevs.springsecurityjwt.repositories.IHorarioRangoRepository;
import cl.javadevs.springsecurityjwt.repositories.IReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioBarberoInstanciaService {

    private final IHorarioBarberoInstanciaRepository horarioBarberoInstanciaRepository;
    private final IHorarioRangoRepository horarioRangoRepository;
    private final IReservaRepository reservaRepository;

    public Map<String, List<DtoHorarioBarberoInstanciaResponse>> obtenerInstanciasAgrupadasPorDiaSemanaActual() {
        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = hoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return horarioBarberoInstanciaRepository.findByFechaBetween(lunes, domingo).stream()
                .map(instancia -> new DtoHorarioBarberoInstanciaResponse(
                        instancia.getFecha(),
                        instancia.getDia().name(),
                        instancia.getTipoHorario().getNombre(),
                        instancia.getBarbero().getNombre()))
                .sorted(Comparator.comparing(DtoHorarioBarberoInstanciaResponse::getFecha)
                        .thenComparing(DtoHorarioBarberoInstanciaResponse::getTipoHorario))
                .collect(Collectors.groupingBy(DtoHorarioBarberoInstanciaResponse::getDia, LinkedHashMap::new, Collectors.toList()));
    }


    public List<DtoBarberoDisponible> listarBarberosDisponibles(LocalDate fecha, Long tipoHorarioId, Long horarioRangoId) {
        // 1. Barberos que trabajan ese día y tipoHorario
        List<HorarioBarberoInstancia> instancias = horarioBarberoInstanciaRepository
                .findByFechaAndTipoHorario_Id(fecha, tipoHorarioId);

        // 2. Reservas existentes para ese día y rango
        HorarioRango horarioRango = horarioRangoRepository.findById(horarioRangoId)
                .orElseThrow(() -> new RuntimeException("HorarioRango no encontrado"));
        List<Reserva> reservas = reservaRepository.findByFechaReservaAndHorarioRango(fecha, horarioRango);

        Set<Long> barberosReservados = reservas.stream()
                .map(r -> r.getBarbero().getBarbero_id())
                .collect(Collectors.toSet());

        // 3. Mapear a DTO con flag disponible
        return instancias.stream()
                .map(instancia -> {
                    Barbero barbero = instancia.getBarbero();
                    DtoBarberoDisponible dto = new DtoBarberoDisponible();
                    dto.setBarberoId(barbero.getBarbero_id());
                    dto.setNombre(barbero.getNombre());
                    dto.setUrlBarbero(barbero.getUrlBarbero());
                    dto.setDisponible(!barberosReservados.contains(barbero.getBarbero_id()));
                    return dto;
                }).toList();
    }
}
