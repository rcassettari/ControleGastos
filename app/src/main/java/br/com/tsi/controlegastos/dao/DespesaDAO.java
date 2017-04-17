package br.com.tsi.controlegastos.dao;

import android.content.Context;

public class DespesaDAO {

    private DBOpenHelper banco;

    public static final String TABELA_CATEGORIA_DESPESAS = "despesa";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_CATEGORIA_DESPESA_ID = "categoriadespesa_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_DATA = "data";
    public static final String COLUNA_VALOR = "valor";

    public DespesaDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

}
