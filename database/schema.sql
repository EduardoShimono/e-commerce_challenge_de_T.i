-- Arena Games - script de criacao do banco
-- Compativel com MySQL 8 e MariaDB 10 (XAMPP)

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS arena_games;
CREATE DATABASE arena_games CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE arena_games;

-- Usuarios (clientes e administradores)
CREATE TABLE usuarios (
    id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL,
    senha_hash  VARCHAR(255) NOT NULL,
    perfil      ENUM('cliente', 'admin') NOT NULL DEFAULT 'cliente',
    criado_em   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_usuarios_email UNIQUE (email)
) ENGINE=InnoDB;

-- Categorias do catalogo
CREATE TABLE categorias (
    id    INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(60) NOT NULL,
    CONSTRAINT uq_categorias_nome UNIQUE (nome)
) ENGINE=InnoDB;

-- Produtos
CREATE TABLE produtos (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    categoria_id  INT UNSIGNED NOT NULL,
    nome          VARCHAR(120) NOT NULL,
    descricao     TEXT,
    preco         DECIMAL(10,2) NOT NULL,
    estoque       INT UNSIGNED NOT NULL DEFAULT 0,
    imagem        VARCHAR(255),
    ativo         TINYINT(1) NOT NULL DEFAULT 1,
    criado_em     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_produtos_categoria FOREIGN KEY (categoria_id)
        REFERENCES categorias (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_produtos_preco CHECK (preco >= 0)
) ENGINE=InnoDB;

-- Pedidos (cabecalho da venda)
CREATE TABLE pedidos (
    id               INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    usuario_id       INT UNSIGNED NOT NULL,
    data_pedido      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    forma_pagamento  ENUM('PIX', 'BOLETO', 'CARTAO') NOT NULL,
    total            DECIMAL(10,2) NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PAGO',
    CONSTRAINT fk_pedidos_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Itens do pedido (resolve o N:N entre pedidos e produtos)
-- preco_unitario guarda o valor praticado no momento da compra
CREATE TABLE itens_pedido (
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    pedido_id       INT UNSIGNED NOT NULL,
    produto_id      INT UNSIGNED NOT NULL,
    quantidade      INT UNSIGNED NOT NULL,
    preco_unitario  DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_itens_pedido FOREIGN KEY (pedido_id)
        REFERENCES pedidos (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_itens_produto FOREIGN KEY (produto_id)
        REFERENCES produtos (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT uq_item_pedido_produto UNIQUE (pedido_id, produto_id),
    CONSTRAINT ck_itens_quantidade CHECK (quantidade > 0)
) ENGINE=InnoDB;

CREATE INDEX idx_produtos_nome ON produtos (nome);
CREATE INDEX idx_pedidos_data ON pedidos (data_pedido);

-- ------------------------------------------------------------
-- Carga inicial de teste
-- ------------------------------------------------------------

-- Senhas (BCrypt):
--   admin@arenagames.com   -> admin123
--   cliente@arenagames.com -> cliente123
INSERT INTO usuarios (nome, email, senha_hash, perfil) VALUES
('Administrador', 'admin@arenagames.com', '$2a$10$ZlPqn0DkA.igkGbvwP3vTe4cfP0rqvKOrN7DSRbr5n6PEbNzwdCoi', 'admin'),
('Cliente Teste', 'cliente@arenagames.com', '$2a$10$rxA3NpeCgRKsBh6/jgO3oujThz6vkTYbD72xhqXDf0PuNm.k9Cwfm', 'cliente');

INSERT INTO categorias (nome) VALUES
('Consoles'),
('Jogos'),
('Acessórios'),
('Periféricos PC');

INSERT INTO produtos (categoria_id, nome, descricao, preco, estoque, imagem) VALUES
(1, 'PlayStation 5 Slim', 'Console PlayStation 5 versão Slim com leitor de disco, SSD de 1 TB e um controle DualSense.', 3799.90, 8, 'img/produtos/ps5.svg'),
(1, 'Xbox Series S', 'Console Xbox Series S totalmente digital, 512 GB de armazenamento, ideal para Game Pass.', 2299.00, 12, 'img/produtos/xbox.svg'),
(1, 'Nintendo Switch OLED', 'Switch com tela OLED de 7 polegadas, 64 GB e base com porta LAN.', 2199.90, 5, 'img/produtos/switch.svg'),
(2, 'EA Sports FC 25 - PS5', 'Jogo de futebol com os principais clubes e ligas do mundo. Mídia física.', 299.90, 25, 'img/produtos/fc25.svg'),
(2, 'The Legend of Zelda: Tears of the Kingdom', 'Aventura em mundo aberto exclusiva para Nintendo Switch.', 349.00, 10, 'img/produtos/zelda.svg'),
(2, 'Forza Horizon 5 - Xbox', 'Corrida em mundo aberto ambientada no México. Código digital.', 199.90, 0, 'img/produtos/forza.svg'),
(3, 'Controle DualSense Branco', 'Controle sem fio para PS5 com feedback háptico e gatilhos adaptáveis.', 449.90, 15, 'img/produtos/dualsense.svg'),
(3, 'Headset Gamer HyperX Cloud II', 'Headset com som surround 7.1 virtual, microfone removível e almofadas em espuma.', 599.00, 7, 'img/produtos/headset.svg'),
(4, 'Mouse Gamer Logitech G502', 'Mouse com sensor HERO 25K, 11 botões programáveis e pesos ajustaveis.', 279.90, 20, 'img/produtos/mouse.svg'),
(4, 'Teclado Mecânico Redragon Kumara', 'Teclado mecânico ABNT2 com switch Outemu Blue e iluminação vermelha.', 219.90, 3, 'img/produtos/teclado.svg'),
(4, 'Monitor Gamer 24" 165Hz', 'Monitor IPS Full HD de 24 polegadas, 165 Hz, 1 ms e suporte a FreeSync.', 1099.00, 6, 'img/produtos/monitor.svg'),
(3, 'Cartão de Memória 256GB', 'Cartão microSD de 256 GB compatível com Nintendo Switch.', 159.90, 30, 'img/produtos/microsd.svg');
