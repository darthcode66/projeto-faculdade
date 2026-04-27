package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.Rota;
import com.transportadora.logistica.repository.RotaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rotas")
public class RotaController {

    private final RotaRepository rotaRepository;

    public RotaController(RotaRepository rotaRepository) {
        this.rotaRepository = rotaRepository;
    }

    @GetMapping
    public List<Rota> listar() {
        return rotaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rota> buscar(@PathVariable Long id) {
        return rotaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Rota criar(@Valid @RequestBody Rota rota) {
        return rotaRepository.save(rota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rota> atualizar(@PathVariable Long id, @Valid @RequestBody Rota dados) {
        return rotaRepository.findById(id).map(r -> {
            r.setOrigem(dados.getOrigem());
            r.setDestino(dados.getDestino());
            r.setDistanciaKm(dados.getDistanciaKm());
            r.setTempoEstimadoHoras(dados.getTempoEstimadoHoras());
            r.setPedagios(dados.getPedagios());
            r.setCustoPedagio(dados.getCustoPedagio());
            return ResponseEntity.ok(rotaRepository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return rotaRepository.findById(id).map(r -> {
            rotaRepository.delete(r);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
