package com.supermercado.modelo;

import java.util.Date;


public class AjusteEstoque {
    private int idAjuste;
    private Produto produto;
    private int quantidadeAjustada;
    private String motivo;
    private Date dataAjuste;


    public AjusteEstoque(int idAjuste, Produto produto, int quantidadeAjustada, String motivo, Date dataAjuste) {
        this.idAjuste = idAjuste;
        this.produto = produto;
        this.quantidadeAjustada = quantidadeAjustada;
        this.motivo = motivo;
        this.dataAjuste = dataAjuste;
    }


    public int getIdAjuste() {
        return idAjuste;
    }

    public void setIdAjuste(int idAjuste) {
        this.idAjuste = idAjuste;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadeAjustada() {
        return quantidadeAjustada;
    }

    public void setQuantidadeAjustada(int quantidadeAjustada) {
        this.quantidadeAjustada = quantidadeAjustada;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getDataAjuste() {
        return dataAjuste;
    }

    public void setDataAjuste(Date dataAjuste) {
        this.dataAjuste = dataAjuste;
    }


    public void registrarAjuste() {
        System.out.println("Registrando ajuste de estoque para o produto: " + produto.getNome() +
                           ", Quantidade: " + quantidadeAjustada + ", Motivo: " + motivo);
        if (this.produto != null) {
            this.produto.atualizarEstoque(this.quantidadeAjustada);
        }
    }

    @Override
    public String toString() {
        return "AjusteEstoque{" +
               "idAjuste=" + idAjuste +
               ", produto=" + (produto != null ? produto.getNome() : "null") +
               ", quantidadeAjustada=" + quantidadeAjustada +
               ", motivo=\'" + motivo + "\\'" +
               ", dataAjuste=" + dataAjuste +
               '}';
    }
}

