package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.Carga;
import com.transportadora.logistica.repository.CargaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargas")
public class CargaController {

    private final CargaRepository cargaRepository;

    public CargaController(CargaRepository cargaRepository) {
        this.cargaRepository = cargaRepository;
    }

    @GetMapping
    public List<Carga> listar() {
        return cargaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carga> buscar(@PathVariable Long id) {
        return cargaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Carga criar(@Valid @RequestBody Carga carga) {
        return cargaRepository.save(carga);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carga> atualizar(@PathVariable Long id, @Valid @RequestBody Carga dados) {
        return cargaRepository.findById(id).map(c -> {
            c.setDescricao(dados.getDescricao());
            c.setPesoKg(dados.getPesoKg());
            c.setVolumeM3(dados.getVolumeM3());
            c.setTipo(dados.getTipo());
            c.setFragil(dados.isFragil());
            c.setPerigosa(dados.isPerigosa());
            return ResponseEntity.ok(cargaRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return cargaRepository.findById(id).map(c -> {
            cargaRepository.delete(c);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
