# Precificação - Etapa 1 (Vinícius Eduardo da Silva)

**Autor:** Vinícius Eduardo da Silva — RA 20240289
**Origem:** entrega direta dos arquivos `FreteService.java` e `Main.java`
**Status:** artefato histórico — lógica integrada ao SPA consolidado

## Conteúdo

Esta pasta preserva os arquivos originais entregues pelo Vinícius com a lógica
da precificação inteligente do sistema TransLog, antes da integração ao Spring Boot:

- `FreteService.java` — POJO standalone com a fórmula de cálculo de frete
- `Main.java` — programa de teste com caso de uso de exemplo

## Caso de teste do Main.java

```java
calcularFrete(
    distanciaKm = 300,
    pesoKg = 500,
    tipoCarga = "fragil",
    custoPedagio = 50,
    margemLucro = 20
)
```

## Fórmula entregue pelo Vinícius

```
Custo por km = R$ 2,50 (combustível) + R$ 0,80 (manutenção) = R$ 3,30
Custo variável = (km × custoPorKm) + (kg × R$ 0,12) + pedágio
Custo base = R$ 150,00 (fixo) + custoVariável
Valor com tipo = custoBase × multiplicador
Valor final = valorComTipo × (1 + margem/100)
```

## Multiplicadores

| Tipo de carga | Multiplicador |
|---|---|
| Granel | 0,9x |
| Geral | 1,0x |
| Frágil | 1,25x |
| Refrigerada | 1,4x |
| Viva | 1,5x |
| Perigosa | 1,6x |

## Evolução para o sistema consolidado

| Etapa 1 (esta pasta) | Etapa 2 (`FreteService.java` do projeto Spring Boot) |
|---|---|
| POJO Java puro standalone | `@Service` integrado ao Spring |
| Retorna `double` (valor final) | Retorna objeto `Frete` com todos os custos discriminados |
| Sem persistência | Persiste via `FreteRepository` em `calcularESalvar()` |
| Multiplicadores via `String` | Mesma lógica, agora em `switch` expression Java 21 |
| **Fórmula e constantes IDÊNTICAS** | **Fórmula e constantes preservadas literalmente** |

## Aproveitamento no produto final

A lógica de cálculo do Vinícius foi **integralmente preservada** no
`FreteService.java` do módulo `logistica-completo-reserva`. Todas as constantes
(R$ 2,50/km, R$ 0,80/km, R$ 0,12/kg, R$ 150,00 fixo) e todos os multiplicadores
foram mantidos sem alteração. A evolução foi apenas estrutural (integração ao
Spring + persistência), sem mudança de regras de negócio.

Ver comentário de crédito no cabeçalho de
`logistica-completo-reserva/src/main/java/com/transportadora/logistica/service/FreteService.java`.
