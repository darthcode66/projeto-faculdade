/*
  Logica JavaScript - Etapa 1
  Autoria: Diego Henrique C. Custodio (RA 20240153)
  Origem: documento "Estrutura HTML.docx"
  Status: mockup - integracao real com API foi feita no app.js do SPA consolidado
*/
document.getElementById('veiculoForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const dadosVeiculo = {
        placa: document.getElementById('placa').value,
        modelo: document.getElementById('modelo').value,
        capacidade: document.getElementById('capacidade').value
    };

    console.log("Enviando para o Backend Spring Boot:", dadosVeiculo);

    const mensagemDiv = document.getElementById('mensagem');
    mensagemDiv.textContent = "Veiculo cadastrado com sucesso!";
    mensagemDiv.className = "success";

    this.reset();
});
