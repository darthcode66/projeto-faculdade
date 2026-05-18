/*
  Logica de Precificacao - Etapa 1
  Autoria: Diego Henrique C. Custodio (RA 20240153)
  Origem: documento "Estrutura HTML.docx"
  Status: formula simplificada - evoluida em FreteService.java (modulo Vinicius)
*/
document.getElementById('formPrecificacao').addEventListener('submit', function (e) {
    e.preventDefault();

    const distancia = parseFloat(document.getElementById('distancia').value);
    const peso = parseFloat(document.getElementById('peso').value);
    const fatorCarga = parseFloat(document.getElementById('tipoCarga').value);

    // Custo base: R$ 2,00 por km + R$ 0,10 por kg
    const custoBase = (distancia * 2.00) + (peso * 0.10);
    const total = custoBase * fatorCarga;

    const area = document.getElementById('resultadoArea');
    const display = document.getElementById('valorFinal');
    display.textContent = `R$ ${total.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
    area.classList.remove('hidden');

    console.log("Simulacao de Frete Concluida:", { distancia, peso, total });
});
