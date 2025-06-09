package com.supermercado.modelo;

public class Categoria {
    private int idCategoria;
    private String nome;


    public Categoria(int idCategoria, String nome) {
        this.idCategoria = idCategoria;
        this.nome = nome;
    }


    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public void cadastrar() {
        System.out.println("Cadastrando categoria: " + this.nome);

    }

    public void listarProdutos() {
        System.out.println("Listando produtos da categoria: " + this.nome);

    }

    @Override
    public String toString() {
        return "Categoria{" +
               "idCategoria=" + idCategoria +
               ", nome='" + nome + '\'' +
               '}';
    }
}

