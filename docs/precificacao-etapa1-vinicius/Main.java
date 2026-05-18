public class Main {
    public static void main(String[] args) {

        FreteService service = new FreteService();

        double valor = service.calcularFrete(
                300,     // distância km
                500,     // peso kg
                "fragil",
                50,      // pedágio
                20       // margem (%)
        );

        System.out.println("Valor do frete: R$ " + valor);
    }
}