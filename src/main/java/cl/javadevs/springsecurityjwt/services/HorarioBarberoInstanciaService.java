package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.horarioInstancia.response.DtoHorarioBarberoInstanciaResponse;
import cl.javadevs.springsecurityjwt.repositories.IHorarioBarberoInstanciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioBarberoInstanciaService {

    private IHorarioBarberoInstanciaRepository horarioBarberoInstanciaRepository;

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
}
