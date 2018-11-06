package br.com.meupedidoapp.meupedidokt.model;

public class Lojista extends Usuario {
    private String nomeFantasia;
    private String telefone3;
    //private Endereco endereco[];
    private boolean possuiLojaFisica;
    private String categoriaPrimaria;
    private float avaliacao;
    private int qtdeAvaliacao;
    private String imagemPerfil;
    private String marketingBanner;
    private boolean estaAberto;
    private Tema tema;
    private int tempoEntrega;
    private double taxaEntrega;

    public Lojista(){}

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public boolean isPossuiLojaFisica() {
        return possuiLojaFisica;
    }

    public String getCategoriaPrimaria() {
        return categoriaPrimaria;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public int getQtdeAvaliacao() {
        return qtdeAvaliacao;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public String getMarketingBanner() {
        return marketingBanner;
    }

    public boolean isEstaAberto() {
        return estaAberto;
    }

    public Tema getTema() {
        return tema;
    }

    public int getTempoEntrega() {
        return tempoEntrega;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }
}