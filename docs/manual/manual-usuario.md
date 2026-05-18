# Manual do Usuário — TransLog

Sistema de Gestão Logística com Precificação Inteligente
Projeto Integrador — FAM 2026

---

## 1. Acesso ao sistema

1. Abra o navegador (Chrome, Firefox ou Edge — versão atual)
2. Acesse: `http://localhost:8080`
3. Você verá a tela de login

### Credenciais de demonstração

| Perfil | Email | Senha |
|---|---|---|
| Administrador | `admin@transportadora.com` | `admin123` |
| Operador | `operador@transportadora.com` | `op123` |

---

## 2. Visão geral da interface

Após o login, o sistema apresenta uma **barra lateral** com 8 áreas:

| Área | Função |
|---|---|
| **Dashboard** | Indicadores gerais (frota, viagens ativas, fretes do mês) |
| **Veículos** | Cadastro e gestão da frota |
| **Motoristas** | Cadastro de motoristas e CNHs |
| **Clientes** | Cadastro de clientes contratantes |
| **Rotas** | Trajetos cadastrados (origem → destino) |
| **Cargas** | Tipos de carga disponíveis para transporte |
| **Viagens** | Operações de transporte em andamento |
| **Cálculo de Frete** | Simulação e cálculo de fretes |
| **Histórico** | Fretes calculados e salvos |

---

## 3. Operações principais

### 3.1 Cadastrar um veículo

1. Clique em **Veículos** na barra lateral
2. Clique em **+ Novo Veículo**
3. Preencha:
   - **Placa** (formato `ABC-1234`)
   - **Marca e Modelo**
   - **Ano de Fabricação**
   - **Capacidade em kg**
   - **Tipo** (Caminhão Pequeno / Médio / Grande / Carreta / Van)
   - **Status** inicial (geralmente "Disponível")
4. Clique em **Salvar**

> ⚠️ A placa é única no sistema — não é possível cadastrar duas com a mesma placa.

### 3.2 Cadastrar uma rota

1. Clique em **Rotas** → **+ Nova Rota**
2. Preencha:
   - **Origem** (ex: "Americana - SP")
   - **Destino** (ex: "Campinas - SP")
   - **Distância em km**
   - **Tempo estimado em horas**
   - **Custo de pedágio**
3. Clique em **Salvar**

### 3.3 Criar uma viagem

**Pré-requisitos:** ter pelo menos 1 veículo `DISPONÍVEL`, 1 motorista ativo, 1 rota e 1 carga cadastrados.

1. Clique em **Viagens** → **+ Nova Viagem**
2. Selecione:
   - **Veículo** (somente os disponíveis aparecem)
   - **Rota**
   - **Carga**
   - **Motorista**
   - **Cliente** (opcional)
   - **Data de partida**
3. Clique em **Salvar**

> O sistema valida automaticamente:
> - Veículo precisa estar **DISPONÍVEL**
> - Peso da carga **não pode exceder** a capacidade do veículo
>
> Caso uma dessas regras seja violada, será exibida uma mensagem explicativa e a viagem não será criada.

Após criar a viagem:
- Status inicial: `PLANEJADA`
- Status do veículo muda automaticamente para `EM_VIAGEM`

### 3.4 Atualizar status da viagem

1. Na tela **Viagens**, localize a viagem e clique no menu de status
2. Selecione o novo status:
   - `PLANEJADA` → `EM_TRANSITO`: viagem iniciou
   - `EM_TRANSITO` → `ENTREGUE`: viagem concluída (veículo volta a `DISPONÍVEL` automaticamente)
   - Qualquer estado → `CANCELADA`: cancela a viagem (veículo volta a `DISPONÍVEL`)

### 3.5 Calcular um frete

1. Clique em **Cálculo de Frete**
2. Preencha:
   - **Distância** (km)
   - **Peso** (kg)
   - **Tipo de carga** (Geral / Frágil / Refrigerada / Perigosa / Viva / Granel)
   - **Custo de pedágio**
   - **Margem de lucro** (em %)
3. Clique em **Simular** para ver o valor sem salvar
4. Ou clique em **Calcular e Salvar** para registrar no histórico

### Como o valor é calculado

```
Custo Variável = (km × R$ 2,50) + (km × R$ 0,80) + (kg × R$ 0,12) + pedágio
Custo Fixo     = R$ 150,00
Valor Base     = (Custo Fixo + Custo Variável) × Multiplicador do Tipo
Valor Final    = Valor Base × (1 + Margem / 100)
```

**Multiplicadores por tipo:**

| Tipo | Multiplicador |
|---|---|
| Granel | 0,9x |
| Geral | 1,0x |
| Frágil | 1,25x |
| Refrigerada | 1,4x |
| Viva | 1,5x |
| Perigosa | 1,6x |

---

## 4. Dashboard

A tela inicial mostra indicadores em tempo real:

- **Total de veículos** (e quantos estão disponíveis)
- **Viagens ativas** (status `EM_TRANSITO`)
- **Fretes calculados no mês**
- **Receita total estimada** (soma dos valores dos fretes)

---

## 5. Histórico

Lista todos os fretes calculados e salvos, com:
- Data do cálculo
- Distância e peso
- Tipo de carga
- Custos discriminados
- Valor final

É possível **deletar** registros antigos pelo botão na linha correspondente.

---

## 6. Perguntas frequentes

**P: Esqueci a senha do admin. Como recupero?**
R: Em ambiente de demonstração, basta apagar o arquivo `data/logistica_db.mv.db` e reiniciar o sistema. O `DataInitializer` recria as credenciais padrão.

**P: O sistema aceita múltiplos usuários simultâneos?**
R: Sim, é uma aplicação web multi-usuário. Cada usuário pode estar logado em sua própria sessão.

**P: Posso usar em produção?**
R: Para produção, troque o H2 pelo MySQL (script `transportadora_completo.sql`) ajustando o `application.properties`, e gere builds com `./mvnw package`.

**P: A placa do veículo precisa ter formato específico?**
R: O sistema aceita qualquer texto, mas recomendamos o padrão brasileiro `ABC-1234` ou Mercosul `ABC1D23` para consistência.

**P: O CNPJ e o telefone precisam ser digitados com pontuação?**
R: Não. O sistema aplica máscara automática enquanto você digita.
- **CNPJ:** digite só números. Será formatado como `00.000.000/0000-00`.
- **Telefone:** digite só números. Será formatado como `(00) 00000-0000` (celular) ou `(00) 0000-0000` (fixo).

A máscara também é aplicada na edição de cadastros antigos, padronizando registros que tenham sido inseridos sem formatação.

---

## 7. Suporte

Em caso de problemas, consulte os logs em `logs/` ou contate a equipe de desenvolvimento via repositório:
`github.com/darthcode66/projeto-faculdade`
