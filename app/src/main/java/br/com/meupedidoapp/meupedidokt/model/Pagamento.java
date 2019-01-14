package br.com.meupedidoapp.meupedidokt.model;

import java.math.BigDecimal;

public class Pagamento {
    private String modalidadePagamento;
    private String tipoPagamento;
    private String descricao;
    private BigDecimal trocoParaValorDinheiro;

    public Pagamento(String modalidadePagamento, String tipoPagamento, String descricao, BigDecimal trocoParaValorDinheiro) {
        this.modalidadePagamento = modalidadePagamento;
        this.tipoPagamento = tipoPagamento;
        this.descricao = descricao;
        this.trocoParaValorDinheiro = trocoParaValorDinheiro;
    }

    public String getModalidadePagamento() {
        return modalidadePagamento;
    }

    public void setModalidadePagamento(String modalidadePagamento) {
        this.modalidadePagamento = modalidadePagamento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getTrocoParaValorDinheiro() {
        return trocoParaValorDinheiro;
    }

    public void setTrocoParaValorDinheiro(BigDecimal trocoParaValorDinheiro) {
        this.trocoParaValorDinheiro = trocoParaValorDinheiro;
    }

    public void setTrocoParaValorDinheiro(double trocoParaValorDinheiro) {
        this.trocoParaValorDinheiro = new BigDecimal(String.valueOf(trocoParaValorDinheiro));
    }
}
