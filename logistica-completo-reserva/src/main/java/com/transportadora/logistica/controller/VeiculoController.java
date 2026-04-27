package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.Veiculo;
import com.transportadora.logistica.repository.VeiculoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping
    public List<Veiculo> listar() {
        return veiculoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Veiculo criar(@Valid @RequestBody Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @Valid @RequestBody Veiculo dados) {
        return veiculoRepository.findById(id).map(v -> {
            v.setPlaca(dados.getPlaca());
            v.setModelo(dados.getModelo());
            v.setMarca(dados.getMarca());
            v.setAnoFabricacao(dados.getAnoFabricacao());
            v.setCapacidadeKg(dados.getCapacidadeKg());
            v.setTipo(dados.getTipo());
            v.setStatus(dados.getStatus());
            return ResponseEntity.ok(veiculoRepository.save(v));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return veiculoRepository.findById(id).map(v -> {
            veiculoRepository.delete(v);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponiveis")
    public List<Veiculo> disponiveis() {
        return veiculoRepository.findByStatus(Veiculo.StatusVeiculo.DISPONIVEL);
    }
}
