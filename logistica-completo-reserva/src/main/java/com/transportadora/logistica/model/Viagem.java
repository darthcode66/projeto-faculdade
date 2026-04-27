package com.transportadora.logistica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viagens")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "rota_id")
    private Rota rota;

    @ManyToOne
    @JoinColumn(name = "carga_id")
    private Carga carga;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime dataPartida;

    private LocalDateTime dataChegadaPrevista;

    private LocalDateTime dataChegadaReal;

    @Enumerated(EnumType.STRING)
    private StatusViagem status = StatusViagem.PLANEJADA;

    private String observacoes;

    public enum StatusViagem {
        PLANEJADA, EM_TRANSITO, ENTREGUE, CANCELADA
    }

    public Viagem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Rota getRota() { return rota; }
    public void setRota(Rota rota) { this.rota = rota; }

    public Carga getCarga() { return carga; }
    public void setCarga(Carga carga) { this.carga = carga; }

    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDateTime dataPartida) { this.dataPartida = dataPartida; }

    public LocalDateTime getDataChegadaPrevista() { return dataChegadaPrevista; }
    public void setDataChegadaPrevista(LocalDateTime dataChegadaPrevista) { this.dataChegadaPrevista = dataChegadaPrevista; }

    public LocalDateTime getDataChegadaReal() { return dataChegadaReal; }
    public void setDataChegadaReal(LocalDateTime dataChegadaReal) { this.dataChegadaReal = dataChegadaReal; }

    public StatusViagem getStatus() { return status; }
    public void setStatus(StatusViagem status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
