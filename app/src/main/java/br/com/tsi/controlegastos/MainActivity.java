package br.com.tsi.controlegastos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.tsi.controlegastos.adapter.CategoriaDespesaAdapter;
import br.com.tsi.controlegastos.dao.CategoriaDespesasDAO;
import br.com.tsi.controlegastos.model.CategoriaDespesa;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvLista;
    private CategoriaDespesaAdapter categoriaDespesaAdapter;
    private List<CategoriaDespesa> listaCategoriaDespesasFromDatabase;
    private static final String sucesso = "success";
    private static final String error_category = "Script";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvLista = (RecyclerView) findViewById(R.id.rvLista);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Chama a tela de cadastro e aguarda um retorno que irá chamar o método onActivityResult
                startActivityForResult(new Intent(MainActivity.this, NovaCategoriaDespesaActivity.class), NovaCategoriaDespesaActivity.CODE_NOVA_CATEGORIA_DESPESA);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new BuscaDadosCategoriaDespesas().execute();
    }

    //Chamado qndo retornar para essa tela da tela de cadastro de uma nova Categoria de Despesa ou de atualização da categoria da despesa
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.cancelled), Toast.LENGTH_LONG).show();
        }
        else
        if(requestCode == NovaCategoriaDespesaActivity.CODE_NOVA_CATEGORIA_DESPESA
                || requestCode == NovaCategoriaDespesaActivity.CODE_CATEGORIA_DESPESA_ATUALIZADA )
        {
            new BuscaDadosCategoriaDespesas().execute();
        }
    }

    private List<CategoriaDespesa> carregaCategoriaDespesas() {

        CategoriaDespesasDAO categoriaDespesasDAO = new CategoriaDespesasDAO(this);
        List<CategoriaDespesa> listaCategoriaDespesas = categoriaDespesasDAO.getAll();
        return listaCategoriaDespesas;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    private void sair(){
        SharedPreferences pref = getSharedPreferences(LoginActivity.KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit(); editor.putString(LoginActivity.KEY_LOGIN, "");
        editor.apply();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.nav_about:
            {
                startActivity(new Intent(this, AboutActivity.class));
                break;
            }

            case R.id.nav_sair:
                sair();
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpListaCategoriaDespesas(List<CategoriaDespesa> listaCategoriaDespesas) {

        categoriaDespesaAdapter = new CategoriaDespesaAdapter(this,listaCategoriaDespesas);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setAdapter(categoriaDespesaAdapter);

    }

    private class BuscaDadosCategoriaDespesas extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(MainActivity.this);
            pdLoading.setMessage(getResources().getString(R.string.loading_data));
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<CategoriaDespesa> listaCategoriaDespesas = new ArrayList<>();
            String retorno = sucesso;

            try {
                // Faz a leitura dos dados a partir do banco de dados.
                listaCategoriaDespesas =  carregaCategoriaDespesas();
                listaCategoriaDespesasFromDatabase = listaCategoriaDespesas;
                return retorno;
            } catch (Exception e) {
                retorno = e.getMessage();
            }

            return retorno;
        }

        @Override
        protected void onPostExecute(String status) {

            if( !status.equals(sucesso) )
            {
                Toast.makeText(MainActivity.this,getResources().getString(R.string.err_loading_data) + status, Toast.LENGTH_SHORT).show();
            } else
            {
                try {
                    // Uma vez os dados lidos, carrego no adapter
                    setUpListaCategoriaDespesas(listaCategoriaDespesasFromDatabase);
                } catch (Exception ex) {
                    Log.e(error_category,ex.getMessage(),ex);
                }
            }

            pdLoading.dismiss();
        }
    }

    private void excluiCategoriaDespesas(long categoriaDespesasId)
    {
        try {
            CategoriaDespesasDAO categoriaDespesasDAO = new CategoriaDespesasDAO(this);
            CategoriaDespesa categoriaDespesa = new CategoriaDespesa();
            categoriaDespesa.setId(categoriaDespesasId);
            categoriaDespesasDAO.deleteById(categoriaDespesa);
        }
        catch(Exception ex)
        {
            Log.e(error_category,ex.getMessage(),ex);
            Toast.makeText(this, getResources().getString(R.string.err_delete_expense_category) + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void excluir(View view)
    {
        View parentView = (View) view.getParent();

        TextView categoriaDespesasIdView = (TextView) parentView.findViewById(R.id.tvIdCategoriaDespesa);
        TextView nomeCategoriaDespesasView = (TextView) parentView.findViewById(R.id.tvNome);
        long categoriaDespesasId = Long.parseLong(categoriaDespesasIdView.getText().toString());
        excluiCategoriaDespesas(categoriaDespesasId);
        new BuscaDadosCategoriaDespesas().execute();
        Toast.makeText(this, getResources().getString(R.string.delete_expense_category_completed) + nomeCategoriaDespesasView.getText().toString(), Toast.LENGTH_LONG).show();
    }

    public void editar(View view)
    {
        View parentView = (View) view.getParent();
        TextView categoriaDespesasIdView = (TextView) parentView.findViewById(R.id.tvIdCategoriaDespesa);
        TextView nomeCategoriaDespesasView = (TextView) parentView.findViewById(R.id.tvNome);
        long categoriaDespesasId = Long.parseLong(categoriaDespesasIdView.getText().toString());

        Intent intentForNewActivity = new Intent(MainActivity.this, NovaCategoriaDespesaActivity.class);
        intentForNewActivity.putExtra("categoriaDespesasId",categoriaDespesasId);
        intentForNewActivity.putExtra("categoriaDespesasNome",nomeCategoriaDespesasView.getText().toString());

        //Chama a tela de atualização e aguarda um retorno que irá chamar o método onActivityResult
        startActivityForResult(intentForNewActivity , NovaCategoriaDespesaActivity.CODE_NOVA_CATEGORIA_DESPESA);
    }

}
