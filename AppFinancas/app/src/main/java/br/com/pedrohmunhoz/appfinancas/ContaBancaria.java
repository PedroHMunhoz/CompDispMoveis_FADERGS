package br.com.pedrohmunhoz.appfinancas;

public class ContaBancaria {

    public int id;
    public String usuario_id;
    public String nome_banco;
    public int numero_conta;
    public double saldo_inicial;
    public double saldo_atual;

    public ContaBancaria(String nome) {
        this.nome_banco = nome;
    }

    public ContaBancaria() {

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

    public String getNome_banco() {
        return nome_banco;
    }

    public void setNome_banco(String nome_banco) {
        this.nome_banco = nome_banco;
    }

    public int getNumero_conta() {
        return numero_conta;
    }

    public void setNumero_conta(int numero_conta) {
        this.numero_conta = numero_conta;
    }

    public double getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(double saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public double getSaldo_atual() {
        return saldo_atual;
    }

    public void setSaldo_atual(double saldo_atual) {
        this.saldo_atual = saldo_atual;
    }

    @Override
    public String toString() {
        if (this.numero_conta == 0 || this.saldo_atual == 0) {
            return getNome_banco();
        } else {
            return getNome_banco() + " | " + getNumero_conta() + " | R$" + getSaldo_atual();
        }
    }
}