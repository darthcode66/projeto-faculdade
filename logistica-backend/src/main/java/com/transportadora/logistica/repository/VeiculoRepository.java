package com.transportadora.logistica.repository;

import com.transportadora.logistica.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByStatus(Veiculo.StatusVeiculo status);
    boolean existsByPlaca(String placa);
}
