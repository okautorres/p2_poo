package com.supermercado.modelo;

import java.util.Date;

public class EntradaEstoque {
    private int idEntrada;
    private Produto produto;
    private int quantidade;
    private Date dataEntrada;
    private double precoUnitario;
    private Fornecedor fornecedor;


    public EntradaEstoque(int idEntrada, Produto produto, int quantidade, Date dataEntrada, double precoUnitario, Fornecedor fornecedor) {
        this.idEntrada = idEntrada;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataEntrada = dataEntrada;
        this.precoUnitario = precoUnitario;
        this.fornecedor = fornecedor;
    }


    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
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

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }


    public void registrarEntrada() {
        System.out.println("Registrando entrada de " + quantidade + " unidades do produto: " + produto.getNome());

        if (this.produto != null) {
            this.produto.atualizarEstoque(this.quantidade);

        }
    }

    @Override
    public String toString() {
        return "EntradaEstoque{" +
               "idEntrada=" + idEntrada +
               ", produto=" + (produto != null ? produto.getNome() : "null") +
               ", quantidade=" + quantidade +
               ", dataEntrada=" + dataEntrada +
               ", precoUnitario=" + precoUnitario +
               ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : "null") +
               '}';
    }
}

