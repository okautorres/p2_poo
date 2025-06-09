package com.supermercado.view;

import com.supermercado.dao.ProdutoDAO;
import com.supermercado.modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class EstoqueBaixoPanel extends JPanel {
    private JPanel painelPrincipal;
    private JTable tabelaEstoqueBaixo;
    private JButton btnAtualizar;

    private ProdutoDAO produtoDAO;
    private DefaultTableModel tableModel;

    public EstoqueBaixoPanel() {
        this.add(painelPrincipal);

        produtoDAO = new ProdutoDAO();

        configurarTabela();

        btnAtualizar.addActionListener(e -> atualizarTabelaEstoqueBaixo());

        // Carregar dados iniciais
        atualizarTabelaEstoqueBaixo();
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Estoque Atual", "Estoque Mínimo", "Fornecedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEstoqueBaixo.setModel(tableModel);
    }

    public void atualizarTabelaEstoqueBaixo() {
        tableModel.setRowCount(0);

        List<Produto> produtos = produtoDAO.listarEstoqueBaixo();
        if (produtos.isEmpty()) {
            Vector<Object> row = new Vector<>();
            row.add("-");
            row.add("Nenhum produto com estoque baixo no momento.");
            row.add("-");
            row.add("-");
            row.add("-");
            tableModel.addRow(row);
        } else {
            for (Produto p : produtos) {
                Vector<Object> row = new Vector<>();
                row.add(p.getIdProduto());
                row.add(p.getNome());
                row.add(p.getEstoque());
                row.add(p.getEstoqueMin());
                row.add(p.getFornecedor() != null ? p.getFornecedor().getNome() : "N/A");
                tableModel.addRow(row);
            }
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        JFrame frame = new JFrame("Alerta de Estoque Baixo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new EstoqueBaixoPanel().painelPrincipal);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}