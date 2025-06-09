package com.supermercado.dao;

import com.supermercado.modelo.EntradaEstoque;
import com.supermercado.modelo.Fornecedor;
import com.supermercado.modelo.Produto;
import com.supermercado.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EntradaEstoqueDAO {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public boolean inserir(EntradaEstoque entrada) {
        String sql = "INSERT INTO EntradaEstoque (id_produto, quantidade, data_entrada, preco_unitario, id_fornecedor) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;

        try {
            conn = ConexaoBD.getConexao();

            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, entrada.getProduto().getIdProduto());
            stmt.setInt(2, entrada.getQuantidade());
            stmt.setTimestamp(3, new Timestamp(entrada.getDataEntrada().getTime()));
            stmt.setDouble(4, entrada.getPrecoUnitario());
            stmt.setInt(5, entrada.getFornecedor().getIdFornecedor());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entrada.setIdEntrada(rs.getInt(1));
                    }
                }


                Produto produto = entrada.getProduto();
                int novoEstoque = produto.getEstoque() + entrada.getQuantidade();

                if (produtoDAO.atualizarEstoque(produto.getIdProduto(), novoEstoque, conn)) {
                    conn.commit();
                    sucesso = true;
                    produto.setEstoque(novoEstoque);
                } else {
                    conn.rollback();
                    System.err.println("Erro ao atualizar estoque do produto ID: " + produto.getIdProduto() + ". Rollback realizado.");
                }
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir entrada de estoque: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao realizar rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
        return sucesso;
    }


    public List<EntradaEstoque> listarTodos() {
        String sql = "SELECT e.id_entrada, e.quantidade, e.data_entrada, e.preco_unitario, " +
                "p.id_produto, p.nome AS nome_produto, p.estoque, " +
                "f.id_fornecedor, f.nome AS nome_fornecedor " +
                "FROM EntradaEstoque e " +
                "JOIN Produtos p ON e.id_produto = p.id_produto " +
                "JOIN Fornecedor f ON e.id_fornecedor = f.id_fornecedor " +
                "ORDER BY e.data_entrada DESC";

        List<EntradaEstoque> entradas = new ArrayList<>();

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome_produto"));
                produto.setEstoque(rs.getInt("estoque"));

                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setNome(rs.getString("nome_fornecedor"));

                EntradaEstoque entrada = new EntradaEstoque(
                        rs.getInt("id_entrada"),
                        produto,
                        rs.getInt("quantidade"),
                        rs.getTimestamp("data_entrada"),
                        rs.getDouble("preco_unitario"),
                        fornecedor
                );
                entradas.add(entrada);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar entradas de estoque: " + e.getMessage());
        }
        return entradas;
    }

    private EntradaEstoque mapResultSetToEntradaEstoque(ResultSet rs) throws SQLException {
        int idProduto = rs.getInt("id_produto");
        int idFornecedor = rs.getInt("id_fornecedor");

        Produto produto = produtoDAO.buscarPorId(idProduto);
        Fornecedor fornecedor = fornecedorDAO.buscarPorId(idFornecedor);

        if (produto == null) {
            System.err.println("Aviso: Produto com ID " + idProduto + " não encontrado para a entrada ID " + rs.getInt("id_entrada"));
            return null;
        }
        if (fornecedor == null) {
            System.err.println("Aviso: Fornecedor com ID " + idFornecedor + " não encontrado para a entrada ID " + rs.getInt("id_entrada"));
            return null;
        }

        return new EntradaEstoque(
            rs.getInt("id_entrada"),
            produto,
            rs.getInt("quantidade"),
            rs.getTimestamp("data_entrada"),
            rs.getDouble("preco_unitario"),
            fornecedor
        );
    }


}

