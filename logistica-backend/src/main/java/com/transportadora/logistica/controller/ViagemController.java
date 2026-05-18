package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.*;
import com.transportadora.logistica.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/viagens")
public class ViagemController {

    private final ViagemRepository viagemRepository;
    private final VeiculoRepository veiculoRepository;
    private final RotaRepository rotaRepository;
    private final CargaRepository cargaRepository;
    private final MotoristaRepository motoristaRepository;
    private final ClienteRepository clienteRepository;

    public ViagemController(ViagemRepository viagemRepository,
                            VeiculoRepository veiculoRepository,
                            RotaRepository rotaRepository,
                            CargaRepository cargaRepository,
                            MotoristaRepository motoristaRepository,
                            ClienteRepository clienteRepository) {
        this.viagemRepository = viagemRepository;
        this.veiculoRepository = veiculoRepository;
        this.rotaRepository = rotaRepository;
        this.cargaRepository = cargaRepository;
        this.motoristaRepository = motoristaRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Viagem> listar() {
        return viagemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viagem> buscar(@PathVariable Long id) {
        return viagemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> dados) {
        Viagem viagem = new Viagem();

        Long veiculoId = Long.valueOf(dados.get("veiculoId").toString());
        Long rotaId = Long.valueOf(dados.get("rotaId").toString());
        Long cargaId = Long.valueOf(dados.get("cargaId").toString());
        Long motoristaId = Long.valueOf(dados.get("motoristaId").toString());

        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);
        Rota rota = rotaRepository.findById(rotaId).orElse(null);
        Carga carga = cargaRepository.findById(cargaId).orElse(null);
        Motorista motorista = motoristaRepository.findById(motoristaId).orElse(null);

        if (veiculo == null || rota == null || carga == null || motorista == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Veiculo, rota, carga ou motorista nao encontrados"));
        }

        // Regra de negocio (modulo de Logistica - Leonardo):
        // Um veiculo so pode iniciar uma viagem se estiver disponivel
        if (veiculo.getStatus() != Veiculo.StatusVeiculo.DISPONIVEL) {
            return ResponseEntity.badRequest().body(Map.of("erro",
                    "Veiculo " + veiculo.getPlaca() + " nao esta disponivel (status atual: "
                            + veiculo.getStatus() + ")"));
        }

        // Regra de negocio (modulo de Logistica - Leonardo):
        // O peso da carga nao pode ultrapassar a capacidade do veiculo
        if (carga.getPesoKg() > veiculo.getCapacidadeKg()) {
            return ResponseEntity.badRequest().body(Map.of("erro",
                    "Peso da carga (" + carga.getPesoKg() + " kg) excede a capacidade do veiculo "
                            + veiculo.getPlaca() + " (" + veiculo.getCapacidadeKg() + " kg)"));
        }

        viagem.setVeiculo(veiculo);
        viagem.setRota(rota);
        viagem.setCarga(carga);
        viagem.setMotorista(motorista);

        if (dados.containsKey("clienteId") && dados.get("clienteId") != null) {
            Long clienteId = Long.valueOf(dados.get("clienteId").toString());
            clienteRepository.findById(clienteId).ifPresent(viagem::setCliente);
        }

        viagem.setDataPartida(LocalDateTime.parse(dados.get("dataPartida").toString()));

        if (dados.containsKey("dataChegadaPrevista") && dados.get("dataChegadaPrevista") != null) {
            viagem.setDataChegadaPrevista(LocalDateTime.parse(dados.get("dataChegadaPrevista").toString()));
        }

        viagem.setObservacoes(dados.getOrDefault("observacoes", "").toString());

        veiculo.setStatus(Veiculo.StatusVeiculo.EM_VIAGEM);
        veiculoRepository.save(veiculo);

        return ResponseEntity.ok(viagemRepository.save(viagem));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> dados) {
        return viagemRepository.findById(id).map(v -> {
            Viagem.StatusViagem novoStatus = Viagem.StatusViagem.valueOf(dados.get("status"));
            v.setStatus(novoStatus);

            if (novoStatus == Viagem.StatusViagem.ENTREGUE) {
                v.setDataChegadaReal(LocalDateTime.now());
                if (v.getVeiculo() != null) {
                    v.getVeiculo().setStatus(Veiculo.StatusVeiculo.DISPONIVEL);
                    veiculoRepository.save(v.getVeiculo());
                }
            } else if (novoStatus == Viagem.StatusViagem.CANCELADA && v.getVeiculo() != null) {
                v.getVeiculo().setStatus(Veiculo.StatusVeiculo.DISPONIVEL);
                veiculoRepository.save(v.getVeiculo());
            }

            return ResponseEntity.ok(viagemRepository.save(v));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return viagemRepository.findById(id).map(v -> {
            viagemRepository.delete(v);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
