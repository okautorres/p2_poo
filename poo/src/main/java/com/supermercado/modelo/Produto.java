package com.supermercado.modelo;

import java.util.Date;

public class Produto {
    private int idProduto;
    private String nome;
    private String codigoBarra;
    private double precoVenda;
    private double precoCompra;
    private int estoque;
    private int estoqueMin;
    private Categoria categoria;
    private Fornecedor fornecedor;

    // Construtor
    public Produto(int idProduto, String nome, String codigoBarra, double precoVenda, double precoCompra,
                   int estoque, int estoqueMin, Categoria categoria, Fornecedor fornecedor) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.codigoBarra = codigoBarra;
        this.precoVenda = precoVenda;
        this.precoCompra = precoCompra;
        this.estoque = estoque;
        this.estoqueMin = estoqueMin;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
    }

    public Produto() {

    }

    public Produto(int idProduto, String nome, int estoque) {
    }

    public Produto(int idProduto, String nome, int estoque, double preco) {
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getEstoqueMin() {
        return estoqueMin;
    }

    public void setEstoqueMin(int estoqueMin) {
        this.estoqueMin = estoqueMin;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }


    public void cadastrar() {
        System.out.println("Cadastrando produto: " + this.nome);

    }

    public void atualizarEstoque(int quantidade) {
        this.estoque += quantidade;
        System.out.println("Estoque do produto " + this.nome + " atualizado para: " + this.estoque);
        if (verificarEstoqueMinimo()) {
            System.out.println("ALERTA: Estoque baixo para o produto " + this.nome);

        }
    }

    public boolean verificarEstoqueMinimo() {
        return this.estoque < this.estoqueMin;
    }

    public double calcularMargemLucro() {
        if (this.precoCompra > 0) {
            return ((this.precoVenda - this.precoCompra) / this.precoCompra) * 100;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Produto{" +
               "idProduto=" + idProduto +
               ", nome=\'" + nome + "\'" +
               ", codigoBarra=\'" + codigoBarra + "\'" +
               ", precoVenda=" + precoVenda +
               ", precoCompra=" + precoCompra +
               ", estoque=" + estoque +
               ", estoqueMin=" + estoqueMin +
               ", categoria=" + (categoria != null ? categoria.getNome() : "null") +
               ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : "null") +
               '}';
    }
}

