package br.com.tsi.controlegastos.dao;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.tsi.controlegastos.R;

/**
 * Created by rcass_000 on 13/03/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "controlegastos.db";
    private static final int VERSAO_BANCO = 2;
    private Context ctx;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSAO_BANCO);
        this.ctx = context;
    }

    // Método chamado quando há necessidade de criar o banco
    @Override public void onCreate(SQLiteDatabase db) {
        // cria a estrutura do banco
        lerEExecutarSQLScript(db, ctx, R.raw.db_criar);
    }

    // Método chamado quando há necessidade de atualizar o banco o banco
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for(int i = oldVersion; i < newVersion; ++i) {
            String migrationFileName = String.format("from_%d_to_%d", i, (i+1));
            log("Looking for migration file: " + migrationFileName);
            int migrationFileResId = ctx.getResources().getIdentifier(migrationFileName, "raw", ctx.getPackageName());
            if(migrationFileResId != 0) {
                // execute script
                log(ctx.getResources().getString(R.string.found_executing_script));
                lerEExecutarSQLScript(db, ctx, migrationFileResId);
            } else
            {
                log(ctx.getResources().getString(R.string.script_not_found));
            }
        }
    }

    private void lerEExecutarSQLScript(SQLiteDatabase db, Context ctx, Integer sqlScriptResId) {

        Resources res = ctx.getResources();

        try {
            InputStream is = res.openRawResource(sqlScriptResId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            executarSQLScript(db, reader);
            reader.close();
            is.close();
        } catch (IOException e)
        {
            throw new RuntimeException(ctx.getResources().getString(R.string.cannot_read_sqllite_file), e);
        }
    }

    // Método auxiliar para executar o script
    private void executarSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");

            if (line.endsWith(";")) {
                String toExec = statement.toString();
                log(ctx.getResources().getString(R.string.executing_script) + toExec);
                db.execSQL(toExec);
                statement = new StringBuilder();
            }
        }
    }

    private void log(String msg) {
        Log.d(DBOpenHelper.class.getSimpleName(), msg);
    }

}
