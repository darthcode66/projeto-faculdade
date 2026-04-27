package com.transportadora.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "cargas")
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String descricao;

    @Positive
    private double pesoKg;

    private double volumeM3;

    @Enumerated(EnumType.STRING)
    private TipoCarga tipo;

    private boolean fragil;

    private boolean perigosa;

    public enum TipoCarga {
        GERAL, GRANEL, REFRIGERADA, PERIGOSA, FRAGIL, VIVA
    }

    public Carga() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public double getVolumeM3() { return volumeM3; }
    public void setVolumeM3(double volumeM3) { this.volumeM3 = volumeM3; }

    public TipoCarga getTipo() { return tipo; }
    public void setTipo(TipoCarga tipo) { this.tipo = tipo; }

    public boolean isFragil() { return fragil; }
    public void setFragil(boolean fragil) { this.fragil = fragil; }

    public boolean isPerigosa() { return perigosa; }
    public void setPerigosa(boolean perigosa) { this.perigosa = perigosa; }
}
