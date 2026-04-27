-- ============================================================
-- BANCO DE DADOS: transportadora
-- Sistema de Gestao Logistica com Precificacao Inteligente
-- Projeto Integrador - FAM 2026
--
-- Modelagem original: Gabriela Andrade Brito (RA 20240868)
-- Refinamento e integracao: Pedro H. A. Marcandali (RA 20241595)
-- ============================================================

DROP DATABASE IF EXISTS transportadora;
CREATE DATABASE transportadora
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE transportadora;

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- TABELA: usuarios
-- Usuarios do sistema (autenticacao e controle de acesso)
-- ============================================================
CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'OPERADOR', 'CONSULTA') NOT NULL DEFAULT 'OPERADOR',
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    INDEX idx_usuario_email (email)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: clientes
-- Empresas/pessoas que contratam servicos de frete
-- ============================================================
CREATE TABLE clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(150),
    cidade VARCHAR(100),
    PRIMARY KEY (id),
    INDEX idx_cliente_cnpj (cnpj),
    INDEX idx_cliente_nome (nome)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: motoristas
-- Motoristas da transportadora
-- ============================================================
CREATE TABLE motoristas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cnh VARCHAR(20) NOT NULL UNIQUE,
    categoria_cnh ENUM('A', 'B', 'C', 'D', 'E'),
    validade_cnh DATE,
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    INDEX idx_motorista_cnh (cnh)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: veiculos
-- Frota de veiculos da transportadora
-- ============================================================
CREATE TABLE veiculos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(10) NOT NULL UNIQUE,
    modelo VARCHAR(50) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    ano_fabricacao INT,
    capacidade_kg DECIMAL(10,2) NOT NULL,
    tipo ENUM('CAMINHAO_PEQUENO', 'CAMINHAO_MEDIO', 'CAMINHAO_GRANDE', 'CARRETA', 'VAN'),
    status ENUM('DISPONIVEL', 'EM_VIAGEM', 'MANUTENCAO') NOT NULL DEFAULT 'DISPONIVEL',
    PRIMARY KEY (id),
    INDEX idx_veiculo_placa (placa),
    INDEX idx_veiculo_status (status)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: rotas
-- Trajetos cadastrados (origem -> destino)
-- ============================================================
CREATE TABLE rotas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    origem VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    distancia_km DECIMAL(10,2) NOT NULL,
    tempo_estimado_horas DECIMAL(5,2),
    pedagios VARCHAR(500),
    custo_pedagio DECIMAL(10,2) DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: cargas
-- Tipos de carga transportadas
-- ============================================================
CREATE TABLE cargas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(200) NOT NULL,
    peso_kg DECIMAL(10,2) NOT NULL,
    volume_m3 DECIMAL(10,2),
    tipo ENUM('GERAL', 'GRANEL', 'REFRIGERADA', 'PERIGOSA', 'FRAGIL', 'VIVA'),
    fragil BOOLEAN NOT NULL DEFAULT FALSE,
    perigosa BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: viagens
-- Operacoes de transporte (combina veiculo + rota + carga + motorista + cliente)
-- ============================================================
CREATE TABLE viagens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    veiculo_id BIGINT,
    rota_id BIGINT,
    carga_id BIGINT,
    motorista_id BIGINT,
    cliente_id BIGINT,
    data_partida DATETIME,
    data_chegada_prevista DATETIME,
    data_chegada_real DATETIME,
    status ENUM('PLANEJADA', 'EM_TRANSITO', 'ENTREGUE', 'CANCELADA') NOT NULL DEFAULT 'PLANEJADA',
    observacoes VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT fk_viagem_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE SET NULL,
    CONSTRAINT fk_viagem_rota FOREIGN KEY (rota_id) REFERENCES rotas(id) ON DELETE SET NULL,
    CONSTRAINT fk_viagem_carga FOREIGN KEY (carga_id) REFERENCES cargas(id) ON DELETE SET NULL,
    CONSTRAINT fk_viagem_motorista FOREIGN KEY (motorista_id) REFERENCES motoristas(id) ON DELETE SET NULL,
    CONSTRAINT fk_viagem_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL,
    INDEX idx_viagem_status (status),
    INDEX idx_viagem_data (data_partida)
) ENGINE=InnoDB;

-- ============================================================
-- TABELA: fretes
-- Calculos de precificacao de frete (regra de negocio principal)
-- ============================================================
CREATE TABLE fretes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    viagem_id BIGINT,
    distancia_km DECIMAL(10,2) NOT NULL,
    peso_kg DECIMAL(10,2) NOT NULL,
    tipo_carga VARCHAR(50),
    custo_combustivel DECIMAL(10,2),
    custo_manutencao DECIMAL(10,2),
    custo_pedagio DECIMAL(10,2),
    custo_fixo DECIMAL(10,2),
    custo_variavel DECIMAL(10,2),
    margem_lucro DECIMAL(5,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_frete_viagem FOREIGN KEY (viagem_id) REFERENCES viagens(id) ON DELETE SET NULL,
    INDEX idx_frete_data (data_criacao)
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- DADOS INICIAIS
-- ============================================================

-- Usuarios (senhas em BCrypt: admin123 e op123)
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Administrador', 'admin@transportadora.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('Operador', 'operador@transportadora.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERADOR');

-- Clientes
INSERT INTO clientes (nome, cnpj, telefone, email, cidade) VALUES
('Industria Sao Paulo Ltda', '12.345.678/0001-90', '(11) 3333-4444', 'contato@industriasp.com.br', 'Sao Paulo'),
('Comercio Campinas SA', '98.765.432/0001-10', '(19) 3232-5566', 'vendas@comerciocps.com.br', 'Campinas'),
('Distribuidora RJ', '55.444.333/0001-22', '(21) 2222-7788', 'logistica@distribrj.com.br', 'Rio de Janeiro');

-- Motoristas
INSERT INTO motoristas (nome, cnh, categoria_cnh, validade_cnh, telefone, ativo) VALUES
('Joao da Silva', '12345678900', 'E', '2028-06-15', '(19) 99999-1111', TRUE),
('Carlos Pereira', '98765432100', 'D', '2027-12-30', '(19) 98888-2222', TRUE),
('Ana Souza', '11122233344', 'C', '2029-03-10', '(19) 97777-3333', TRUE);

-- Veiculos
INSERT INTO veiculos (placa, modelo, marca, ano_fabricacao, capacidade_kg, tipo, status) VALUES
('ABC-1234', 'Actros 2651', 'Mercedes-Benz', 2023, 25000.00, 'CARRETA', 'DISPONIVEL'),
('DEF-5678', 'Delivery 11.180', 'Volkswagen', 2024, 6500.00, 'CAMINHAO_MEDIO', 'DISPONIVEL'),
('GHI-9012', 'Daily 35-150', 'Iveco', 2025, 3500.00, 'VAN', 'MANUTENCAO');

-- Rotas
INSERT INTO rotas (origem, destino, distancia_km, tempo_estimado_horas, custo_pedagio) VALUES
('Americana - SP', 'Campinas - SP', 45.00, 1.0, 15.80),
('Americana - SP', 'Sao Paulo - SP', 130.00, 2.5, 52.40),
('Campinas - SP', 'Rio de Janeiro - RJ', 500.00, 6.5, 120.00);

-- Cargas
INSERT INTO cargas (descricao, peso_kg, volume_m3, tipo, fragil, perigosa) VALUES
('Eletronicos diversos', 2500.00, 15.00, 'FRAGIL', TRUE, FALSE),
('Soja a granel', 20000.00, 30.00, 'GRANEL', FALSE, FALSE);

-- ============================================================
-- FIM DO SCRIPT
-- ============================================================
