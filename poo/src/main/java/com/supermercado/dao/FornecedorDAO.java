package com.supermercado.dao;

import com.supermercado.modelo.Fornecedor;
import com.supermercado.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class FornecedorDAO {


    public boolean inserir(Fornecedor fornecedor) {
        String sql = "INSERT INTO Fornecedor (nome, cnpj, email, telefone, endereco) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getEmail());
            stmt.setString(4, fornecedor.getTelefone());
            stmt.setString(5, fornecedor.getEndereco());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        fornecedor.setIdFornecedor(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir fornecedor: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Fornecedor fornecedor) {
        String sql = "UPDATE Fornecedor SET nome = ?, cnpj = ?, email = ?, telefone = ?, endereco = ? WHERE id_fornecedor = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getEmail());
            stmt.setString(4, fornecedor.getTelefone());
            stmt.setString(5, fornecedor.getEndereco());
            stmt.setInt(6, fornecedor.getIdFornecedor());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar fornecedor: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM Fornecedor WHERE id_fornecedor = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro ao excluir fornecedor: Fornecedor está associado a produtos, entradas ou pedidos.");
            } else {
                System.err.println("Erro ao excluir fornecedor: " + e.getMessage());
            }
            return false;
        }
    }

    public Fornecedor buscarPorId(int id) {
        String sql = "SELECT * FROM Fornecedor WHERE id_fornecedor = ?";
        Fornecedor fornecedor = null;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fornecedor = new Fornecedor(
                        rs.getInt("id_fornecedor"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedor por ID: " + e.getMessage());
        }
        return fornecedor;
    }


    public List<Fornecedor> listarTodos() {
        String sql = "SELECT * FROM Fornecedor ORDER BY nome";
        List<Fornecedor> fornecedores = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor(
                    rs.getInt("id_fornecedor"),
                    rs.getString("nome"),
                    rs.getString("cnpj"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("endereco")
                );
                fornecedores.add(fornecedor);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar fornecedores: " + e.getMessage());
        }
        return fornecedores;
    }
}

