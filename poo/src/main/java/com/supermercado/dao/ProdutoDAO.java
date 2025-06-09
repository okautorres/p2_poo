package com.supermercado.dao;

import com.supermercado.modelo.Categoria;
import com.supermercado.modelo.Fornecedor;
import com.supermercado.modelo.Produto;
import com.supermercado.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO Produtos (nome, codigoBarra, precoVenda, precoCompra, id_categoria, id_fornecedor, estoque, estoqueMin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigoBarra());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setDouble(4, produto.getPrecoCompra());
            stmt.setInt(5, produto.getCategoria().getIdCategoria());
            stmt.setInt(6, produto.getFornecedor().getIdFornecedor());
            stmt.setInt(7, produto.getEstoque());
            stmt.setInt(8, produto.getEstoqueMin());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                // Recupera o ID gerado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setIdProduto(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
            return false;
        }
    }


    public boolean atualizar(Produto produto) {
        String sql = "UPDATE Produtos SET nome = ?, codigoBarra = ?, precoVenda = ?, precoCompra = ?, id_categoria = ?, id_fornecedor = ?, estoque = ?, estoqueMin = ? WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigoBarra());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setDouble(4, produto.getPrecoCompra());
            stmt.setInt(5, produto.getCategoria().getIdCategoria());
            stmt.setInt(6, produto.getFornecedor().getIdFornecedor());
            stmt.setInt(7, produto.getEstoque());
            stmt.setInt(8, produto.getEstoqueMin());
            stmt.setInt(9, produto.getIdProduto());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

     public boolean atualizarEstoque(int idProduto, int novoEstoque, Connection conn) throws SQLException {
         String sql = "UPDATE Produtos SET estoque = ? WHERE id_produto = ?";
         try (PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, novoEstoque);
             stmt.setInt(2, idProduto);
             return stmt.executeUpdate() > 0;
         }
     }

    public boolean atualizarEstoque(Connection conn, int idProduto, int novoEstoque) throws SQLException {
        String sql = "UPDATE Produtos SET estoque = ? WHERE id_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, idProduto);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM Produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {

            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro ao excluir produto: Produto está associado a vendas, entradas, ajustes ou pedidos.");
            } else {
                System.err.println("Erro ao excluir produto: " + e.getMessage());
            }
            return false;
        }
    }


    public Produto buscarPorId(int idProduto) {
        String sql = "SELECT * FROM produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                            rs.getInt("id_produto"),
                            rs.getString("nome"),
                            rs.getInt("estoque")
                            // demais campos
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        }
        return null;
    }

    public Produto buscarPorCodigoBarra(String codigoBarra) {
        String sql = "SELECT * FROM produtos WHERE codigoBarra = ?";
        Produto produto = null;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoBarra);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = mapResultSetToProduto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por código de barras: " + e.getMessage());
        }
        return produto;
    }


    public List<Produto> listarTodos() {
        String sql = "SELECT p.id_produto, p.nome, p.codigoBarra, p.precoVenda, p.precoCompra, p.estoque, p.estoqueMin, " +
                "c.id_categoria, c.nome AS categoria_nome, " +
                "f.id_fornecedor, f.nome AS fornecedor_nome " +
                "FROM produtos p " +
                "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                "LEFT JOIN Fornecedor f ON p.id_fornecedor = f.id_fornecedor " +
                "ORDER BY p.nome";

        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("categoria_nome"));
                Fornecedor fornecedor = new Fornecedor(rs.getInt("id_fornecedor"), rs.getString("fornecedor_nome"));


                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome"),
                        rs.getString("codigoBarra"),
                        rs.getDouble("precoVenda"),
                        rs.getDouble("precoCompra"),
                        rs.getInt("estoque"),
                        rs.getInt("estoqueMin"),
                        categoria,
                        fornecedor
                );

                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

     public List<Produto> listarEstoqueBaixo() {
         String sql = "SELECT p.id_produto, p.nome, p.codigoBarra, p.precoVenda, p.precoCompra, p.estoque, p.estoqueMin, " +
                 "c.id_categoria, c.nome AS categoria_nome, " +
                 "f.id_fornecedor, f.nome AS fornecedor_nome " +
                 "FROM produtos p " +
                 "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                 "LEFT JOIN Fornecedor f ON p.id_fornecedor = f.id_fornecedor " +
                 "WHERE p.estoque < p.estoqueMin " +
                 "ORDER BY p.nome";

         List<Produto> produtos = new ArrayList<>();
         try (Connection conn = ConexaoBD.getConexao();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

             while (rs.next()) {
                 Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("categoria_nome"));
                 Fornecedor fornecedor = new Fornecedor(rs.getInt("id_fornecedor"), rs.getString("fornecedor_nome"));

                 Produto produto = new Produto(
                         rs.getInt("id_produto"),
                         rs.getString("nome"),
                         rs.getString("codigoBarra"),
                         rs.getDouble("precoVenda"),
                         rs.getDouble("precoCompra"),
                         rs.getInt("estoque"),
                         rs.getInt("estoqueMin"),
                         categoria,
                         fornecedor
                 );

                 produtos.add(produto);
             }
         } catch (SQLException e) {
             System.err.println("Erro ao listar produtos com estoque baixo: " + e.getMessage());
         }
         return produtos;
     }


    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        int idCategoria = rs.getInt("id_categoria");
        int idFornecedor = rs.getInt("id_fornecedor");


        Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
        Fornecedor fornecedor = fornecedorDAO.buscarPorId(idFornecedor);


        if (categoria == null) {
             System.err.println("Aviso: Categoria com ID " + idCategoria + " não encontrada para o produto ID " + rs.getInt("id_produto"));
        }
         if (fornecedor == null) {
             System.err.println("Aviso: Fornecedor com ID " + idFornecedor + " não encontrado para o produto ID " + rs.getInt("id_produto"));
        }

        return new Produto(
            rs.getInt("id_produto"),
            rs.getString("nome"),
            rs.getString("codigoBarra"),
            rs.getDouble("precoVenda"),
            rs.getDouble("precoCompra"),
            rs.getInt("estoque"),
            rs.getInt("estoqueMin"),
            categoria,
            fornecedor
        );
    }
}

