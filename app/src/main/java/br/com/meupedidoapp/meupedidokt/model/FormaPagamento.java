package br.com.meupedidoapp.meupedidokt.model;

import java.math.BigDecimal;

public enum FormaPagamento {
    DINHEIRO("DINHEIRO", "Dinheiro", "A_VISTA", 0.0),
    CARTAOCREDITO("CARTAO_CREDITO_CRED", "Cartão de Crédito", "CREDITO", 0.0),
    CARTAODEBITO("CARTAO_CREDITO_DEB", "Cartão de Crédito", "DEBITO", 0.0);

    private final String tipoPagamento;
    private final String modalidadePagamento;
    private final BigDecimal trocoParaValorDinheiro;
    private final String descricao;

    FormaPagamento(String tipoPagamento, String descricao, String modalidadePagamento, double trocoParaValorDinheiro) {
        this.tipoPagamento = tipoPagamento;
        this.descricao = descricao;
        this.modalidadePagamento = modalidadePagamento;
        this.trocoParaValorDinheiro = new BigDecimal(String.valueOf(trocoParaValorDinheiro));
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public String getModalidadePagamento() {
        return modalidadePagamento;
    }

    public BigDecimal getTrocoParaValorDinheiro() {
        return trocoParaValorDinheiro;
    }

    public String getDescricao() {
        return descricao;
    }
}
