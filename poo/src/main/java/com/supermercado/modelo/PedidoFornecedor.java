package com.supermercado.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoFornecedor {
    private int idPedido;
    private Fornecedor fornecedor; // Relacionamento com Fornecedor
    private Date dataPedido;
    private String status; // Ex: Pendente, Entregue, Cancelado
    private List<ItemPedidoFornecedor> itensPedido; // Relacionamento com ItemPedidoFornecedor


    public PedidoFornecedor(int idPedido, Fornecedor fornecedor, Date dataPedido, String status) {
        this.idPedido = idPedido;
        this.fornecedor = fornecedor;
        this.dataPedido = dataPedido;
        this.status = status;
        this.itensPedido = new ArrayList<>();
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemPedidoFornecedor> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedidoFornecedor> itensPedido) {
        this.itensPedido = itensPedido;
    }


    public void registrarPedido() {
        System.out.println("Registrando pedido ID: " + this.idPedido + " para o fornecedor: " + fornecedor.getNome());

    }

    public void adicionarItem(ItemPedidoFornecedor item) {
        if (item != null) {
            this.itensPedido.add(item);
            item.setPedidoFornecedor(this); // Estabelece a relação bidirecional
            System.out.println("Item adicionado ao pedido " + this.idPedido + ": " + item.getProduto().getNome());
        }
    }

    public void atualizarStatus(String novoStatus) {
        this.status = novoStatus;
        System.out.println("Status do pedido ID: " + this.idPedido + " atualizado para: " + novoStatus);
        if ("Entregue".equalsIgnoreCase(novoStatus)) {
            processarEntrega();
        }
    }

    private void processarEntrega() {
        System.out.println("Processando entrega do pedido ID: " + this.idPedido);
        Date dataEntrada = new Date(); // Usa a data atual como data de entrada
        for (ItemPedidoFornecedor item : itensPedido) {
            EntradaEstoque entrada = new EntradaEstoque(0,
                                                    item.getProduto(),
                                                    item.getQuantidade(),
                                                    dataEntrada,
                                                    item.getPrecoUnitario(),
                                                    this.fornecedor);
            entrada.registrarEntrada();
        }
    }

    public double calcularValorTotalPedido() {
        double total = 0.0;
        for (ItemPedidoFornecedor item : itensPedido) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "PedidoFornecedor{" +
               "idPedido=" + idPedido +
               ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : "null") +
               ", dataPedido=" + dataPedido +
               ", status=\'" + status + "\\'" +
               ", numeroItens=" + itensPedido.size() +
               ", valorTotal=" + calcularValorTotalPedido() +
               '}';
    }
}

