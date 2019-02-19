package br.com.meupedidoapp.meupedidokt.model;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;

public final class Pagamento {
    private String modalidadePagamento;
    private String tipoPagamento;
    private BigDecimal trocoParaValorDinheiro;

    public Pagamento(){}

    public Pagamento(FormaPagamento pagamento) {
        this.modalidadePagamento = pagamento.getModalidadePagamento();
        this.tipoPagamento = pagamento.getTipoPagamento();
        this.trocoParaValorDinheiro = pagamento.getTrocoParaValorDinheiroBD();
    }

    public Pagamento(String modalidadePagamento, String tipoPagamento, BigDecimal trocoParaValorDinheiro) {
        this.modalidadePagamento = modalidadePagamento;
        this.tipoPagamento = tipoPagamento;
        this.trocoParaValorDinheiro = trocoParaValorDinheiro;
    }

    public double getTrocoParaValorDinheiro() {
        return trocoParaValorDinheiro.doubleValue();
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
}
