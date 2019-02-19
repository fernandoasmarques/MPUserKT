package br.com.meupedidoapp.meupedidokt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public final class ItensPedido implements Parcelable {
    private Produto produto;
    private int quantidade;

    public ItensPedido(){}

    protected ItensPedido(Parcel in) {
        produto = in.readParcelable(Produto.class.getClassLoader());
        quantidade = in.readInt();
    }

    public static final Creator<ItensPedido> CREATOR = new Creator<ItensPedido>() {
        @Override
        public ItensPedido createFromParcel(Parcel in) {
            return new ItensPedido(in);
        }

        @Override
        public ItensPedido[] newArray(int size) {
            return new ItensPedido[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(produto, flags);
        dest.writeInt(quantidade);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getPrecoTotal() {
        return new BigDecimal(getQuantidade()).multiply(getProduto().getPrecoBD());
    }
}
