# Diagramas de Sequência — TransLog

Os três fluxos mais relevantes para a apresentação.

## 1. Autenticação (Login)

```mermaid
sequenceDiagram
    actor Usuario
    participant Frontend as SPA<br/>(app.js)
    participant Auth as AuthController
    participant Repo as UsuarioRepository
    participant BCrypt as PasswordEncoder
    participant DB as Banco H2/MySQL

    Usuario->>Frontend: Informa email + senha
    Frontend->>Auth: POST /api/auth/login
    Auth->>Repo: findByEmail(email)
    Repo->>DB: SELECT * FROM usuarios WHERE email = ?
    DB-->>Repo: Usuario (com senha hash)
    Repo-->>Auth: Usuario
    Auth->>BCrypt: matches(senha, hashArmazenado)
    BCrypt-->>Auth: true
    Auth-->>Frontend: 200 OK + dados do usuario
    Frontend->>Frontend: Salva sessao em sessionStorage
    Frontend-->>Usuario: Redireciona para Dashboard
```

## 2. Criar viagem (com regras de negócio do Leonardo)

```mermaid
sequenceDiagram
    actor Operador
    participant Frontend as SPA
    participant Viagem as ViagemController
    participant VRepo as VeiculoRepository
    participant CRepo as CargaRepository
    participant VgRepo as ViagemRepository
    participant DB as Banco

    Operador->>Frontend: Preenche formulario de viagem
    Frontend->>Viagem: POST /api/viagens

    Viagem->>VRepo: findById(veiculoId)
    VRepo->>DB: SELECT * FROM veiculos WHERE id = ?
    DB-->>VRepo: Veiculo
    VRepo-->>Viagem: Veiculo

    Note over Viagem: Regra Leonardo #1<br/>Verificar disponibilidade
    alt Veiculo NAO esta DISPONIVEL
        Viagem-->>Frontend: 400 Bad Request<br/>"Veiculo nao disponivel"
        Frontend-->>Operador: Exibe erro
    end

    Viagem->>CRepo: findById(cargaId)
    CRepo-->>Viagem: Carga

    Note over Viagem: Regra Leonardo #2<br/>Validar capacidade
    alt Peso da carga > capacidade
        Viagem-->>Frontend: 400 Bad Request<br/>"Peso excede capacidade"
        Frontend-->>Operador: Exibe erro
    end

    Viagem->>VgRepo: save(viagem)
    VgRepo->>DB: INSERT INTO viagens ...
    DB-->>VgRepo: Viagem persistida

    Viagem->>VRepo: save(veiculo com status=EM_VIAGEM)
    VRepo->>DB: UPDATE veiculos SET status='EM_VIAGEM'

    Viagem-->>Frontend: 200 OK + Viagem criada
    Frontend-->>Operador: Confirmacao + atualiza tela
```

## 3. Calcular frete (regras do Vinícius)

```mermaid
sequenceDiagram
    actor Operador
    participant Frontend as SPA
    participant Frete as FreteController
    participant Service as FreteService
    participant FRepo as FreteRepository
    participant DB as Banco

    Operador->>Frontend: Informa km, kg, tipo, pedagio, margem
    Frontend->>Frete: POST /api/fretes/simular

    Frete->>Service: calcularFrete(km, kg, tipo, pedagio, margem)

    Note over Service: Calculo (formula Vinicius)
    Service->>Service: combustivel = km * 2.50
    Service->>Service: manutencao = km * 0.80
    Service->>Service: custoPeso = kg * 0.12
    Service->>Service: custoVariavel = combustivel + manutencao + custoPeso + pedagio
    Service->>Service: multiplicador = getMultiplicadorTipo(tipo)
    Service->>Service: valorBase = (150 + custoVariavel) * multiplicador
    Service->>Service: valorFinal = valorBase * (1 + margem/100)

    Service-->>Frete: Frete (objeto em memoria, NAO salvo)
    Frete-->>Frontend: 200 OK + Frete simulado
    Frontend-->>Operador: Exibe valor final + breakdown

    opt Operador clica em "Calcular e Salvar"
        Frontend->>Frete: POST /api/fretes/calcular
        Frete->>Service: calcularESalvar(...)
        Service->>Service: calcularFrete(...)
        Service->>FRepo: save(frete)
        FRepo->>DB: INSERT INTO fretes ...
        DB-->>FRepo: Frete persistido
        FRepo-->>Service: Frete
        Service-->>Frete: Frete
        Frete-->>Frontend: 200 OK
        Frontend-->>Operador: Mensagem "salvo no historico"
    end
```

## Observações

- Os controllers seguem padrão REST: GET (listar/buscar), POST (criar), PUT/PATCH (atualizar), DELETE (remover).
- Toda chamada do frontend usa `fetch()` com headers JSON.
- O backend retorna sempre JSON; códigos HTTP padrão (200, 400, 404, 500).
- Persistência via Spring Data JPA (Hibernate) — não há SQL manual nos controllers.
