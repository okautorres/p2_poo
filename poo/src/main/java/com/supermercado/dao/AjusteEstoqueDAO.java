package com.supermercado.dao;

import com.supermercado.modelo.AjusteEstoque;
import com.supermercado.modelo.Produto;
import com.supermercado.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AjusteEstoqueDAO {

    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public boolean inserir(AjusteEstoque ajuste) {
        String sql = "INSERT INTO AjusteEstoque (id_produto, quantidade_ajustada, motivo, data_ajuste) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean sucesso = false;

        try {
            conn = ConexaoBD.getConexao();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, ajuste.getProduto().getIdProduto());
            stmt.setInt(2, ajuste.getQuantidadeAjustada());
            stmt.setString(3, ajuste.getMotivo());
            stmt.setTimestamp(4, new Timestamp(ajuste.getDataAjuste().getTime()));

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ajuste.setIdAjuste(rs.getInt(1));
                    }
                }

                // Atualiza o estoque do produto
                Produto produto = ajuste.getProduto();
                int novoEstoque = produto.getEstoque() + ajuste.getQuantidadeAjustada();

                // No AjusteEstoqueDAO, trocar para:
                if (produtoDAO.atualizarEstoque(conn, produto.getIdProduto(), novoEstoque)) {
                    conn.commit();
                    sucesso = true;
                    produto.setEstoque(novoEstoque);
                    System.out.println("Ajuste registrado e estoque atualizado para produto ID: " + produto.getIdProduto());
                } else {
                    conn.rollback();
                    System.err.println("Erro ao atualizar estoque do produto ID: " + produto.getIdProduto() + " durante ajuste. Rollback realizado.");
                }
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir ajuste de estoque: " + e.getMessage());
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


    public List<AjusteEstoque> listarTodos() {
        String sql = "SELECT a.id_ajuste, a.quantidade_ajustada, a.motivo, a.data_ajuste, " +
                "p.id_produto, p.nome, p.estoque " +
                "FROM AjusteEstoque a " +
                    "JOIN Produtos p ON a.id_produto = p.id_produto " +
                "ORDER BY a.data_ajuste DESC";

        List<AjusteEstoque> ajustes = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome"),
                        rs.getInt("estoque")
                );


                AjusteEstoque ajuste = new AjusteEstoque(
                        rs.getInt("id_ajuste"),
                        produto,
                        rs.getInt("quantidade_ajustada"),
                        rs.getString("motivo"),
                        rs.getTimestamp("data_ajuste")
                );

                ajustes.add(ajuste);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar ajustes de estoque: " + e.getMessage());
        }
        return ajustes;
    }

    private AjusteEstoque mapResultSetToAjusteEstoque(ResultSet rs) throws SQLException {
        int idProduto = rs.getInt("id_produto");
        Produto produto = produtoDAO.buscarPorId(idProduto);

        if (produto == null) {
            System.err.println("Aviso: Produto com ID " + idProduto + " não encontrado para o ajuste ID " + rs.getInt("id_ajuste"));
            return null; // Ou tratar como preferir
        }

        return new AjusteEstoque(
            rs.getInt("id_ajuste"),
            produto,
            rs.getInt("quantidade_ajustada"),
            rs.getString("motivo"),
            rs.getTimestamp("data_ajuste")
        );
    }

}

