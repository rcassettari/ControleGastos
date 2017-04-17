package br.com.tsi.controlegastos;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tvAppVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAppVersionName = (TextView) findViewById(R.id.tvAppVersionName);
        String nomeVersao = obterNomeVersaoAplicativo();
        tvAppVersionName.setText(getResources().getString(R.string.app_version_name_title) + nomeVersao);
    }

    private String obterNomeVersaoAplicativo()
    {
        PackageInfo pinfo;
        String appVersionName;

        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            pinfo = null;
        }

        if( pinfo != null  )
            appVersionName = pinfo.versionName;
        else
            appVersionName = getResources().getString(R.string.app_version_name_default_when_no_access);

        return appVersionName;
    }

    public void fecharVersao(View view)
    {
        finish();
    }
}
