package br.com.tsi.controlegastos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.tsi.controlegastos.dao.UsuarioDAO;
import br.com.tsi.controlegastos.model.Usuario;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_TIME_MILLIS = 4500;
    private final String CATEGORIA_ERRO="SplashScreenSync";
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        obterDadosUsuario();
        loadAnimationAndStartNextScreen();
    }

    public void obterDadosUsuario() {

        handler.postAtFrontOfQueue(
                new Runnable() {

                    @Override
                    public void run() {
                        new BuscaDadosUsuario().execute(getResources().getString(R.string.url_to_get_user));
                    }
                }
        );

    }

    private void loadAnimationAndStartNextScreen() {

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.animacao_splashscreen);
        anim.reset();

        ImageView iv = (ImageView) findViewById(R.id.splash_image);

        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreenActivity.this,
                        LoginActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME_MILLIS);
    }

    private void sincronizarUsuario(Usuario usuario) throws Exception
    {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        usuarioDAO.sincronizarUsuario(usuario);
    }

    private class BuscaDadosUsuario extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);

                if( conn.getResponseCode() == HttpURLConnection.HTTP_OK )
                {
                    InputStream is = conn.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

                    StringBuilder result = new StringBuilder();
                    String linha;

                    while( (linha = buffer.readLine()) != null )
                    {
                        result.append(linha);
                    }

                    conn.disconnect();

                    return  result.toString();
                }

            } catch (MalformedURLException e) {
                Log.e(CATEGORIA_ERRO, e.getMessage(),e );
            } catch (IOException e) {
                Log.e(CATEGORIA_ERRO, e.getMessage(),e );
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if( s == null )
            {
                Toast.makeText(SplashScreenActivity.this,getResources().getString(R.string.err_sincronize_user_data), Toast.LENGTH_LONG).show();
            } else
            {
                try {

                    JSONObject json = new JSONObject(s);
                    Usuario usuario = new Usuario();
                    usuario.setUsuario(json.getString("usuario"));
                    usuario.setSenha(json.getString("senha"));

                    SplashScreenActivity.this.sincronizarUsuario(usuario);

                } catch (JSONException e) {
                    Log.e(CATEGORIA_ERRO, e.getMessage(),e );
                }
                catch (Exception e) {
                    Log.e(CATEGORIA_ERRO, e.getMessage(),e );
                }
            }
        }
    }


}
