package cl.javadevs.springsecurityjwt.util;

import cl.javadevs.springsecurityjwt.services.HorarioBarberoBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HorarioBaseScheduler {
    private HorarioBarberoBaseService horarioBarberoBaseService;

    @Scheduled(cron = "0 50 23 ? * SUN")
    public void confirmarHorarioAutomatico() {
        horarioBarberoBaseService.confirmarHorarioBaseParaSemanasSiguientes();
    }
}
