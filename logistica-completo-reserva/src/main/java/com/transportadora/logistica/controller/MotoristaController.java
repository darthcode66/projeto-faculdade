package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.Motorista;
import com.transportadora.logistica.repository.MotoristaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {

    private final MotoristaRepository motoristaRepository;

    public MotoristaController(MotoristaRepository motoristaRepository) {
        this.motoristaRepository = motoristaRepository;
    }

    @GetMapping
    public List<Motorista> listar() {
        return motoristaRepository.findAll();
    }

    @GetMapping("/ativos")
    public List<Motorista> ativos() {
        return motoristaRepository.findByAtivo(true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motorista> buscar(@PathVariable Long id) {
        return motoristaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Motorista criar(@Valid @RequestBody Motorista motorista) {
        return motoristaRepository.save(motorista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Motorista> atualizar(@PathVariable Long id, @Valid @RequestBody Motorista dados) {
        return motoristaRepository.findById(id).map(m -> {
            m.setNome(dados.getNome());
            m.setCnh(dados.getCnh());
            m.setCategoriaCnh(dados.getCategoriaCnh());
            m.setValidadeCnh(dados.getValidadeCnh());
            m.setTelefone(dados.getTelefone());
            m.setAtivo(dados.isAtivo());
            return ResponseEntity.ok(motoristaRepository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return motoristaRepository.findById(id).map(m -> {
            motoristaRepository.delete(m);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
