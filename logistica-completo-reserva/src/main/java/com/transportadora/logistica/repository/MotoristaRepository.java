package com.transportadora.logistica.repository;

import com.transportadora.logistica.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    boolean existsByCnh(String cnh);
    List<Motorista> findByAtivo(boolean ativo);
}
