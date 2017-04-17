package br.com.tsi.controlegastos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import br.com.tsi.controlegastos.dao.UsuarioDAO;
import br.com.tsi.controlegastos.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    public final static String KEY_APP_PREFERENCES = "KEY_APP_PREFERENCES";
    public final static String KEY_LOGIN = "login";

    private EditText etLogin;
    private EditText etSenha;
    private CheckBox cbManterConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etSenha = (EditText) findViewById(R.id.etSenha);
        cbManterConectado = (CheckBox) findViewById(R.id.cbManterConectado);

        if(isConnected())
            iniciarApp();

    }

    private boolean isConnected()
    {
        SharedPreferences shared = getSharedPreferences(KEY_APP_PREFERENCES,MODE_PRIVATE);
        String login = shared.getString(KEY_LOGIN, "");

        if(login.equals(""))
            return false;
        else
            return true;

    }

    public void logar(View view)
    {
        if(isLoginValido()) {
            if (cbManterConectado.isChecked()) {
                manterConectado();
            }

            iniciarApp();
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.login_activity_invalid_user_or_password),Toast.LENGTH_SHORT).show();
        }

    }

    private void manterConectado(){
        String login = etLogin.getText().toString();
        SharedPreferences pref = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit(); editor.putString(KEY_LOGIN, login);
        editor.apply();
    }

    private boolean isLoginValido() {
        String login = etLogin.getText().toString();
        String senha = etSenha.getText().toString();

        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        Usuario usuario = usuarioDAO.getByLogin(login);

        if( usuario != null && usuario.getSenha().equals(senha) )
            return true;
        else
            return false;
    }

    private void iniciarApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
