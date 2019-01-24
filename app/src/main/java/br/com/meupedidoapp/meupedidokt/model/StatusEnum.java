package br.com.meupedidoapp.meupedidokt.model;

public enum StatusEnum {
    EM_ESPERA("Seu pedido está aguardando aprovação"),
    CANCELADO("Seu pedido foi cancelado."),
    REPROVADO("Seu pedido foi negado pela empresa"),
    APROVADO("Seu pedido está em andamento"),
    ENVIADO("Seu pedido foi enviado"),
    ENTREGUE("Seu pedido foi entregue");

    private String mensagem;

    StatusEnum(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
