package br.com.meupedidoapp.meupedidokt.model;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum FormaPagamento {
    //DINHEIRO("DINHEIRO", "A_VISTA", 0.0),
    CARTAOCREDITO("CARTAO_CREDITO_CRED", "CREDITO", 0.0),
    CARTAODEBITO("CARTAO_CREDITO_DEB", "DEBITO", 0.0);

    private final String tipoPagamento;
    private final String modalidadePagamento;
    private final BigDecimal trocoParaValorDinheiro;

    FormaPagamento(String tipoPagamento, String modalidadePagamento, double trocoParaValorDinheiro) {
        this.tipoPagamento = tipoPagamento;
        this.modalidadePagamento = modalidadePagamento;
        this.trocoParaValorDinheiro = new BigDecimal(String.valueOf(trocoParaValorDinheiro));
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public String getModalidadePagamento() {
        return modalidadePagamento;
    }

    public double getTrocoParaValorDinheiro() {
        return trocoParaValorDinheiro.doubleValue();
    }

    public BigDecimal getTrocoParaValorDinheiroBD() {
        return trocoParaValorDinheiro;
    }
}
