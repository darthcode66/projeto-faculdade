package com.transportadora.logistica.controller;

import com.transportadora.logistica.model.*;
import com.transportadora.logistica.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final VeiculoRepository veiculoRepo;
    private final RotaRepository rotaRepo;
    private final CargaRepository cargaRepo;
    private final ViagemRepository viagemRepo;
    private final FreteRepository freteRepo;
    private final ClienteRepository clienteRepo;
    private final MotoristaRepository motoristaRepo;

    public RelatorioController(VeiculoRepository veiculoRepo, RotaRepository rotaRepo,
                               CargaRepository cargaRepo, ViagemRepository viagemRepo,
                               FreteRepository freteRepo, ClienteRepository clienteRepo,
                               MotoristaRepository motoristaRepo) {
        this.veiculoRepo = veiculoRepo;
        this.rotaRepo = rotaRepo;
        this.cargaRepo = cargaRepo;
        this.viagemRepo = viagemRepo;
        this.freteRepo = freteRepo;
        this.clienteRepo = clienteRepo;
        this.motoristaRepo = motoristaRepo;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> dados = new HashMap<>();

        dados.put("totalVeiculos", veiculoRepo.count());
        dados.put("veiculosDisponiveis", veiculoRepo.findByStatus(Veiculo.StatusVeiculo.DISPONIVEL).size());
        dados.put("veiculosEmViagem", veiculoRepo.findByStatus(Veiculo.StatusVeiculo.EM_VIAGEM).size());
        dados.put("veiculosManutencao", veiculoRepo.findByStatus(Veiculo.StatusVeiculo.MANUTENCAO).size());

        dados.put("totalRotas", rotaRepo.count());
        dados.put("totalCargas", cargaRepo.count());
        dados.put("totalClientes", clienteRepo.count());
        dados.put("totalMotoristas", motoristaRepo.count());
        dados.put("motoristasAtivos", motoristaRepo.findByAtivo(true).size());

        dados.put("totalViagens", viagemRepo.count());
        dados.put("viagensPlanejadas", viagemRepo.findByStatus(Viagem.StatusViagem.PLANEJADA).size());
        dados.put("viagensEmTransito", viagemRepo.findByStatus(Viagem.StatusViagem.EM_TRANSITO).size());
        dados.put("viagensEntregues", viagemRepo.findByStatus(Viagem.StatusViagem.ENTREGUE).size());

        dados.put("totalFretes", freteRepo.count());

        double receitaTotal = freteRepo.findAll().stream()
                .mapToDouble(Frete::getValorTotal)
                .sum();
        dados.put("receitaTotal", Math.round(receitaTotal * 100.0) / 100.0);

        return dados;
    }
}
