package com.supermercado.modelo;


public class ItemPedidoFornecedor {
    private int idItemPedido;
    private PedidoFornecedor pedidoFornecedor;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;


    public ItemPedidoFornecedor(int idItemPedido, PedidoFornecedor pedidoFornecedor, Produto produto, int quantidade, double precoUnitario) {
        this.idItemPedido = idItemPedido;
        this.pedidoFornecedor = pedidoFornecedor;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }


    public int getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(int idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public PedidoFornecedor getPedidoFornecedor() {
        return pedidoFornecedor;
    }


    public void setPedidoFornecedor(PedidoFornecedor pedidoFornecedor) {
        this.pedidoFornecedor = pedidoFornecedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }


    public double calcularSubtotal() {
        return this.quantidade * this.precoUnitario;
    }

    @Override
    public String toString() {
        return "ItemPedidoFornecedor{" +
               "idItemPedido=" + idItemPedido +
               ", pedidoId=" + (pedidoFornecedor != null ? pedidoFornecedor.getIdPedido() : "null") +
               ", produto=" + (produto != null ? produto.getNome() : "null") +
               ", quantidade=" + quantidade +
               ", precoUnitario=" + precoUnitario +
               ", subtotal=" + calcularSubtotal() +
               '}';
    }
}

