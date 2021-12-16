package br.com.pedrohmunhoz.appfinancas;

import android.content.Context;

import java.time.LocalDate;

public class Lancamento {
    public int id;
    public String usuario_id;
    public String descricao;
    public int tipoLancamento;
    public double valor;
    public LocalDate data;
    public Context context;
    public String dataFormatada;
    public String valorFormatado;
    public int contabancaria_id;

    public Lancamento(Context context) {
        this.context = context;
    }

    public Lancamento(String descricao, Context context) {
        this.descricao = descricao;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(int tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipoLancamentoDsc() {
        return (tipoLancamento == 1 ? this.context.getString(R.string.titulo_receitas) : this.context.getString(R.string.titulo_despesas));
    }

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(LocalDate dataOriginal) {
        String data = "";
        String ajustaDia = dataOriginal.getDayOfMonth() < 10 ? "0" + dataOriginal.getDayOfMonth() : String.valueOf(dataOriginal.getDayOfMonth());
        data = ajustaDia + "/" + dataOriginal.getMonthValue() + "/" + dataOriginal.getYear();
        dataFormatada = data;
    }

    public String getValorFormatado() {
        return valorFormatado;
    }

    public void setValorFormatado(String valorFormatado) {
        this.valorFormatado = "R$ " + valorFormatado;
    }

    @Override
    public String toString() {
        if (tipoLancamento == 0) {
            return descricao;
        } else {
            String prefixo = (getTipoLancamento() == 1 ? "+ " : "- ");
            return descricao + "  |  " + prefixo + getValorFormatado() + "  |  " + getDataFormatada();
        }
    }

    public int getContabancaria_id() {
        return contabancaria_id;
    }

    public void setContabancaria_id(int contabancaria_id) {
        this.contabancaria_id = contabancaria_id;
    }
}
