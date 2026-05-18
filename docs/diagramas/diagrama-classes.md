# Diagrama de Classes UML — TransLog

Diagrama gerado a partir das entidades JPA em `logistica-completo-reserva/src/main/java/com/transportadora/logistica/model/`.

Renderizar em: https://mermaid.live ou no GitHub diretamente.

```mermaid
classDiagram
    class Usuario {
        -Long id
        -String nome
        -String email
        -String senha
        -Perfil perfil
        -boolean ativo
        +Perfil~ADMIN|OPERADOR|CONSULTA~
    }

    class Cliente {
        -Long id
        -String nome
        -String cnpj
        -String telefone
        -String email
        -String cidade
    }

    class Motorista {
        -Long id
        -String nome
        -String cnh
        -CategoriaCNH categoriaCnh
        -LocalDate validadeCnh
        -String telefone
        -boolean ativo
        +CategoriaCNH~A|B|C|D|E~
    }

    class Veiculo {
        -Long id
        -String placa
        -String modelo
        -String marca
        -int anoFabricacao
        -double capacidadeKg
        -TipoVeiculo tipo
        -StatusVeiculo status
        +TipoVeiculo~CAMINHAO_PEQUENO|MEDIO|GRANDE|CARRETA|VAN~
        +StatusVeiculo~DISPONIVEL|EM_VIAGEM|MANUTENCAO~
    }

    class Rota {
        -Long id
        -String origem
        -String destino
        -double distanciaKm
        -double tempoEstimadoHoras
        -double custoPedagio
        -String pedagios
    }

    class Carga {
        -Long id
        -String descricao
        -double pesoKg
        -double volumeM3
        -TipoCarga tipo
        -boolean fragil
        -boolean perigosa
        +TipoCarga~GERAL|FRAGIL|REFRIGERADA|PERIGOSA|VIVA|GRANEL~
    }

    class Viagem {
        -Long id
        -LocalDateTime dataPartida
        -LocalDateTime dataChegadaPrevista
        -LocalDateTime dataChegadaReal
        -StatusViagem status
        -String observacoes
        +StatusViagem~PLANEJADA|EM_TRANSITO|ENTREGUE|CANCELADA~
    }

    class Frete {
        -Long id
        -double distanciaKm
        -double pesoKg
        -String tipoCarga
        -double custoCombustivel
        -double custoManutencao
        -double custoPedagio
        -double custoFixo
        -double custoVariavel
        -double margemLucro
        -double valorTotal
        -LocalDateTime dataCriacao
    }

    class FreteService {
        +calcularFrete(distancia, peso, tipo, pedagio, margem) Frete
        +calcularESalvar(...) Frete
        +listarTodos() List~Frete~
        +deletar(id) void
    }

    Viagem "1" --> "1" Veiculo : usa
    Viagem "1" --> "1" Rota : percorre
    Viagem "1" --> "1" Carga : transporta
    Viagem "1" --> "1" Motorista : conduzida por
    Viagem "1" --> "0..1" Cliente : contratada por
    Frete "1" --> "0..1" Viagem : refere-se a
    FreteService ..> Frete : produz
```

## Relações principais

| Origem | Cardinalidade | Destino | Descrição |
|---|---|---|---|
| Viagem | N:1 | Veículo | Cada viagem usa um veículo (status muda para EM_VIAGEM) |
| Viagem | N:1 | Rota | Cada viagem percorre uma rota cadastrada |
| Viagem | N:1 | Carga | Cada viagem transporta uma carga |
| Viagem | N:1 | Motorista | Cada viagem é conduzida por um motorista |
| Viagem | N:0..1 | Cliente | Cliente é opcional (viagem interna pode não ter) |
| Frete | N:0..1 | Viagem | Frete pode ser calculado avulso (simulação) ou vinculado |

## Regras de negócio modeladas

Conforme módulo de Logística (Leonardo) — implementadas em `ViagemController.criar()`:

1. **Disponibilidade do veículo:** `veiculo.status == DISPONIVEL` antes de iniciar viagem
2. **Capacidade:** `carga.pesoKg <= veiculo.capacidadeKg`
3. **Rota válida:** referência a Rota cadastrada (FK)

Conforme módulo de Precificação (Vinícius) — implementado em `FreteService.calcularFrete()`:

```
ValorTotal = (CustoFixo + CustoVariavel) × MultiplicadorTipoCarga × (1 + MargemLucro/100)

CustoVariavel = (km × 2.50) + (km × 0.80) + (kg × 0.12) + pedagio
CustoFixo     = R$ 150.00
```

Multiplicadores: GRANEL 0.9 / GERAL 1.0 / FRAGIL 1.25 / REFRIGERADA 1.4 / VIVA 1.5 / PERIGOSA 1.6
