package br.com.meupedidoapp.meupedidokt.model;

import java.math.BigDecimal;

public final class Fatura {
    private BigDecimal valorSubTotal;
    private BigDecimal valorTotal;
    private BigDecimal descontos;
    private Pagamento pagamento;

    public Fatura(){}

    public Fatura(BigDecimal valorSubTotal, BigDecimal valorTotal, BigDecimal descontos, Pagamento pagamento) {
        this.valorSubTotal = valorSubTotal;
        this.valorTotal = valorTotal;
        this.descontos = descontos;
        this.pagamento = pagamento;
    }

    public double getValorSubTotal() {
        return valorSubTotal.doubleValue();
    }

    public void setValorSubTotal(BigDecimal valorSubTotal) {
        this.valorSubTotal = valorSubTotal;
    }

    public double getValorTotal() {
        return valorTotal.doubleValue();
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getDescontos() {
        return descontos.doubleValue();
    }

    public void setDescontos(BigDecimal descontos) {
        this.descontos = descontos;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}