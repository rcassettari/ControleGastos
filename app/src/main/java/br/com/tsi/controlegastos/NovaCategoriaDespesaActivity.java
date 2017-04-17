package br.com.tsi.controlegastos;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.tsi.controlegastos.dao.CategoriaDespesasDAO;
import br.com.tsi.controlegastos.model.CategoriaDespesa;

public class NovaCategoriaDespesaActivity extends AppCompatActivity {

    public final static int CODE_NOVA_CATEGORIA_DESPESA = 1002;
    public final static int CODE_CATEGORIA_DESPESA_ATUALIZADA = 1003;
    private TextInputLayout tilNomeCategoriaDespesa;
    private Button btnCategoriaDespesas;
    private Long categoriaDespesasIdEmEdicao = null;
    private static final String error_category = "Script";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria_despesa);
        tilNomeCategoriaDespesa = (TextInputLayout) findViewById(R.id.tilNomeCategoriaDespesa);
        btnCategoriaDespesas = (Button) findViewById(R.id.btnCategoriaDespesas);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null && bundle.containsKey("categoriaDespesasId"))
        {
            categoriaDespesasIdEmEdicao = bundle.getLong("categoriaDespesasId");
            String categoriaDespesasNome = bundle.getString("categoriaDespesasNome");
            tilNomeCategoriaDespesa.getEditText().setText(categoriaDespesasNome);
            btnCategoriaDespesas.setText(getResources().getString(R.string.button_expense_category_update));
        }

    }

    public void cadastrarAtualizar(View v) {

        try {

            CategoriaDespesasDAO categoriaDespesasDAO = new CategoriaDespesasDAO(this);
            CategoriaDespesa categoriaDespesa = new CategoriaDespesa();
            categoriaDespesa.setNome(tilNomeCategoriaDespesa.getEditText().getText().toString());

            if (categoriaDespesasIdEmEdicao != null) {
                categoriaDespesa.setId(categoriaDespesasIdEmEdicao);
                categoriaDespesasDAO.updateById(categoriaDespesa);
                Toast.makeText(this, getResources().getString(R.string.expense_category_updated), Toast.LENGTH_LONG).show();
            } else {
                categoriaDespesasDAO.add(categoriaDespesa);
                Toast.makeText(this, getResources().getString(R.string.expense_category_created), Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception ex)
        {
            Toast.makeText(this, getResources().getString(R.string.err_keeping_expense_category) + ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(error_category,ex.getMessage(),ex);
        }

        if (categoriaDespesasIdEmEdicao != null)
            retornaParaTelaAnterior(this.CODE_CATEGORIA_DESPESA_ATUALIZADA);
        else
            retornaParaTelaAnterior(this.CODE_NOVA_CATEGORIA_DESPESA);
    }

    public void retornaParaTelaAnterior(int codigoEvento)
    {
        Intent intentMessage=new Intent();
        setResult(codigoEvento, intentMessage);
        finish();
    }

}
