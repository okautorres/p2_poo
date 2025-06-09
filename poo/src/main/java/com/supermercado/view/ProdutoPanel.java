package com.supermercado.view;

import com.supermercado.dao.CategoriaDAO;
import com.supermercado.dao.FornecedorDAO;
import com.supermercado.dao.ProdutoDAO;
import com.supermercado.modelo.Categoria;
import com.supermercado.modelo.Fornecedor;
import com.supermercado.modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class ProdutoPanel extends JPanel {
    private JPanel painelPrincipal;
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtCodigoBarra;
    private JTextField txtPrecoVenda;
    private JTextField txtPrecoCompra;
    private JTextField txtEstoque;
    private JTextField txtEstoqueMin;
    private JComboBox<Categoria> cmbCategoria;
    private JComboBox<Fornecedor> cmbFornecedor;
    private JTable tabelaProdutos;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton ajusteEstoqueButton;
    private JButton entradaEstoqueButton;
    private JButton estoqueBaixoButton;

    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;
    private FornecedorDAO fornecedorDAO;
    private DefaultTableModel tableModel;

    public ProdutoPanel() {
        this.add(painelPrincipal);

        produtoDAO = new ProdutoDAO();
        categoriaDAO = new CategoriaDAO();
        fornecedorDAO = new FornecedorDAO();

        configurarTabela();
        configurarRenderers();
        configurarListeners();

        carregarCategorias();
        carregarFornecedores();
        atualizarTabelaProdutos();

        ajusteEstoqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame janelaAjuste = new JFrame("Ajuste de Estoque");


                AjusteEstoquePanel painelAjuste = new AjusteEstoquePanel();


                janelaAjuste.add(painelAjuste);


                janelaAjuste.setSize(800, 600);
                janelaAjuste.setLocationRelativeTo(null);

                janelaAjuste.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                janelaAjuste.setVisible(true);
            }
        });
        entradaEstoqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame janelaEntrada = new JFrame("Entrada de Estoque");
                EntradaEstoquePanel painelEntrada = new EntradaEstoquePanel();
                janelaEntrada.add(painelEntrada);
                janelaEntrada.setSize(800, 600);
                janelaEntrada.setLocationRelativeTo(null);
                janelaEntrada.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                janelaEntrada.setVisible(true);
            }
        });
        estoqueBaixoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame janelaEstoqueBaixo = new JFrame("Alerta de Estoque Baixo");
                EstoqueBaixoPanel painelEstoqueBaixo = new EstoqueBaixoPanel();
                janelaEstoqueBaixo.add(painelEstoqueBaixo);
                janelaEstoqueBaixo.setSize(800, 600);
                janelaEstoqueBaixo.setLocationRelativeTo(null);
                janelaEstoqueBaixo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                janelaEstoqueBaixo.setVisible(true);
            }
        });
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Cód. Barras", "Preço Venda", "Estoque", "Est. Mín", "Categoria", "Fornecedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos.setModel(tableModel);
    }

    private void configurarRenderers() {
        cmbCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Categoria) {
                    setText(((Categoria) value).getNome());
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

    private void configurarListeners() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());

        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaProdutos.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });
    }

    private void carregarCategorias() {
        cmbCategoria.removeAllItems();
        List<Categoria> categorias = categoriaDAO.listarTodos();
        for (Categoria cat : categorias) {
            cmbCategoria.addItem(cat);
        }
    }

    private void carregarFornecedores() {
        cmbFornecedor.removeAllItems();
        List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();
        for (Fornecedor forn : fornecedores) {
            cmbFornecedor.addItem(forn);
        }
    }

    private void atualizarTabelaProdutos() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            Vector<Object> row = new Vector<>();
            row.add(p.getIdProduto());
            row.add(p.getNome());
            row.add(p.getCodigoBarra());
            row.add(String.format("%.2f", p.getPrecoVenda()));
            row.add(p.getEstoque());
            row.add(p.getEstoqueMin());
            row.add(p.getCategoria() != null ? p.getCategoria().getNome() : "N/A");
            row.add(p.getFornecedor() != null ? p.getFornecedor().getNome() : "N/A");
            tableModel.addRow(row);
        }
    }

    private void preencherFormularioComLinhaSelecionada() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada != -1) {
            int idProduto = (int) tableModel.getValueAt(linhaSelecionada, 0);
            Produto produto = produtoDAO.buscarPorCodigoBarra(tableModel.getValueAt(linhaSelecionada, 2).toString()); // Usando código de barras para pegar todos os dados.

            if (produto != null) {
                txtId.setText(String.valueOf(produto.getIdProduto()));
                txtNome.setText(produto.getNome());
                txtCodigoBarra.setText(produto.getCodigoBarra());
                txtPrecoVenda.setText(String.format("%.2f", produto.getPrecoVenda()).replace(",", "."));
                txtPrecoCompra.setText(String.format("%.2f", produto.getPrecoCompra()).replace(",", "."));
                txtEstoque.setText(String.valueOf(produto.getEstoque()));
                txtEstoqueMin.setText(String.valueOf(produto.getEstoqueMin()));

                selecionarItemComboBox(cmbCategoria, produto.getCategoria());
                selecionarItemComboBox(cmbFornecedor, produto.getFornecedor());
            }
        }
    }

    private <T> void selecionarItemComboBox(JComboBox<T> comboBox, T itemParaSelecionar) {
        if (itemParaSelecionar == null) {
            comboBox.setSelectedIndex(-1);
            return;
        }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            T item = comboBox.getItemAt(i);
            if (item instanceof Categoria && itemParaSelecionar instanceof Categoria) {
                if (((Categoria) item).getIdCategoria() == ((Categoria) itemParaSelecionar).getIdCategoria()) {
                    comboBox.setSelectedIndex(i);
                    return;
                }
            } else if (item instanceof Fornecedor && itemParaSelecionar instanceof Fornecedor) {
                if (((Fornecedor) item).getIdFornecedor() == ((Fornecedor) itemParaSelecionar).getIdFornecedor()) {
                    comboBox.setSelectedIndex(i);
                    return;
                }
            }
        }
        comboBox.setSelectedIndex(-1);
    }

    private void limparFormulario() {
        txtId.setText("");
        txtNome.setText("");
        txtCodigoBarra.setText("");
        txtPrecoVenda.setText("");
        txtPrecoCompra.setText("");
        txtEstoque.setText("");
        txtEstoqueMin.setText("");
        cmbCategoria.setSelectedIndex(-1);
        cmbFornecedor.setSelectedIndex(-1);
        tabelaProdutos.clearSelection();
        txtNome.requestFocus();
    }

    private void salvarProduto() {
        if (txtNome.getText().trim().isEmpty() || cmbCategoria.getSelectedItem() == null || cmbFornecedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Nome, Categoria e Fornecedor são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nome = txtNome.getText().trim();
            String codigoBarra = txtCodigoBarra.getText().trim();
            double precoVenda = Double.parseDouble(txtPrecoVenda.getText().replace(",", "."));
            double precoCompra = Double.parseDouble(txtPrecoCompra.getText().replace(",", "."));
            int estoque = Integer.parseInt(txtEstoque.getText());
            int estoqueMin = Integer.parseInt(txtEstoqueMin.getText());
            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
            Fornecedor fornecedor = (Fornecedor) cmbFornecedor.getSelectedItem();

            Produto produto;
            boolean isUpdate = !txtId.getText().isEmpty();

            if (isUpdate) {
                int id = Integer.parseInt(txtId.getText());
                produto = new Produto(id, nome, codigoBarra, precoVenda, precoCompra, estoque, estoqueMin, categoria, fornecedor);
                if (produtoDAO.atualizar(produto)) {
                    JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                produto = new Produto(0, nome, codigoBarra, precoVenda, precoCompra, estoque, estoqueMin, categoria, fornecedor);
                if (produtoDAO.inserir(produto)) {
                    JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso! ID: " + produto.getIdProduto(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

            limparFormulario();
            atualizarTabelaProdutos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (Preço, Estoque). Verifique os valores.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void excluirProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProduto = (int) tableModel.getValueAt(linhaSelecionada, 0);
        String nomeProduto = (String) tableModel.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o produto: " + nomeProduto + " (ID: " + idProduto + ")?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (produtoDAO.excluir(idProduto)) {
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                atualizarTabelaProdutos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto. Verifique se ele não está sendo usado em vendas, etc.", "Erro", JOptionPane.ERROR_MESSAGE);
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

        JFrame frame = new JFrame("Gerenciamento de Produtos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ProdutoPanel().painelPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}