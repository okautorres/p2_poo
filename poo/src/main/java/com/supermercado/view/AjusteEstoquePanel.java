package com.supermercado.view;

import com.supermercado.dao.AjusteEstoqueDAO;
import com.supermercado.dao.ProdutoDAO;
import com.supermercado.modelo.AjusteEstoque;
import com.supermercado.modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class AjusteEstoquePanel extends JPanel {
    private JPanel painelPrincipal;
    private JComboBox<Produto> cmbProduto;
    private JTextField txtQuantidadeAjustada;
    private JTextField txtMotivo;
    private JButton btnRegistrarAjuste;
    private JTable tabelaAjustes;

    private AjusteEstoqueDAO ajusteEstoqueDAO;
    private ProdutoDAO produtoDAO;
    private DefaultTableModel tableModel;

    public AjusteEstoquePanel() {

        this.add(painelPrincipal);

        ajusteEstoqueDAO = new AjusteEstoqueDAO();
        produtoDAO = new ProdutoDAO();


        configurarTabela();


        carregarProdutos();
        atualizarTabelaAjustes();


        configurarComboBoxRenderer();

        btnRegistrarAjuste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarAjuste();
            }
        });
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(new Object[]{"ID Ajuste", "Data", "Produto", "Qtd. Ajustada", "Motivo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAjustes.setModel(tableModel);
    }

    private void configurarComboBoxRenderer() {
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
    }

    private void carregarProdutos() {
        cmbProduto.removeAllItems();
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto prod : produtos) {
            cmbProduto.addItem(prod);
        }
        cmbProduto.setSelectedIndex(-1);
    }

    private void atualizarTabelaAjustes() {
        tableModel.setRowCount(0);

        List<AjusteEstoque> ajustes = ajusteEstoqueDAO.listarTodos();
        for (AjusteEstoque a : ajustes) {
            Vector<Object> row = new Vector<>();
            row.add(a.getIdAjuste());
            row.add(a.getDataAjuste());
            row.add(a.getProduto() != null ? a.getProduto().getNome() : "N/A");
            row.add(a.getQuantidadeAjustada());
            row.add(a.getMotivo());
            tableModel.addRow(row);
        }
    }

    private void limparFormularioAjuste() {
        cmbProduto.setSelectedIndex(-1);
        txtQuantidadeAjustada.setText("");
        txtMotivo.setText("");
        cmbProduto.requestFocus();
    }

    private void registrarAjuste() {
        if (cmbProduto.getSelectedItem() == null || txtQuantidadeAjustada.getText().trim().isEmpty() || txtMotivo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Produto, Quantidade Ajustada e Motivo são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Produto produtoSelecionado = (Produto) cmbProduto.getSelectedItem();
            int quantidadeAjustada = Integer.parseInt(txtQuantidadeAjustada.getText().trim());
            String motivo = txtMotivo.getText().trim();
            Date dataAjuste = new Date();

            if (quantidadeAjustada == 0) {
                JOptionPane.showMessageDialog(this, "Quantidade ajustada não pode ser zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (produtoSelecionado.getEstoque() + quantidadeAjustada < 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Este ajuste deixará o estoque do produto '" + produtoSelecionado.getNome() + "' negativo. Deseja continuar?",
                        "Estoque Negativo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            AjusteEstoque novoAjuste = new AjusteEstoque(0, produtoSelecionado, quantidadeAjustada, motivo, dataAjuste);

            if (ajusteEstoqueDAO.inserir(novoAjuste)) {
                JOptionPane.showMessageDialog(this, "Ajuste de estoque registrado com sucesso! ID: " + novoAjuste.getIdAjuste(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormularioAjuste();
                atualizarTabelaAjustes();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar ajuste de estoque. Verifique os logs.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (Quantidade). Verifique o valor.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
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

        JFrame frame = new JFrame("Registro de Ajuste de Estoque (GUI Builder)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new AjusteEstoquePanel().painelPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}