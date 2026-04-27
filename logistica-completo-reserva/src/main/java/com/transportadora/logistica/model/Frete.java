package com.transportadora.logistica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fretes")
public class Frete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

    private double distanciaKm;
    private double pesoKg;
    private String tipoCarga;

    // Custos
    private double custoCombustivel;
    private double custoManutencao;
    private double custoPedagio;
    private double custoFixo;
    private double custoVariavel;

    // Precificacao
    private double margemLucro; // percentual (ex: 15.0 = 15%)
    private double valorTotal;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Frete() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Viagem getViagem() { return viagem; }
    public void setViagem(Viagem viagem) { this.viagem = viagem; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public String getTipoCarga() { return tipoCarga; }
    public void setTipoCarga(String tipoCarga) { this.tipoCarga = tipoCarga; }

    public double getCustoCombustivel() { return custoCombustivel; }
    public void setCustoCombustivel(double custoCombustivel) { this.custoCombustivel = custoCombustivel; }

    public double getCustoManutencao() { return custoManutencao; }
    public void setCustoManutencao(double custoManutencao) { this.custoManutencao = custoManutencao; }

    public double getCustoPedagio() { return custoPedagio; }
    public void setCustoPedagio(double custoPedagio) { this.custoPedagio = custoPedagio; }

    public double getCustoFixo() { return custoFixo; }
    public void setCustoFixo(double custoFixo) { this.custoFixo = custoFixo; }

    public double getCustoVariavel() { return custoVariavel; }
    public void setCustoVariavel(double custoVariavel) { this.custoVariavel = custoVariavel; }

    public double getMargemLucro() { return margemLucro; }
    public void setMargemLucro(double margemLucro) { this.margemLucro = margemLucro; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
