package com.transportadora.logistica.config;

import com.transportadora.logistica.model.*;
import com.transportadora.logistica.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final VeiculoRepository veiculoRepo;
    private final RotaRepository rotaRepo;
    private final CargaRepository cargaRepo;
    private final ClienteRepository clienteRepo;
    private final MotoristaRepository motoristaRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepo, VeiculoRepository veiculoRepo,
                           RotaRepository rotaRepo, CargaRepository cargaRepo,
                           ClienteRepository clienteRepo, MotoristaRepository motoristaRepo,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.veiculoRepo = veiculoRepo;
        this.rotaRepo = rotaRepo;
        this.cargaRepo = cargaRepo;
        this.clienteRepo = clienteRepo;
        this.motoristaRepo = motoristaRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepo.count() == 0) {
            usuarioRepo.save(new Usuario("Administrador", "admin@transportadora.com",
                    passwordEncoder.encode("admin123"), Usuario.Perfil.ADMIN));
            usuarioRepo.save(new Usuario("Operador", "operador@transportadora.com",
                    passwordEncoder.encode("op123"), Usuario.Perfil.OPERADOR));

            Veiculo v1 = new Veiculo();
            v1.setPlaca("ABC-1234"); v1.setModelo("Actros 2651"); v1.setMarca("Mercedes-Benz");
            v1.setAnoFabricacao(2023); v1.setCapacidadeKg(25000);
            v1.setTipo(Veiculo.TipoVeiculo.CARRETA); v1.setStatus(Veiculo.StatusVeiculo.DISPONIVEL);
            veiculoRepo.save(v1);

            Veiculo v2 = new Veiculo();
            v2.setPlaca("DEF-5678"); v2.setModelo("Delivery 11.180"); v2.setMarca("Volkswagen");
            v2.setAnoFabricacao(2024); v2.setCapacidadeKg(6500);
            v2.setTipo(Veiculo.TipoVeiculo.CAMINHAO_MEDIO); v2.setStatus(Veiculo.StatusVeiculo.DISPONIVEL);
            veiculoRepo.save(v2);

            Veiculo v3 = new Veiculo();
            v3.setPlaca("GHI-9012"); v3.setModelo("Daily 35-150"); v3.setMarca("Iveco");
            v3.setAnoFabricacao(2025); v3.setCapacidadeKg(3500);
            v3.setTipo(Veiculo.TipoVeiculo.VAN); v3.setStatus(Veiculo.StatusVeiculo.MANUTENCAO);
            veiculoRepo.save(v3);

            Rota r1 = new Rota();
            r1.setOrigem("Americana - SP"); r1.setDestino("Campinas - SP");
            r1.setDistanciaKm(45); r1.setTempoEstimadoHoras(1.0); r1.setCustoPedagio(15.80);
            rotaRepo.save(r1);

            Rota r2 = new Rota();
            r2.setOrigem("Americana - SP"); r2.setDestino("Sao Paulo - SP");
            r2.setDistanciaKm(130); r2.setTempoEstimadoHoras(2.5); r2.setCustoPedagio(52.40);
            rotaRepo.save(r2);

            Rota r3 = new Rota();
            r3.setOrigem("Campinas - SP"); r3.setDestino("Rio de Janeiro - RJ");
            r3.setDistanciaKm(500); r3.setTempoEstimadoHoras(6.5); r3.setCustoPedagio(120.00);
            rotaRepo.save(r3);

            Carga c1 = new Carga();
            c1.setDescricao("Eletronicos diversos"); c1.setPesoKg(2500); c1.setVolumeM3(15);
            c1.setTipo(Carga.TipoCarga.FRAGIL); c1.setFragil(true);
            cargaRepo.save(c1);

            Carga c2 = new Carga();
            c2.setDescricao("Soja a granel"); c2.setPesoKg(20000); c2.setVolumeM3(30);
            c2.setTipo(Carga.TipoCarga.GRANEL);
            cargaRepo.save(c2);

            Cliente cl1 = new Cliente("Industria Sao Paulo Ltda", "(11) 3333-4444", "Sao Paulo");
            cl1.setEmail("contato@industriasp.com.br"); cl1.setCnpj("12.345.678/0001-90");
            clienteRepo.save(cl1);

            Cliente cl2 = new Cliente("Comercio Campinas SA", "(19) 3232-5566", "Campinas");
            cl2.setEmail("vendas@comerciocps.com.br"); cl2.setCnpj("98.765.432/0001-10");
            clienteRepo.save(cl2);

            Cliente cl3 = new Cliente("Distribuidora RJ", "(21) 2222-7788", "Rio de Janeiro");
            cl3.setEmail("logistica@distribrj.com.br"); cl3.setCnpj("55.444.333/0001-22");
            clienteRepo.save(cl3);

            Motorista m1 = new Motorista("Joao da Silva", "12345678900", "(19) 99999-1111");
            m1.setCategoriaCnh(Motorista.CategoriaCNH.E);
            m1.setValidadeCnh(LocalDate.of(2028, 6, 15));
            motoristaRepo.save(m1);

            Motorista m2 = new Motorista("Carlos Pereira", "98765432100", "(19) 98888-2222");
            m2.setCategoriaCnh(Motorista.CategoriaCNH.D);
            m2.setValidadeCnh(LocalDate.of(2027, 12, 30));
            motoristaRepo.save(m2);

            Motorista m3 = new Motorista("Ana Souza", "11122233344", "(19) 97777-3333");
            m3.setCategoriaCnh(Motorista.CategoriaCNH.C);
            m3.setValidadeCnh(LocalDate.of(2029, 3, 10));
            motoristaRepo.save(m3);

            System.out.println("=== Dados iniciais carregados com sucesso! ===");
        }
    }
}
