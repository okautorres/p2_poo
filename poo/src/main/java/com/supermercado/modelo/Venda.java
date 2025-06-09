package com.supermercado.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venda {
    private int idVenda;
    private Date dataVenda;
    private double valorTotal;
    private String formaPagamento;
    private Cliente cliente;
    private List<ItemVenda> itensVenda;


    public Venda(int idVenda, Date dataVenda, String formaPagamento, Cliente cliente) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.formaPagamento = formaPagamento;
        this.cliente = cliente;
        this.itensVenda = new ArrayList<>();
        this.valorTotal = 0.0;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorTotal() {
        return calcularValorTotal();
    }


    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(List<ItemVenda> itensVenda) {
        this.itensVenda = itensVenda;
        calcularValorTotal();
    }


    public void registrarVenda() {
        System.out.println("Registrando venda ID: " + this.idVenda);

        for (ItemVenda item : itensVenda) {
            if (item.getProduto() != null) {

                item.getProduto().atualizarEstoque(-item.getQuantidade());
            }
        }
    }

    public void adicionarItem(ItemVenda item) {
        if (item != null) {
            this.itensVenda.add(item);
            item.setVenda(this);
            System.out.println("Item adicionado à venda " + this.idVenda + ": " + item.getProduto().getNome());
            calcularValorTotal();
        }
    }

    public double calcularValorTotal() {
        this.valorTotal = 0.0;
        for (ItemVenda item : this.itensVenda) {
            this.valorTotal += item.calcularSubtotal();
        }
        return this.valorTotal;
    }

    public void finalizarVenda() {
        System.out.println("Finalizando venda ID: " + this.idVenda + ", Valor Total: " + getValorTotal());
        registrarVenda();
    }

    @Override
    public String toString() {
        return "Venda{" +
               "idVenda=" + idVenda +
               ", dataVenda=" + dataVenda +
               ", valorTotal=" + getValorTotal() +
               ", formaPagamento=\'" + formaPagamento + "\'" +
               ", cliente=" + (cliente != null ? cliente.getNome() : "N/A") +
               ", numeroItens=" + itensVenda.size() +
               '}';
    }
}

