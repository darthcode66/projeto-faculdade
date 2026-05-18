package com.transportadora.logistica.service;

import com.transportadora.logistica.model.*;
import com.transportadora.logistica.repository.FreteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servico de precificacao de fretes (modulo do Vinicius).
 *
 * A logica de calculo (constantes de custo, multiplicadores por tipo de carga
 * e formula final) foi definida por Vinicius Eduardo da Silva (RA 20240289)
 * na entrega original em docs/precificacao-etapa1-vinicius/FreteService.java.
 *
 * Esta versao integra a logica ao Spring Boot e adiciona persistencia via JPA,
 * preservando integralmente as constantes e a formula do autor original.
 *
 * Formula: ValorTotal = (CustoFixo + CustoVariavel) x Multiplicador x (1 + Margem/100)
 */
@Service
public class FreteService {

    private final FreteRepository freteRepository;

    // Constantes de custo (simulacao)
    private static final double CUSTO_COMBUSTIVEL_POR_KM = 2.50;   // R$/km
    private static final double CUSTO_MANUTENCAO_POR_KM = 0.80;    // R$/km
    private static final double CUSTO_FIXO_VIAGEM = 150.00;        // R$ por viagem
    private static final double CUSTO_POR_KG = 0.12;               // R$/kg

    // Multiplicadores por tipo de carga
    private static final double MULT_REFRIGERADA = 1.40;
    private static final double MULT_PERIGOSA = 1.60;
    private static final double MULT_FRAGIL = 1.25;
    private static final double MULT_VIVA = 1.50;
    private static final double MULT_GRANEL = 0.90;
    private static final double MULT_GERAL = 1.00;

    public FreteService(FreteRepository freteRepository) {
        this.freteRepository = freteRepository;
    }

    public Frete calcularFrete(double distanciaKm, double pesoKg, String tipoCarga,
                               double custoPedagio, double margemLucro) {
        Frete frete = new Frete();
        frete.setDistanciaKm(distanciaKm);
        frete.setPesoKg(pesoKg);
        frete.setTipoCarga(tipoCarga);
        frete.setMargemLucro(margemLucro);

        // Custos variaveis
        double combustivel = distanciaKm * CUSTO_COMBUSTIVEL_POR_KM;
        double manutencao = distanciaKm * CUSTO_MANUTENCAO_POR_KM;
        double custoPeso = pesoKg * CUSTO_POR_KG;

        frete.setCustoCombustivel(arredondar(combustivel));
        frete.setCustoManutencao(arredondar(manutencao));
        frete.setCustoPedagio(arredondar(custoPedagio));
        frete.setCustoFixo(CUSTO_FIXO_VIAGEM);

        double custoVariavel = combustivel + manutencao + custoPeso + custoPedagio;
        frete.setCustoVariavel(arredondar(custoVariavel));

        // Multiplicador por tipo de carga
        double multiplicador = getMultiplicadorTipo(tipoCarga);

        // Custo total antes da margem
        double custoBase = (CUSTO_FIXO_VIAGEM + custoVariavel) * multiplicador;

        // Aplicar margem de lucro
        double valorFinal = custoBase * (1 + margemLucro / 100.0);
        frete.setValorTotal(arredondar(valorFinal));

        return frete;
    }

    public Frete calcularESalvar(double distanciaKm, double pesoKg, String tipoCarga,
                                  double custoPedagio, double margemLucro) {
        Frete frete = calcularFrete(distanciaKm, pesoKg, tipoCarga, custoPedagio, margemLucro);
        return freteRepository.save(frete);
    }

    public List<Frete> listarTodos() {
        return freteRepository.findAll();
    }

    public void deletar(Long id) {
        freteRepository.deleteById(id);
    }

    private double getMultiplicadorTipo(String tipo) {
        if (tipo == null) return MULT_GERAL;
        return switch (tipo.toUpperCase()) {
            case "REFRIGERADA" -> MULT_REFRIGERADA;
            case "PERIGOSA" -> MULT_PERIGOSA;
            case "FRAGIL" -> MULT_FRAGIL;
            case "VIVA" -> MULT_VIVA;
            case "GRANEL" -> MULT_GRANEL;
            default -> MULT_GERAL;
        };
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
