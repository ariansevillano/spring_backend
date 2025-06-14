package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.dtos.horarioBase.DtoHorarioBase;
import cl.javadevs.springsecurityjwt.exceptions.BarberoNoEncontradoException;
import cl.javadevs.springsecurityjwt.exceptions.TipoHorarioNoEncotradoException;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.models.HorarioBarberoBase;
import cl.javadevs.springsecurityjwt.models.HorarioBarberoInstancia;
import cl.javadevs.springsecurityjwt.models.TipoHorario;
import cl.javadevs.springsecurityjwt.repositories.IBarberoRepository;
import cl.javadevs.springsecurityjwt.repositories.IHorarioBarberoBaseRepository;
import cl.javadevs.springsecurityjwt.repositories.IHorarioBarberoInstanciaRepository;
import cl.javadevs.springsecurityjwt.repositories.ITipoHorarioRepository;
import cl.javadevs.springsecurityjwt.util.DiaSemana;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioBarberoBaseService {

    private final IHorarioBarberoBaseRepository horarioBarberoBaseRepository;
    private final IBarberoRepository barberoRepository;
    private final ITipoHorarioRepository tipoHorarioRepository;
    private final IHorarioBarberoInstanciaRepository horarioBarberoInstanciaRepository;


    public void crearHorarioBaseInicial(Long barbero_id){
        List<HorarioBarberoBase> horarios = new ArrayList<>();

        for (DiaSemana dia : DiaSemana.values()){
            for(long tipoHorarioId = 1; tipoHorarioId <= 3; tipoHorarioId++){
                HorarioBarberoBase horario = new HorarioBarberoBase();
                Barbero barbero = barberoRepository.findById(barbero_id)
                                .orElseThrow(() -> new BarberoNoEncontradoException(MensajeError.BARBERO_NO_ENCONTRADO));
                horario.setBarbero(barbero);
                horario.setDia(dia);
                TipoHorario tipoHorario = tipoHorarioRepository.findById(tipoHorarioId)
                                .orElseThrow(() -> new TipoHorarioNoEncotradoException(MensajeError.TIPO_HORARIO_NO_ENCOTRADO));
                horario.setTipoHorario(tipoHorario);
                horario.setEst_id(null);
                horario.setEstado(1);
                horarios.add(horario);
            }
        }
        horarioBarberoBaseRepository.saveAll(horarios);
    }

    @Transactional
    public void actualizarTurnosDia(DtoHorarioBase dtoHorarioBase){
        DiaSemana dia =  dtoHorarioBase.getDia();
        Map<Long,List<Long>> turnosPorTipo = dtoHorarioBase.getTurnosPorTipo();

        List<HorarioBarberoBase> registrosDia = horarioBarberoBaseRepository.findByDia(dia);

        for (HorarioBarberoBase registro : registrosDia){
            Long tipoHorarioId = registro.getTipoHorario().getId();
            Long barberoId = registro.getBarbero().getBarbero_id();

            List<Long> barberosAsignados = turnosPorTipo.getOrDefault(tipoHorarioId,List.of());

            if (barberosAsignados.contains(barberoId)){
                registro.setEst_id(1);
            } else {
                registro.setEst_id(null);
            }
        }
        horarioBarberoBaseRepository.saveAll(registrosDia);
    }

    @Transactional
    public void confirmarHorarioBaseParaSemanasSiguientes(){

        LocalDate proximoLunes = obtenerProximoLunes();
        LocalDate proximoDomingo = proximoLunes.plusDays(6);

        // 🧹 Elimina horarios de la próxima semana antes de insertar
        horarioBarberoInstanciaRepository.deleteByFechaBetween(proximoLunes, proximoDomingo);

        List<HorarioBarberoBase> baseActivos = horarioBarberoBaseRepository.findAll()
                .stream()
                .filter(r -> r.getEst_id() != null && r.getEst_id()==1)
                .collect(Collectors.toList());


        List<HorarioBarberoInstancia> instancias = new ArrayList<>();
        for(HorarioBarberoBase base : baseActivos){
            DiaSemana dia = base.getDia();
            LocalDate fechaExacta = proximoLunes.plusDays(dia.ordinal());

            HorarioBarberoInstancia nueva = new HorarioBarberoInstancia();
            nueva.setBarbero(base.getBarbero());
            nueva.setTipoHorario(base.getTipoHorario());
            nueva.setDia(dia);
            nueva.setFecha(fechaExacta);

            instancias.add(nueva);
        }
        horarioBarberoInstanciaRepository.saveAll(instancias);
    }

    private LocalDate obtenerProximoLunes() {
        LocalDate hoy = LocalDate.now();
        return hoy.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }


}
