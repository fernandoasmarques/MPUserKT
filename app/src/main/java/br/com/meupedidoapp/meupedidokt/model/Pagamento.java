package br.com.meupedidoapp.meupedidokt.model;

import java.math.BigDecimal;

public final class Pagamento {
    private String modalidadePagamento;
    private String tipoPagamento;
    private String descricao;
    private BigDecimal trocoParaValorDinheiro;

    public Pagamento(){}

    public Pagamento(FormaPagamento pagamento) {
        this.modalidadePagamento = pagamento.getModalidadePagamento();
        this.tipoPagamento = pagamento.getTipoPagamento();
        this.descricao = pagamento.getDescricao();
        this.trocoParaValorDinheiro = pagamento.getTrocoParaValorDinheiro();
    }

    public BigDecimal getTrocoParaValorDinheiro() {
        return trocoParaValorDinheiro;
    }

    public void setTrocoParaValorDinheiro(double trocoParaValorDinheiro) {
        this.trocoParaValorDinheiro = new BigDecimal(String.valueOf(trocoParaValorDinheiro));
    }

    public String getModalidadePagamento() {
        return modalidadePagamento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public String getDescricao() {
        return descricao;
    }
}
