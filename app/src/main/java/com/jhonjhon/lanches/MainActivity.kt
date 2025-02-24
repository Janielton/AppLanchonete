package com.jhonjhon.lanches

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jhonjhon.lanches.classes.Alertas
import com.jhonjhon.lanches.classes.PreferencesUsuario
import com.jhonjhon.lanches.classes.VereficaUpdate
import com.jhonjhon.lanches.classes.VerificaNET
import com.jhonjhon.lanches.paginas.CadastroActivity
import com.jhonjhon.lanches.paginas.PaginaInicio
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 2000
    private var Criado: Boolean = false
    val connection = VerificaNET(this)
    lateinit var linerInternet: LinearLayout;

    val preferencesUsuario = PreferencesUsuario(this)
    val verificaUpdate =  VereficaUpdate(this, this);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linerInternet = findViewById<LinearLayout>(R.id.linerNoInternet);

        OneSignal.getUserDevice()


    }

    override fun onResume() {
      super.onResume()
        if (connection.IsInternet()) {
            Inicio();
        }else{
            linerInternet.visibility = View.VISIBLE
        }

    }
    fun Inicio(){
        linerInternet.visibility = View.GONE
        if(preferencesUsuario.primeiroAcesso){
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            finish()
          //  AbreJanela()
        }else{
            if(preferencesUsuario.getDadosSalvos(1) || preferencesUsuario.getDadosSalvos(2)){
                verificaUpdate.UltimaAtualizacao()
            }else {
                verificaUpdate.Versao()
            }
        }
    }

    fun clickVerifica(view: View) {
        if (connection.IsInternet()) {
            Inicio();
        }else{
            val alerta = Alertas(this, true);
            alerta.ExiberAlertaErro("Sem acesso à internet")
        }
    }

}
