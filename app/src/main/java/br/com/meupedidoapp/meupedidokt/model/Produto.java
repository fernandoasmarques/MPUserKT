package br.com.meupedidoapp.meupedidokt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public final class Produto implements Parcelable {
    private String imagem;
    private String nome;
    private List<String> ingredientes;
    private String tipo;
    private BigDecimal preco;

    public Produto(){}

    protected Produto(Parcel in){
        imagem = in.readString();
        nome = in.readString();
        ingredientes = in.createStringArrayList();
        tipo = in.readString();
        preco = new BigDecimal(in.readString());
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagem);
        dest.writeString(nome);
        dest.writeStringList(ingredientes);
        dest.writeString(tipo);
        dest.writeString(preco.toString());
    }

    public String getImagem() {
        return imagem;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPreco() {
        return preco.doubleValue();
    }

    public BigDecimal getPrecoBD() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = new BigDecimal(String.valueOf(preco));
    }
}
