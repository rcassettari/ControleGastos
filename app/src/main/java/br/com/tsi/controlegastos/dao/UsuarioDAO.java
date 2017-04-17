package br.com.tsi.controlegastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.tsi.controlegastos.R;
import br.com.tsi.controlegastos.model.Usuario;

public class UsuarioDAO {

    private DBOpenHelper banco;
    private Context context;

    public static final String TABELA_USUARIO = "usuario";
    public static final String COLUNA_LOGIN = "login";
    public static final String COLUNA_SENHA = "senha";

    public UsuarioDAO(Context context) {
        this.context = context;
        banco = new DBOpenHelper(context);
    }

    public String sincronizarUsuario(Usuario usuario) throws Exception {
        deleteAll();
        return add(usuario);
    }

    public void deleteAll() throws Exception {
        SQLiteDatabase db = banco.getWritableDatabase();
        String colunas[] = {COLUNA_LOGIN, COLUNA_SENHA};

        try {
            db.beginTransaction();
            db.delete(TABELA_USUARIO, null, null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.endTransaction();
        }
    }


    public String add(Usuario usuario){

        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_LOGIN, usuario.getUsuario());
        values.put(COLUNA_SENHA, usuario.getSenha());
        resultado = db.insert(TABELA_USUARIO, null, values);
        db.close();

        if(resultado == -1)
            return context.getResources().getString(R.string.ERR_DATABASE_INSERT_ERROR);
        else
            return context.getResources().getString(R.string.DATABASE_INSERT_SUCCESS);

    }

    public Usuario getByLogin(String login) {

        SQLiteDatabase db = banco.getReadableDatabase();
        String colunas[] = {COLUNA_LOGIN, COLUNA_SENHA};
        String where = " login = '" + login + "'";
        Cursor cursor = db.query(TABELA_USUARIO, colunas, where, null, null, null, null, null);

        Usuario usuario = null;

        if (cursor != null) {
            if( cursor.moveToFirst() ) {
                usuario = new Usuario();
                usuario.setUsuario(cursor.getString(cursor.getColumnIndex(COLUNA_LOGIN)));
                usuario.setSenha(cursor.getString(cursor.getColumnIndex(COLUNA_SENHA)));
            }
        }

        return usuario;
    }


}


