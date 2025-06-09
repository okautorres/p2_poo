package com.supermercado.view;

import com.supermercado.dao.EntradaEstoqueDAO;
import com.supermercado.dao.FornecedorDAO;
import com.supermercado.dao.ProdutoDAO;
import com.supermercado.modelo.EntradaEstoque;
import com.supermercado.modelo.Fornecedor;
import com.supermercado.modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class EntradaEstoquePanel extends JPanel {
    private JPanel painelPrincipal;
    private JComboBox<Produto> cmbProduto;
    private JComboBox<Fornecedor> cmbFornecedor;
    private JTextField txtQuantidade;
    private JTextField txtPrecoUnitario;
    private JButton btnRegistrarEntrada;
    private JTable tabelaEntradas;

    private EntradaEstoqueDAO entradaEstoqueDAO;
    private ProdutoDAO produtoDAO;
    private FornecedorDAO fornecedorDAO;
    private DefaultTableModel tableModel;

    public EntradaEstoquePanel() {
        this.add(painelPrincipal);

        entradaEstoqueDAO = new EntradaEstoqueDAO();
        produtoDAO = new ProdutoDAO();
        fornecedorDAO = new FornecedorDAO();

        configurarTabela();
        configurarComboBoxRenderers();

        carregarProdutos();
        carregarFornecedores();
        atualizarTabelaEntradas();

        btnRegistrarEntrada.addActionListener(e -> registrarEntrada());
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(new Object[]{"ID Entrada", "Data", "Produto", "Fornecedor", "Quantidade", "Preço Unit."}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEntradas.setModel(tableModel);
    }

    private void configurarComboBoxRenderers() {
        cmbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    setText(((Produto) value).getNome() + " (ID: " + ((Produto) value).getIdProduto() + ")");
                }
                return this;
            }
        });

        cmbFornecedor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Fornecedor) {
                    setText(((Fornecedor) value).getNome());
                }
                return this;
            }
        });
    }

    private void carregarProdutos() {
        cmbProduto.removeAllItems();
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto prod : produtos) {
            cmbProduto.addItem(prod);
        }
        cmbProduto.setSelectedIndex(-1);
    }

    private void carregarFornecedores() {
        cmbFornecedor.removeAllItems();
        List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();
        for (Fornecedor forn : fornecedores) {
            cmbFornecedor.addItem(forn);
        }
        cmbFornecedor.setSelectedIndex(-1);
    }

    private void atualizarTabelaEntradas() {
        tableModel.setRowCount(0);
        List<EntradaEstoque> entradas = entradaEstoqueDAO.listarTodos();
        for (EntradaEstoque e : entradas) {
            Vector<Object> row = new Vector<>();
            row.add(e.getIdEntrada());
            row.add(e.getDataEntrada());
            row.add(e.getProduto() != null ? e.getProduto().getNome() : "N/A");
            row.add(e.getFornecedor() != null ? e.getFornecedor().getNome() : "N/A");
            row.add(e.getQuantidade());
            row.add(String.format("%.2f", e.getPrecoUnitario()));
            tableModel.addRow(row);
        }
    }

    private void limparFormularioEntrada() {
        cmbProduto.setSelectedIndex(-1);
        cmbFornecedor.setSelectedIndex(-1);
        txtQuantidade.setText("");
        txtPrecoUnitario.setText("");
        cmbProduto.requestFocus();
    }

    private void registrarEntrada() {
        if (cmbProduto.getSelectedItem() == null || cmbFornecedor.getSelectedItem() == null || txtQuantidade.getText().trim().isEmpty() || txtPrecoUnitario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Produto, Fornecedor, Quantidade e Preço Unitário são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Produto produtoSelecionado = (Produto) cmbProduto.getSelectedItem();
            Fornecedor fornecedorSelecionado = (Fornecedor) cmbFornecedor.getSelectedItem();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            double precoUnitario = Double.parseDouble(txtPrecoUnitario.getText().trim().replace(",", "."));
            Date dataEntrada = new Date();

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (precoUnitario < 0) {
                JOptionPane.showMessageDialog(this, "Preço unitário não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EntradaEstoque novaEntrada = new EntradaEstoque(0, produtoSelecionado, quantidade, dataEntrada, precoUnitario, fornecedorSelecionado);

            if (entradaEstoqueDAO.inserir(novaEntrada)) {
                JOptionPane.showMessageDialog(this, "Entrada de estoque registrada com sucesso! ID: " + novaEntrada.getIdEntrada(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormularioEntrada();
                atualizarTabelaEntradas();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar entrada de estoque. Verifique os logs.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (Quantidade, Preço). Verifique os valores.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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

        JFrame frame = new JFrame("Registro de Entrada de Estoque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new EntradaEstoquePanel().painelPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}