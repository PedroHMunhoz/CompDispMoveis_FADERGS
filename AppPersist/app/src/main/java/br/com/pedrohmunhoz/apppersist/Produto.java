package br.com.pedrohmunhoz.apppersist;

import androidx.annotation.NonNull;

public class Produto {
    public int id;

    public String nome;

    public String categoria;

    public Produto() {
    }

    public Produto(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nome + " | " + categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
