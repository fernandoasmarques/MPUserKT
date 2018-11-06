package br.com.meupedidoapp.meupedidokt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class ItensPedido implements Parcelable {
    private String idProduto;
    private String nome;
    private int quantidade;
    private double preco;

    public ItensPedido() {}

    protected ItensPedido(Parcel in) {
        idProduto = in.readString();
        nome = in.readString();
        quantidade = in.readInt();
        preco = in.readDouble();
    }

    public static final Creator<ItensPedido> CREATOR = new Creator<ItensPedido>(){
        @Override
        public ItensPedido createFromParcel(Parcel in) {
            return new ItensPedido(in);
        }

        @Override
        public ItensPedido[] newArray(int size) {
            return new ItensPedido[size];
        }
    };

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto){
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPrecoTotal(){
        return new BigDecimal(getQuantidade()).multiply(new BigDecimal(getPreco())).doubleValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idProduto);
        dest.writeString(nome);
        dest.writeInt(quantidade);
        dest.writeDouble(preco);
    }
}