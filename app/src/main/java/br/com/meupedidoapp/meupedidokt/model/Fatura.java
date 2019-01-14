package br.com.meupedidoapp.meupedidokt.model;

import java.math.BigDecimal;

public class Fatura {
    private BigDecimal valorSubTotal;
    private BigDecimal valorTotal;
    private BigDecimal descontos;
    private Pagamento pagamento;

    public Fatura(BigDecimal valorSubTotal, BigDecimal valorTotal, BigDecimal descontos, Pagamento pagamento) {
        this.valorSubTotal = valorSubTotal;
        this.valorTotal = valorTotal;
        this.descontos = descontos;
        this.pagamento = pagamento;
    }

    public BigDecimal getValorSubTotal() {
        return valorSubTotal;
    }

    public void setValorSubTotal(BigDecimal valorSubTotal) {
        this.valorSubTotal = valorSubTotal;
    }

    public void setValorSubTotal(double valorSubTotal) {
        this.valorSubTotal = new BigDecimal(String.valueOf(valorSubTotal));
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = new BigDecimal(String.valueOf(valorTotal));
    }

    public BigDecimal getDescontos() {
        return descontos;
    }

    public void setDescontos(BigDecimal descontos) {
        this.descontos = descontos;
    }

    public void setDescontos(double descontos) {
        this.descontos = new BigDecimal(String.valueOf(descontos));
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
