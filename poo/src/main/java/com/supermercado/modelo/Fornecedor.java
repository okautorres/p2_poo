package com.supermercado.modelo;

public class Fornecedor {
    private int idFornecedor;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;


    public Fornecedor(int idFornecedor, String nome, String cnpj, String email, String telefone, String endereco) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Fornecedor(int idFornecedor, String nome) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.cnpj = "";
        this.email = "";
        this.telefone = "";
        this.endereco = "";
    }

    public Fornecedor() {

    }



    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public void cadastrar() {
        System.out.println("Cadastrando fornecedor: " + this.nome);

    }

    public void atualizarDados() {
        System.out.println("Atualizando dados do fornecedor: " + this.nome);

    }

    @Override
    public String toString() {
        return "Fornecedor{" +
               "idFornecedor=" + idFornecedor +
               ", nome='" + nome + '\'' +
               ", cnpj='" + cnpj + '\'' +
               ", email='" + email + '\'' +
               ", telefone='" + telefone + '\'' +
               ", endereco='" + endereco + '\'' +
               '}';
    }
}

