package com.supermercado.modelo;

public class ItemVenda {
    private int idItemVenda;
    private Venda venda; // Relacionamento com Venda
    private Produto produto; // Relacionamento com Produto
    private int quantidade;
    private double precoUnitario; // Preço do produto no momento da venda


    public ItemVenda(int idItemVenda, Venda venda, Produto produto, int quantidade, double precoUnitario) {
        this.idItemVenda = idItemVenda;
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;

        this.precoUnitario = precoUnitario; 
    }


    public int getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(int idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public Venda getVenda() {
        return venda;
    }


    public void setVenda(Venda venda) {
        this.venda = venda;
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
        return "ItemVenda{" +
               "idItemVenda=" + idItemVenda +
               ", vendaId=" + (venda != null ? venda.getIdVenda() : "null") +
               ", produto=" + (produto != null ? produto.getNome() : "null") +
               ", quantidade=" + quantidade +
               ", precoUnitario=" + precoUnitario +
               ", subtotal=" + calcularSubtotal() +
               '}';
    }
}

