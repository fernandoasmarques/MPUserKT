package br.com.meupedidoapp.meupedidokt.model;

public final class Status {
    private StatusEnum status;
    private String mensagem;

    public Status(){}

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
