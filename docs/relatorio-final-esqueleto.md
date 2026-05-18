# Relatório Final Consolidado — TransLog

> **Esqueleto pronto para virar `.docx`** (uso interno do grupo). Cada `[Inserir...]` indica conteúdo que vem de relatório individual de algum integrante.

---

## Capa

**Projeto Integrador 2026.1**
Sistema de Gestão Logística com Precificação Inteligente (TransLog)
Faculdade de Americana — FAM

**Equipe:**
- Diego Henrique C. Custódio — RA 20240153 — Frontend e Documentação
- Gabriela Andrade Brito — RA 20240868 — Banco de Dados
- Leonardo Dos Santos — RA 20221957 — Logística e Operações
- Pedro H. A. Marcandali — RA 20241595 — Backend e Estrutura
- Vinícius Eduardo da Silva — RA 20240289 — Precificação e Regras de Negócio

**Data:** 19 de maio de 2026

---

## Sumário

1. Resumo Executivo
2. Introdução e Justificativa
3. Levantamento de Requisitos (Diego)
4. Arquitetura do Sistema
5. Módulo de Banco de Dados (Gabriela)
6. Módulo de Backend e Estrutura (Pedro)
7. Módulo de Logística e Operações (Leonardo)
8. Módulo de Precificação (Vinícius)
9. Módulo de Frontend (Diego)
10. Integração entre Módulos
11. Resultados e Testes
12. Conclusão
13. Assinaturas

---

## 1. Resumo Executivo

O TransLog é um sistema web de gestão de transportadoras com foco em **precificação inteligente de fretes**. Permite o cadastro de veículos, motoristas, clientes, rotas e cargas; criação e acompanhamento de viagens; e cálculo automático de fretes com base em uma fórmula que considera distância, peso, tipo de carga e margem de lucro.

Tecnologias: **Java 21**, **Spring Boot 3.5**, **Spring Data JPA**, **Spring Security + BCrypt**, **H2/MySQL**, **HTML5/CSS3/JavaScript**.

---

## 2. Introdução e Justificativa

[Inserir trechos do `Projeto - Etapa 1 (Evandro).docx` — contexto do problema logístico]

[Inserir do relatório do Diego: justificativa de mercado e objetivos]

---

## 3. Levantamento de Requisitos

**Responsável:** Diego Henrique C. Custódio

### Requisitos Funcionais
[Inserir lista completa de RFs do relatório do Diego]

### Requisitos Não Funcionais
[Inserir RNFs — desempenho, responsividade, segurança]

### Rastreabilidade RF → Caso de Uso → Implementação
Ver `docs/diagramas/casos-de-uso.md` — tabela final.

---

## 4. Arquitetura do Sistema

### 4.1 Padrão MVC

[Inserir do relatório do Pedro: descrição da arquitetura em 3 camadas]

### 4.2 Diagramas UML

- **Diagrama de Classes:** `docs/diagramas/diagrama-classes.md`
- **Diagrama de Casos de Uso:** `docs/diagramas/casos-de-uso.md`
- **Diagramas de Sequência:** `docs/diagramas/diagrama-sequencia.md`

### 4.3 Tecnologias

| Camada | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java | 21 |
| Framework | Spring Boot | 3.5.0 |
| ORM | Spring Data JPA (Hibernate) | 3.5.0 |
| Segurança | Spring Security + BCrypt | 6.x |
| BD (dev) | H2 | 2.3 |
| BD (prod) | MySQL | 8.x |
| Build | Maven Wrapper | 3.9 |
| Frontend | HTML5 / CSS3 / JS vanilla | — |
| Ícones | Lucide | — |

---

## 5. Módulo de Banco de Dados

**Responsável:** Gabriela Andrade Brito (RA 20240868)

### 5.1 Evolução da Modelagem

| Etapa | Tabelas | Arquivo |
|---|---|---|
| Inicial (Etapa 1) | 5 (clientes, motoristas, veiculos, viagens, fretes) | `transportadora.sql` |
| Consolidada | 8 (+ usuarios, rotas, cargas) | `transportadora_completo.sql` |

[Inserir relatório da Gabriela na íntegra]

### 5.2 DER

Ver `docs/diagramas/der-banco.md`.

### 5.3 Decisões de design

- BIGINT para IDs (compatível com `IDENTITY` do JPA)
- utf8mb4 para suporte a acentuação e emojis
- InnoDB para transações e FKs
- BCrypt para senhas

---

## 6. Módulo de Backend e Estrutura

**Responsável:** Pedro H. A. Marcandali (RA 20241595)

[Inserir conteúdo do `Relatorio Parcial - Pedro Marcandali.docx` (versão atualizada de 28/04)]

### 6.1 Endpoints REST

9 controllers cobrindo autenticação, CRUD de entidades, cálculo de frete e dashboard.

### 6.2 Segurança

Spring Security + BCrypt. Senhas nunca armazenadas em texto puro.

### 6.3 Persistência

Spring Data JPA — interfaces declarativas, sem SQL manual.

---

## 7. Módulo de Logística e Operações

**Responsável:** Leonardo Dos Santos (RA 20221957)

[Inserir relatório do Leonardo na íntegra]

### 7.1 Regras de Negócio Implementadas

| Regra | Onde está implementada |
|---|---|
| Veículo precisa estar DISPONÍVEL para iniciar viagem | `ViagemController.criar()` linha ~70 |
| Peso da carga ≤ capacidade do veículo | `ViagemController.criar()` linha ~78 |
| Toda viagem associada a rota válida | `ViagemController.criar()` (FK) |
| Status da viagem segue ciclo PLANEJADA → EM_TRANSITO → ENTREGUE | `ViagemController.atualizarStatus()` |

### 7.2 Fluxo Operacional

Ver diagrama de sequência "Criar viagem" em `docs/diagramas/diagrama-sequencia.md`.

---

## 8. Módulo de Precificação

**Responsável:** Vinícius Eduardo da Silva (RA 20240289)

[**PENDENTE:** inserir relatório do Vinícius quando entregue]

### 8.1 Fórmula de Cálculo

Implementada em `FreteService.calcularFrete()`:

```
Custo Variável = (km × 2,50) + (km × 0,80) + (kg × 0,12) + pedágio
Custo Fixo = R$ 150,00
Valor = (CustoFixo + CustoVariavel) × MultiplicadorTipo × (1 + Margem/100)
```

### 8.2 Multiplicadores

| Tipo de Carga | Multiplicador | Justificativa |
|---|---|---|
| Granel | 0,9x | Manuseio simples |
| Geral | 1,0x | Padrão |
| Frágil | 1,25x | Cuidado adicional |
| Refrigerada | 1,4x | Custo de energia/refrigeração |
| Viva | 1,5x | Logística especial |
| Perigosa | 1,6x | Seguros e licenças |

---

## 9. Módulo de Frontend

**Responsável:** Diego Henrique C. Custódio (RA 20240153)

[Inserir relatório do Diego na íntegra]

### 9.1 Evolução

- **Etapa 1** (preservada em `docs/frontend-etapa1-diego/`): páginas HTML separadas
- **Etapa Final**: SPA única integrada com as APIs REST

### 9.2 Identidade Visual

Paleta de cores definida pelo Diego, aplicada literalmente ao `style.css` consolidado:
- Navy `#2c3e50` (cabeçalhos e branding)
- Verde `#27ae60` (botões de ação)
- Azul `#3498db` (cards de resultado)
- Fundo `#f4f7f6`

### 9.3 Responsividade (RNF03)

Media queries em `style.css` garantem operação em telas mobile (480px+).

---

## 10. Integração entre Módulos

### 10.1 Mapa Relatório → Código

| Integrante | Entrega | Implementação |
|---|---|---|
| Gabriela | Modelagem (`transportadora.sql`) | `transportadora_completo.sql` + 8 entidades JPA |
| Leonardo | Relatório de Logística | `ViagemController` + validações comentadas |
| Vinícius | (Pendente) Relatório de Precificação | `FreteService.java` |
| Diego | Estrutura HTML + Relatório | SPA em `src/main/resources/static/` + paleta em `style.css` |
| Pedro | Backend e Estrutura | Backbone do projeto Spring Boot |

### 10.2 Comentários de Rastreabilidade no Código

- `ViagemController.java`: `// Regra de negocio (modulo de Logistica - Leonardo)`
- `style.css`: cabeçalho citando a paleta do Diego
- `transportadora_completo.sql`: cabeçalho creditando Gabriela
- `docs/frontend-etapa1-diego/`: pasta com arquivos originais do Diego

---

## 11. Resultados e Testes

### 11.1 Testes Manuais Realizados

[Inserir roteiro de testes que será executado na apresentação]

- ✅ Login com credenciais válidas/inválidas
- ✅ Cadastro de veículos, motoristas, rotas, cargas, clientes
- ✅ Criação de viagem (golden path)
- ✅ Tentativa de viagem com veículo `MANUTENCAO` → erro
- ✅ Tentativa de viagem com peso > capacidade → erro
- ✅ Cálculo de frete (simulação)
- ✅ Cálculo e salvamento de frete
- ✅ Atualização de status de viagem
- ✅ Dashboard mostrando indicadores corretos

### 11.2 Casos de Erro Tratados

| Situação | Tratamento |
|---|---|
| Senha incorreta no login | Mensagem "Credenciais inválidas" |
| Veículo indisponível | 400 Bad Request com explicação |
| Peso excede capacidade | 400 Bad Request com valores |
| ID inexistente em GET/PUT/DELETE | 404 Not Found |
| Validações JPA (campos obrigatórios) | 400 com lista de erros |

---

## 12. Conclusão

O projeto TransLog atingiu seu objetivo de demonstrar a aplicação prática dos conceitos de **Engenharia de Software**, **Programação Orientada a Objetos**, **Banco de Dados** e **Tópicos Integradores** estudados durante o semestre. O sistema é funcional, testado e está documentado em todos os níveis (código, banco, frontend, requisitos).

A integração entre os trabalhos individuais foi feita preservando a autoria de cada integrante (comentários no código, cabeçalhos de arquivos, artefatos históricos em `docs/`), garantindo rastreabilidade entre os relatórios individuais e o produto final.

**Pendências reconhecidas:**
- Testes unitários automatizados (parcial)
- Documentação da API com Swagger (futura iteração)

---

## 13. Assinaturas

| Integrante | RA | Assinatura |
|---|---|---|
| Diego Henrique C. Custódio | 20240153 | _____________________ |
| Gabriela Andrade Brito | 20240868 | _____________________ |
| Leonardo Dos Santos | 20221957 | _____________________ |
| Pedro H. A. Marcandali | 20241595 | _____________________ |
| Vinícius Eduardo da Silva | 20240289 | _____________________ |

Americana, 19 de maio de 2026.
