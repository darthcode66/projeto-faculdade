# DER — Diagrama Entidade-Relacionamento (TransLog)

Modelagem original: Gabriela Andrade Brito (RA 20240868)
Refinamento e consolidação: Pedro H. A. Marcandali (RA 20241595)

Gerado a partir de `transportadora_completo.sql`. Renderizar em https://mermaid.live ou no GitHub.

```mermaid
erDiagram
    USUARIOS {
        bigint id PK
        varchar nome
        varchar email UK
        varchar senha
        enum perfil "ADMIN|OPERADOR|CONSULTA"
        boolean ativo
    }

    CLIENTES {
        bigint id PK
        varchar nome
        varchar cnpj UK
        varchar telefone
        varchar email
        varchar cidade
    }

    MOTORISTAS {
        bigint id PK
        varchar nome
        varchar cnh UK
        enum categoria_cnh "A|B|C|D|E"
        date validade_cnh
        varchar telefone
        boolean ativo
    }

    VEICULOS {
        bigint id PK
        varchar placa UK
        varchar modelo
        varchar marca
        int ano_fabricacao
        decimal capacidade_kg
        enum tipo "CAMINHAO_PEQ|MED|GR|CARRETA|VAN"
        enum status "DISPONIVEL|EM_VIAGEM|MANUTENCAO"
    }

    ROTAS {
        bigint id PK
        varchar origem
        varchar destino
        decimal distancia_km
        decimal tempo_estimado_horas
        decimal custo_pedagio
        varchar pedagios
    }

    CARGAS {
        bigint id PK
        varchar descricao
        decimal peso_kg
        decimal volume_m3
        enum tipo "GERAL|FRAGIL|REFRIG|PERIG|VIVA|GRANEL"
        boolean fragil
        boolean perigosa
    }

    VIAGENS {
        bigint id PK
        bigint veiculo_id FK
        bigint rota_id FK
        bigint carga_id FK
        bigint motorista_id FK
        bigint cliente_id FK
        datetime data_partida
        datetime data_chegada_prevista
        datetime data_chegada_real
        enum status "PLANEJADA|EM_TRANSITO|ENTREGUE|CANCELADA"
        varchar observacoes
    }

    FRETES {
        bigint id PK
        bigint viagem_id FK
        decimal distancia_km
        decimal peso_kg
        varchar tipo_carga
        decimal custo_combustivel
        decimal custo_manutencao
        decimal custo_pedagio
        decimal custo_fixo
        decimal custo_variavel
        decimal margem_lucro
        decimal valor_total
        datetime data_criacao
    }

    VIAGENS }o--|| VEICULOS    : "veiculo_id"
    VIAGENS }o--|| ROTAS       : "rota_id"
    VIAGENS }o--|| CARGAS      : "carga_id"
    VIAGENS }o--|| MOTORISTAS  : "motorista_id"
    VIAGENS }o--o| CLIENTES    : "cliente_id"
    FRETES  }o--o| VIAGENS     : "viagem_id"
```

## Evolução da modelagem

| Versão | Autoria | Tabelas | Características |
|---|---|---|---|
| **Etapa 1** (`transportadora.sql`) | Gabriela | 5 | clientes, motoristas, veiculos, viagens, fretes — origem/destino inline na viagem |
| **Consolidada** (`transportadora_completo.sql`) | Gabriela + Pedro | 8 | + usuarios, rotas, cargas — normalização e FKs adequadas |

## Convenções

- **PK:** `id` BIGINT AUTO_INCREMENT em todas as tabelas (padrão JPA)
- **FK:** sufixo `_id` (ex: `veiculo_id`)
- **Charset:** utf8mb4 (suporte a emojis e acentuação)
- **Engine:** InnoDB (transações + foreign keys)
- **Senhas:** armazenadas com hash BCrypt (não há armazenamento em texto puro)
- **Índices:** criados em campos de busca frequente (email, cnpj, placa, status)

## Cardinalidades

- **Veículo : Viagem** = 1 : N (um veículo faz muitas viagens ao longo do tempo, mas só uma por vez)
- **Rota : Viagem** = 1 : N (a mesma rota é reutilizada por várias viagens)
- **Carga : Viagem** = 1 : N (cada viagem leva uma carga; carga pode reaparecer em outras)
- **Motorista : Viagem** = 1 : N
- **Cliente : Viagem** = 1 : N (opcional — viagem pode não ter cliente associado)
- **Viagem : Frete** = 1 : N (uma viagem pode ter múltiplas simulações de frete antes de fechar o valor)
