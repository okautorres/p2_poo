#### P2 - Laboratório de banco de dados
### Grupo: Daniel Fabio Santos Coelho Silva
#          Eunata Vinicius Oliveira Ferpa
#		   Kauan Torres

### SCRIPTS DDL ========================================================
CREATE DATABASE p2_daniel_eunata_kauan;
use p2_daniel_eunata_kauan;

CREATE TABLE Fornecedor (
  id_fornecedor INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(75),
  cnpj VARCHAR(20),
  email VARCHAR(75),
  telefone VARCHAR(15),
  endereco VARCHAR(200)
);

CREATE TABLE Categoria (
  id_categoria INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(75)
);

CREATE TABLE Produtos (
id_produto INT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(75),
codigoBarra VARCHAR(200),
precoVenda DECIMAL(8,2),
precoCompra DECIMAL(8,2),
id_categoria INT,
id_fornecedor INT NOT NULL,
estoque INT,
estoqueMin INT,
FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria),
FOREIGN KEY (id_fornecedor) REFERENCES Fornecedor(id_fornecedor)
);

CREATE TABLE EntradaEstoque (
  id_entrada INT PRIMARY KEY AUTO_INCREMENT,
  id_produto INT NOT NULL,
  quantidade INT,
  data_entrada DATETIME,
  preco_unitario DECIMAL(8,2),
  id_fornecedor INT NOT NULL,
  FOREIGN KEY (id_produto) REFERENCES Produtos(id_produto),
  FOREIGN KEY (id_fornecedor) REFERENCES Fornecedor(id_fornecedor)
);

CREATE TABLE AjusteEstoque (
  id_ajuste INT PRIMARY KEY AUTO_INCREMENT,
  id_produto INT NOT NULL,
  quantidade_ajustada INT,
  motivo VARCHAR(200),
  data_ajuste DATETIME,
  FOREIGN KEY (id_produto) REFERENCES Produtos(id_produto)
);

CREATE TABLE Cliente (
  id_cliente INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(75),
  cpf VARCHAR(14),
  telefone VARCHAR(15),
  email VARCHAR(75)
);

CREATE TABLE Venda (
  id_venda INT PRIMARY KEY AUTO_INCREMENT,
  data_venda DATETIME,
  valor_total DECIMAL(12,2),
  forma_pagamento VARCHAR(50),
  id_cliente INT,
  FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);
CREATE TABLE ItemVenda (
  id_item_venda INT PRIMARY KEY AUTO_INCREMENT,
  id_venda INT NOT NULL,
  id_produto INT NOT NULL,
  quantidade INT,
  preco_unitario DECIMAL(8,2),
  FOREIGN KEY (id_venda) REFERENCES Venda(id_venda),
  FOREIGN KEY (id_produto) REFERENCES Produtos(id_produto)
);

CREATE TABLE PedidoFornecedor (
  id_pedido INT PRIMARY KEY AUTO_INCREMENT,
  id_fornecedor INT NOT NULL,
  data_pedido DATETIME,
  status VARCHAR(45),
  FOREIGN KEY (id_fornecedor) REFERENCES Fornecedor(id_fornecedor)
);

CREATE TABLE ItemPedidoFornecedor (
  id_item_pedido INT PRIMARY KEY AUTO_INCREMENT,
  id_pedido INT NOT NULL,
  id_produto INT NOT NULL,
  quantidade INT,
  preco_unitario DECIMAL(8,2),
  FOREIGN KEY (id_pedido) REFERENCES PedidoFornecedor(id_pedido),
  FOREIGN KEY (id_produto) REFERENCES Produtos(id_produto)
);

### SCRIPTS DML ========================================================
# Alguns inserts para as views
INSERT INTO Categoria (nome) VALUES ('Bebidas'), ('Alimentos'), ('Higiene'), ('Limpeza');

INSERT INTO Fornecedor (nome, cnpj, email, telefone, endereco) VALUES
('KSCOMERCIAL LTDA.', '34.123.654/2345-21', 'vendas@kscomercial.com.br', '11912336256', 'Avenida Inconfidencia Mineira, 230'),
('KATUCHA DISTRIBUIÇÕES', '34.212.654/3145-09', 'katucha@gmail.com', '11988336286', 'Rua Paes de Barros, 1532');

INSERT INTO Produtos (nome, codigoBarra, precoVenda, precoCompra, id_categoria, id_fornecedor, estoque, estoqueMin) VALUES
('Listerine enxaguante', '7894900011517', 8.99, 6.50, 3, 1, 50, 10),
('Arroz 5Kg', '7891234567890', 21.90, 18.00, 2, 2, 80, 20),
('Bala halls', '7894561237891', 2.50, 1.20, 2, 1, 30, 10),
('Detergente Ypê', '7891029384756', 1.99, 1.00, 4, 2, 40, 15),
('Miojo da monica', '7899999999999', 1.50, 1.00, 2, 1, 100, 30),
('Lâmina de barbear', '7898888888888', 7.90, 6.00, 3, 2, 60, 15);

INSERT INTO Cliente (nome, cpf, telefone, email) VALUES
('Matheus Almeida Pamonha', '234.645.123-33', '11995929178', 'matheuszinholindao@gmail.com.br'),
('Marcela Alameda Santos', '321.142.321-43', '11983586837', 'marcsantos@hotmail.com'),
('Fabão Santos', '443.180.532-21', '11971277375', 'fabinkiller@yahoo.com.br');

INSERT INTO Venda (data_venda, valor_total, forma_pagamento, id_cliente) VALUES
(NOW(), 30.89, 'Cartão de Crédito', 1),
(NOW(), 24.40, 'Dinheiro', 2),
(NOW(), 50.00, 'Pix', 3);

INSERT INTO ItemVenda (id_venda, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 2, 8.99),
(1, 3, 1, 2.50),
(2, 2, 1, 21.90),
(2, 4, 1, 1.99),
(3, 5, 10, 1.50),
(3, 6, 2, 7.90);

INSERT INTO EntradaEstoque (id_produto, quantidade, data_entrada, preco_unitario, id_fornecedor) VALUES
(1, 30, '2024-05-10', 6.50, 1),
(2, 50, '2024-05-09', 18.00, 2),
(5, 100, '2024-05-08', 1.00, 1),
(6, 80, '2024-05-07', 6.00, 2);

INSERT INTO AjusteEstoque (id_produto, quantidade_ajustada, motivo, data_ajuste) VALUES
(3, -5, 'Produto rasgado', '2024-05-11'),
(4, 10, 'Reposição manual', '2024-05-11'),
(5, -15, 'Amassado', '2024-05-12');

INSERT INTO PedidoFornecedor (id_fornecedor, data_pedido, status) VALUES
(1, '2024-05-01', 'Entregue'),
(2, '2024-05-02', 'Pendente'),
(1, '2024-05-03', 'Entregue');

INSERT INTO ItemPedidoFornecedor (id_pedido, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 20, 6.40),
(1, 3, 50, 1.15),
(2, 2, 30, 17.80),
(3, 5, 60, 0.95);

# Alguns updates
UPDATE Fornecedor
SET email = 'katuchaNOVO@katucha.com.br'
WHERE id_fornecedor = 2;

UPDATE Produtos
SET precoVenda = 2.29
WHERE nome = 'Bala Halls';

UPDATE Produtos
SET estoque = 27
WHERE id_produto = 5;

UPDATE PedidoFornecedor
SET status = 'Entregue'
WHERE id_fornecedor = 2 AND status = 'Pendente';

#Alguns deletes


DELETE FROM AjusteEstoque
WHERE data_ajuste = '2024-05-11';


DELETE FROM ItemPedidoFornecedor
WHERE id_pedido = 3;

### CONSULTAS DQL (VIEWS)=============================================================================

CREATE OR REPLACE VIEW vw_vendas_mensais AS
SELECT
  YEAR(data_venda) AS ano,
  MONTH(data_venda) AS mes,
  forma_pagamento,
  COUNT(*) AS total_vendas,
  SUM(valor_total) AS total_recebido
FROM Venda
GROUP BY ano, mes, forma_pagamento;

SELECT * FROM vw_vendas_mensais WHERE mes = MONTH(CURDATE()) AND ano = YEAR(CURDATE());

CREATE OR REPLACE VIEW vw_produto_mais_vendido AS
SELECT
  p.id_produto AS id,
  p.nome AS nome,
  p.precoVenda AS preco,
  p.estoque AS estoque,
  SUM(iv.quantidade) AS total_vendas,
  SUM(iv.quantidade * iv.preco_unitario) AS valor_total
FROM ItemVenda iv
JOIN Produtos p ON p.id_produto = iv.id_produto
GROUP BY p.id_produto, p.nome, p.precoVenda, p.estoque
ORDER BY total_vendas DESC;

SELECT * FROM vw_produto_mais_vendido;

CREATE OR REPLACE VIEW vw_estoque_baixo AS
SELECT
  p.id_produto AS id,
  p.nome AS nome,
  p.precoVenda AS preco,
  p.estoque AS estoque,
  p.estoqueMin AS estoqueMin
FROM Produtos p WHERE p.estoque < estoqueMin
GROUP BY p.id_produto ORDER BY estoqueMin ASC;

SELECT * FROM vw_estoque_baixo;

CREATE OR REPLACE VIEW vw_margem_lucro AS
SELECT 
    nome,
    precoVenda,
    precoCompra,
    (precoVenda - precoCompra) AS lucro_unitario,
    ROUND(((precoVenda - precoCompra) / precoCompra) * 100, 2) AS margem_percentual
FROM Produtos
ORDER BY margem_percentual DESC;

SELECT * FROM vw_margem_lucro;

CREATE OR REPLACE VIEW vw_fluxo_caixa AS
SELECT 
  data,
  tipo_movimentacao,
  forma_pagamento,
  SUM(valor) AS total_valor
FROM ( ##Precisei juntar as duas
  SELECT
    DATE(data_venda) AS data,
    'Entrada' AS tipo_movimentacao,
    forma_pagamento,
    SUM(valor_total) AS valor
  FROM Venda
  GROUP BY DATE(data_venda), forma_pagamento
  UNION ALL ## Unindo
  SELECT
    DATE(pf.data_pedido) AS data,
    'Saída' AS tipo_movimentacao,
    'Pedido ao fornecedor' AS forma_pagamento, 
    SUM(ipf.quantidade * ipf.preco_unitario) AS valor
  FROM PedidoFornecedor pf
  JOIN ItemPedidoFornecedor ipf ON pf.id_pedido = ipf.id_pedido
  GROUP BY DATE(pf.data_pedido)
) AS fluxo
GROUP BY data, tipo_movimentacao, forma_pagamento
ORDER BY data DESC, tipo_movimentacao;

SELECT * FROM vw_fluxo_caixa;



  
  

    