package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.horarioInstancia.response.DtoHorarioBarberoInstanciaResponse;
import cl.javadevs.springsecurityjwt.services.HorarioBarberoInstanciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horarioInstancia/")
@RequiredArgsConstructor
public class RestControllerHorarioBarberoInstancia {

    private final HorarioBarberoInstanciaService horarioBarberoInstanciaService;

    @GetMapping("/actual")
    public ResponseEntity<Map<String, List<DtoHorarioBarberoInstanciaResponse>>> obtenerSemanaAgrupada() {
        return ResponseEntity.ok(horarioBarberoInstanciaService.obtenerInstanciasAgrupadasPorDiaSemanaActual());
    }
}
