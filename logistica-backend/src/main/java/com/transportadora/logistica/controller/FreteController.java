package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.Frete;
import com.transportadora.logistica.service.FreteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fretes")
public class FreteController {

    private final FreteService freteService;

    public FreteController(FreteService freteService) {
        this.freteService = freteService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<Frete> calcular(@RequestBody Map<String, Object> dados) {
        double distancia = Double.parseDouble(dados.get("distanciaKm").toString());
        double peso = Double.parseDouble(dados.get("pesoKg").toString());
        String tipoCarga = dados.getOrDefault("tipoCarga", "GERAL").toString();
        double pedagio = Double.parseDouble(dados.getOrDefault("custoPedagio", "0").toString());
        double margem = Double.parseDouble(dados.getOrDefault("margemLucro", "15").toString());

        Frete frete = freteService.calcularESalvar(distancia, peso, tipoCarga, pedagio, margem);
        return ResponseEntity.ok(frete);
    }

    @PostMapping("/simular")
    public ResponseEntity<Frete> simular(@RequestBody Map<String, Object> dados) {
        double distancia = Double.parseDouble(dados.get("distanciaKm").toString());
        double peso = Double.parseDouble(dados.get("pesoKg").toString());
        String tipoCarga = dados.getOrDefault("tipoCarga", "GERAL").toString();
        double pedagio = Double.parseDouble(dados.getOrDefault("custoPedagio", "0").toString());
        double margem = Double.parseDouble(dados.getOrDefault("margemLucro", "15").toString());

        Frete frete = freteService.calcularFrete(distancia, peso, tipoCarga, pedagio, margem);
        return ResponseEntity.ok(frete);
    }

    @GetMapping
    public List<Frete> listar() {
        return freteService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        freteService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
