# Frontend - Etapa 1 (Diego Henrique C. Custodio)

**Autor:** Diego Henrique C. Custodio - RA 20240153
**Origem:** documento `Estrutura HTML.docx`
**Status:** artefato historico

## Conteudo

Esta pasta preserva os arquivos originais entregues pelo Diego na Etapa 1 do
Projeto Integrador, antes da consolidacao em SPA unica:

- `index.html` + `script.js` - tela inicial de cadastro de veiculo
- `precificacao.html` + `precificacao.js` - tela de simulacao de frete
- `style.css` - paleta de cores e identidade visual do sistema

## Evolucao para o sistema consolidado

| Etapa 1 (esta pasta) | Etapa 2 (`src/main/resources/static/`) |
|---|---|
| 2 paginas HTML separadas | SPA unica com 8 abas |
| `script.js` simula envio com `console.log` | `app.js` (793 linhas) integra com APIs REST |
| Formula simplificada: `(km*2 + kg*0.10) * fator` | Formula completa em `FreteService.java` |
| Paleta de cores (#27ae60 / #2c3e50 / #f4f7f6 / #3498db) | **Paleta preservada** no `:root` do `style.css` consolidado |

## Aproveitamento no produto final

A identidade visual definida pelo Diego (paleta de cores, estilo de botao
verde de acao, card de resultado azul com borda lateral) foi **integralmente
preservada** no SPA consolidado. Ver comentarios em
`src/main/resources/static/css/style.css`.
