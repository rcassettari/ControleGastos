package br.com.tsi.controlegastos.model;

import java.io.Serializable;

public class CategoriaDespesa implements Serializable {

    private long id;
    private String nome;

    public void CategoriaDespesa()
    {
    }

    public void CategoriaDespesa(String nome)
    {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString()
    {
        return nome;
    }

}
