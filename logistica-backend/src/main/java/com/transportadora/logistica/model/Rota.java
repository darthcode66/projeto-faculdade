package com.transportadora.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "rotas")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String origem;

    @NotBlank
    private String destino;

    @Positive
    private double distanciaKm;

    private double tempoEstimadoHoras;

    private String pedagios;

    private double custoPedagio;

    public Rota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }

    public double getTempoEstimadoHoras() { return tempoEstimadoHoras; }
    public void setTempoEstimadoHoras(double tempoEstimadoHoras) { this.tempoEstimadoHoras = tempoEstimadoHoras; }

    public String getPedagios() { return pedagios; }
    public void setPedagios(String pedagios) { this.pedagios = pedagios; }

    public double getCustoPedagio() { return custoPedagio; }
    public void setCustoPedagio(double custoPedagio) { this.custoPedagio = custoPedagio; }
}
