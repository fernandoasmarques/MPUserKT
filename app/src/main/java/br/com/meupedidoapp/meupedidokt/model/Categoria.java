package br.com.meupedidoapp.meupedidokt.model;

public final class Categoria {
    private String id;
    private String imagem;
    private String nome;
    private int ordem;

    public Categoria() {
    }

    public String getImagem() {
        return imagem;
    }

    public String getNome() {
        return nome;
    }

    public int getOrdem() {
        return ordem;
    }

    public String getId() {
        return id;
    }
}