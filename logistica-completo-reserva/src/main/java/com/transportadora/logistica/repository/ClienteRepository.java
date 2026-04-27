package com.transportadora.logistica.repository;

import com.transportadora.logistica.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCnpj(String cnpj);
}
