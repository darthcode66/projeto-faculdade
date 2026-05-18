public class FreteService {

    // Constantes de custo
    private static final double CUSTO_COMBUSTIVEL_KM = 2.50;
    private static final double CUSTO_MANUTENCAO_KM = 0.80;
    private static final double CUSTO_PESO_KG = 0.12;
    private static final double CUSTO_FIXO = 150.00;

    public double calcularFrete(double distanciaKm,
                                double pesoKg,
                                String tipoCarga,
                                double custoPedagio,
                                double margemLucro) {

        // Cálculo do custo variável por km
        double custoPorKm = CUSTO_COMBUSTIVEL_KM + CUSTO_MANUTENCAO_KM;

        // Cálculo do custo variável total
        double custoVariavel = (distanciaKm * custoPorKm)
                             + (pesoKg * CUSTO_PESO_KG)
                             + custoPedagio;

        // Custo base
        double custoBase = CUSTO_FIXO + custoVariavel;

        // Multiplicador por tipo de carga
        double multiplicador = obterMultiplicador(tipoCarga);

        // Aplicação do multiplicador
        double valorComTipo = custoBase * multiplicador;

        // Aplicação da margem de lucro
        double valorFinal = valorComTipo * (1 + (margemLucro / 100));

        return valorFinal;
    }

    private double obterMultiplicador(String tipoCarga) {
        switch (tipoCarga.toLowerCase()) {
            case "granel":
                return 0.9;
            case "geral":
                return 1.0;
            case "fragil":
                return 1.25;
            case "refrigerada":
                return 1.4;
            case "viva":
                return 1.5;
            case "perigosa":
                return 1.6;
            default:
                return 1.0;
        }
    }
}