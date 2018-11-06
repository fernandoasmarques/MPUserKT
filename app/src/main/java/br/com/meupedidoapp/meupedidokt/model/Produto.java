package br.com.meupedidoapp.meupedidokt.model;

import java.util.List;

public class Produto {
    private String imagem;
    private String nome;
    private List<String> ingredientes;
    private String tipo;
    private double preco;

    public Produto(){}

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
        return preco;
    }
}
