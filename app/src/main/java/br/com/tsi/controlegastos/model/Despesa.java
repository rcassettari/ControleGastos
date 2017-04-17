package br.com.tsi.controlegastos.model;

import java.io.Serializable;
import java.util.Date;

public class Despesa implements Serializable {

    private long id;
    private String nome;
    private String data;
    private Double valor;

    public void Despesa()
    {

    }

    public void Despesa(String nome, String data, Double valor)
    {
        this.nome = nome;
        this.data = data;
        this.valor = valor;
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
