package br.com.tsi.controlegastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.LinkedList;
import java.util.List;
import br.com.tsi.controlegastos.model.CategoriaDespesa;
import br.com.tsi.controlegastos.R;

public class CategoriaDespesasDAO {

    private DBOpenHelper banco;
    private Context context;

    public static final String TABELA_CATEGORIA_DESPESAS = "categoriadespesa";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";

    public CategoriaDespesasDAO(Context context) {
        this.context = context;
        banco = new DBOpenHelper(context);
    }

    public List<CategoriaDespesa> getAll() {

        List<CategoriaDespesa> categoriaDespesas = new LinkedList<>();
        String query = "SELECT * FROM " + TABELA_CATEGORIA_DESPESAS;
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        CategoriaDespesa categoriaDespesa = null;

        if (cursor.moveToFirst()) {
            do {
                categoriaDespesa = new CategoriaDespesa();
                categoriaDespesa.setId(cursor.getLong(cursor.getColumnIndex(COLUNA_ID)));
                categoriaDespesa.setNome(cursor.getString(cursor.getColumnIndex(COLUNA_NOME)));
                categoriaDespesas.add(categoriaDespesa);
            }
            while (cursor.moveToNext());
        }

        return categoriaDespesas;
    }

    public CategoriaDespesa getById(long id) {
        SQLiteDatabase db = banco.getReadableDatabase();
        String colunas[] = {COLUNA_ID, COLUNA_NOME};
        String where = "_id = " + id;
        Cursor cursor = db.query(true, TABELA_CATEGORIA_DESPESAS, colunas, where, null, null, null, null, null);
        CategoriaDespesa categoriaDespesa = null;

        if (cursor != null) {
            cursor.moveToFirst();
            categoriaDespesa = new CategoriaDespesa();
            categoriaDespesa.setNome(cursor.getString(cursor.getColumnIndex(COLUNA_NOME)));
            categoriaDespesa.setId(cursor.getLong(cursor.getColumnIndex(COLUNA_ID)));
        }

        return categoriaDespesa;
    }

    public void updateById(CategoriaDespesa categoriaDespesa) throws Exception {
        SQLiteDatabase db = banco.getWritableDatabase();
        String colunas[] = {COLUNA_ID, COLUNA_NOME};
        String where = "_id = " + categoriaDespesa.getId();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_NOME, categoriaDespesa.getNome());

        try {
            db.beginTransaction();
            db.update(TABELA_CATEGORIA_DESPESAS, contentValues, where, null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.endTransaction();
        }
    }

    public void deleteById(CategoriaDespesa categoriaDespesa) throws Exception {
        SQLiteDatabase db = banco.getWritableDatabase();
        String colunas[] = {COLUNA_ID, COLUNA_NOME};
        String where = "_id = " + categoriaDespesa.getId();

        try {
            db.beginTransaction();
            db.delete(TABELA_CATEGORIA_DESPESAS, where, null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.endTransaction();
        }
    }


    public String add(CategoriaDespesa categoriaDespesa){

        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_NOME, categoriaDespesa.getNome());
        resultado = db.insert(TABELA_CATEGORIA_DESPESAS, null, values);
        db.close();

        if(resultado == -1)
            return context.getResources().getString(R.string.ERR_DATABASE_INSERT_ERROR);
        else
            return context.getResources().getString(R.string.DATABASE_INSERT_SUCCESS);
    }

}

