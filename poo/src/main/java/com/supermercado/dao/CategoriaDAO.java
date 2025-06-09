package com.supermercado.dao;

import com.supermercado.modelo.Categoria;
import com.supermercado.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean inserir(Categoria categoria) {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                // Recupera o ID gerado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setIdCategoria(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Categoria categoria) {
        String sql = "UPDATE Categoria SET nome = ? WHERE id_categoria = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                 System.err.println("Erro ao excluir categoria: Categoria está associada a produtos.");
            } else {
                System.err.println("Erro ao excluir categoria: " + e.getMessage());
            }
            return false;
        }
    }

    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM Categoria WHERE id_categoria = ?";
        Categoria categoria = null;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria por ID: " + e.getMessage());
        }
        return categoria;
    }


    public List<Categoria> listarTodos() {
        String sql = "SELECT * FROM Categoria ORDER BY nome";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nome")
                );
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }
}

