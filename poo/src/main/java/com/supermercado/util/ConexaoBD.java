package com.supermercado.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoBD {


    private static final String URL = "jdbc:mysql://localhost:3306/p2_daniel_eunata_kauan";
    private static final String USER = "root";
    private static final String PASSWORD = "root@4599";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Connection conexao = null;


    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName(DRIVER);
                conexao = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            } catch (ClassNotFoundException e) {
                System.err.println("Erro: Driver JDBC do MySQL não encontrado.");
                throw new SQLException("Driver não encontrado", e);
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
                throw e;
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                if (!conexao.isClosed()) {
                    conexao.close();
                    conexao = null;
                    System.out.println("Conexão com o banco de dados fechada com sucesso.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = ConexaoBD.getConexao();
            if (conn != null) {
                System.out.println("Teste de conexão bem-sucedido!");
                ConexaoBD.fecharConexao();
            } else {
                System.err.println("Falha no teste de conexão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro durante o teste de conexão: " + e.getMessage());
        }
    }
}

