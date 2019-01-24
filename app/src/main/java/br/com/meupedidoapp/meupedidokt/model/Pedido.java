package br.com.meupedidoapp.meupedidokt.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;

public final class Pedido {
    private String uidCliente;
    private String uidLojista;
    private Fatura fatura;
    private ArrayList<ItensPedido> itensPedidos;
    private Status status;
    private Timestamp dataHora;
    private Endereco enderecoEntrega;
    private int qtdeTotal;
    private boolean finalizado;

    public Pedido(){
        Calendar c = Calendar.getInstance();
        dataHora = new Timestamp(c.getTime());
    }

    public String getUidCliente() {
        return uidCliente;
    }

    public void setUidCliente(String uidCliente) {
        this.uidCliente = uidCliente;
    }

    public String getUidLojista() {
        return uidLojista;
    }

    public void setUidLojista(String uidLojista) {
        this.uidLojista = uidLojista;
    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public ArrayList<ItensPedido> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(ArrayList<ItensPedido> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public int getQtdeTotal() {
        return qtdeTotal;
    }

    public void setQtdeTotal(int qtdeTotal) {
        this.qtdeTotal = qtdeTotal;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
}
