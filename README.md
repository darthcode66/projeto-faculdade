# Projeto Integrador - Sistema de Gestao Logistica (TransLog)

Sistema web de gestao de transportadora com precificacao inteligente de frete, desenvolvido como Projeto Integrador da FAM (Faculdade de Americana).

## Equipe

| Integrante | RA | Area |
|------------|----|----|
| Diego Henrique C. Custodio | 20240153 | Frontend e Documentacao |
| Gabriela Andrade Brito | 20240868 | Banco de Dados |
| Leonardo Dos Santos | 20221957 | Logistica e Operacoes |
| Pedro H. A. Marcandali | 20241595 | Backend e Estrutura |
| Vinicius Eduardo da Silva | 20240289 | Precificacao e Regras de Negocio |

## Documentacao Tecnica

| Documento | Conteudo |
|---|---|
| [docs/diagramas/diagrama-classes.md](docs/diagramas/diagrama-classes.md) | UML — Diagrama de Classes (Mermaid) |
| [docs/diagramas/der-banco.md](docs/diagramas/der-banco.md) | DER — Modelagem do Banco (Mermaid) |
| [docs/diagramas/casos-de-uso.md](docs/diagramas/casos-de-uso.md) | UML — Casos de Uso + Rastreabilidade RF/RNF |
| [docs/diagramas/diagrama-sequencia.md](docs/diagramas/diagrama-sequencia.md) | UML — Diagramas de Sequencia (Login, Viagem, Frete) |
| [docs/manual/manual-usuario.md](docs/manual/manual-usuario.md) | Manual do Usuario |
| [docs/frontend-etapa1-diego/](docs/frontend-etapa1-diego/) | Arquivos originais do frontend (Etapa 1 - Diego) |
| [docs/precificacao-etapa1-vinicius/](docs/precificacao-etapa1-vinicius/) | Logica original do calculo de frete (Etapa 1 - Vinicius) |

## Estrutura do Repositorio

```
Projeto Faculdade/
  logistica-backend/             -- Sistema apenas com backend e APIs REST
  logistica-completo-reserva/    -- Sistema completo (backend + frontend) - reserva
  transportadora_completo.sql    -- Script MySQL com modelagem completa
  transportadora.sql             -- Script SQL original (Gabriela)
  conversa-claude.jsonl          -- Historico da conversa de desenvolvimento
  *.docx, *.pdf                  -- Documentacao do projeto
```

## Tecnologias

- **Java 21** + **Spring Boot 3.5.0**
- **Spring Data JPA** (Hibernate)
- **Spring Security** + **BCrypt**
- **H2 Database** (desenvolvimento) / **MySQL** (producao)
- **Maven** (com wrapper)
- **HTML / CSS / JavaScript** (frontend)
- **Lucide Icons** (icones SVG)

## Como Executar

Pre-requisitos: **Java 21+** instalado.

### Backend completo (com frontend web)

```bash
cd logistica-completo-reserva
./mvnw spring-boot:run
```

Acesse: http://localhost:8080

Login: `admin@transportadora.com` / `admin123`

### Apenas backend (so APIs REST)

```bash
cd logistica-backend
./mvnw spring-boot:run
```

APIs disponiveis em http://localhost:8080/api/

## Modulos do Sistema

| Modulo | Endpoint | Funcionalidade |
|--------|----------|----------------|
| Autenticacao | `/api/auth` | Login e registro de usuarios |
| Veiculos | `/api/veiculos` | Cadastro da frota |
| Rotas | `/api/rotas` | Cadastro de trajetos |
| Cargas | `/api/cargas` | Cadastro de cargas |
| Clientes | `/api/clientes` | Cadastro de clientes |
| Motoristas | `/api/motoristas` | Cadastro de motoristas |
| Viagens | `/api/viagens` | Operacoes de transporte |
| Fretes | `/api/fretes` | Calculo e historico de fretes |
| Relatorios | `/api/relatorios/dashboard` | Indicadores gerais |

## Calculo de Frete

A precificacao considera:

- Distancia (R$ 2,50/km combustivel + R$ 0,80/km manutencao)
- Peso (R$ 0,12/kg)
- Custo fixo por viagem (R$ 150,00)
- Custo de pedagio
- Multiplicador por tipo de carga (granel 0.9x ate perigosa 1.6x)
- Margem de lucro (definida pelo usuario)

**Formula:** `(Custo Fixo + Custo Variavel) x Multiplicador x (1 + Margem/100)`

## Cronograma

- **03/03** - Levantamento de Requisitos
- **10/03** - Diagramas UML e estrutura inicial
- **17/03** - Modelos do banco e conexao
- **24/03** - Funcionalidades principais
- **31/03** - APIs e regras de negocio
- **07/04** - Teste e integracao
- **14/04** - Testes gerais
- **12/05** - Homologacao
- **19/05** - Apresentacao final

## Licenca

Projeto academico - FAM 2026.
