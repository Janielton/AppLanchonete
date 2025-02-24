package com.jhonjhon.lanches.paginas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.Alertas;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ContatoActivity extends AppCompatActivity {
    TextView titulo;
    EditText mensagem;
    Alertas alertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
        titulo = (TextView) findViewById(R.id.tvTitulo);
        mensagem = (EditText) findViewById(R.id.editMSG);
        alertas = new Alertas(this, false);

        titulo.setText("Fale conosco");
    }

    public void eventFechar(View view) {
        finish();
    }

    public void enviarMSG(View view) {
        String numero = "5577998050013";
        if (!mensagem.getText().toString().isEmpty()) {


            PackageManager packageManager = getApplicationContext().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = null;
            try {
                url = "https://wa.me/" + numero + "?text=" + URLEncoder.encode(mensagem.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.fromParts("sms", numero, null));
                sendIntent.putExtra("sms_body", mensagem.getText().toString());
                startActivity(sendIntent);
            }

        } else {
            alertas.ExiberAlertaErro("Por favor, digite sua mensagem");
        }

    }
}