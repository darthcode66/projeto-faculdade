package com.transportadora.logistica.repository;

import com.transportadora.logistica.model.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {
    List<Viagem> findByStatus(Viagem.StatusViagem status);
}
