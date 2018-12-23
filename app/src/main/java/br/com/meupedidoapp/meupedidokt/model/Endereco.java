package br.com.meupedidoapp.meupedidokt.model;

import com.google.firebase.firestore.GeoPoint;

public final class Endereco {
    private String nomeLocal;
    private String logradouro;
    private boolean possuiNumero;
    private String numero;
    private String setor;
    private String complemento;
    private GeoPoint localizacao;
    private String referencia;
    private boolean enderecoAtivo;

    public Endereco(String nomeLocal, String logradouro, boolean possuiNumero, String numero, String setor, String complemento, GeoPoint localizacao, String referencia) {
        this.nomeLocal = nomeLocal;
        this.logradouro = logradouro;
        this.possuiNumero = possuiNumero;
        this.numero = numero;
        this.setor = setor;
        this.complemento = complemento;
        this.localizacao = localizacao;
        this.referencia = referencia;

        enderecoAtivo = true;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public boolean isPossuiNumero() {
        return possuiNumero;
    }

    public void setPossuiNumero(boolean possuiNumero) {
        this.possuiNumero = possuiNumero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public GeoPoint getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(GeoPoint localizacao) {
        this.localizacao = localizacao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public boolean isEnderecoAtivo() {
        return enderecoAtivo;
    }
}
