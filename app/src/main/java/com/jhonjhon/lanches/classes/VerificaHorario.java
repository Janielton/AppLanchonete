package com.jhonjhon.lanches.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.jhonjhon.lanches.intefaces.iRetroService;
import com.jhonjhon.lanches.models.DataJson;
import com.jhonjhon.lanches.models.RecordsUpdate;
import com.jhonjhon.lanches.models.Update;
import com.jhonjhon.lanches.paginas.PaginaInicio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificaHorario {
    public boolean aberto;
    public static String data;
    public String horario;
    Context context;
    Activity actividade;
    List<DataJson> listasnova = new ArrayList<>();

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public String getData() {
        return data;
    }

    public void setDataIS(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public VerificaHorario(Context contexto, Activity activity){
        this.context = contexto;
        this.actividade = activity;
        //setAberto(false);

        iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
        final Call<DataJson> call = pegadados.getDataOnline();
      //  final PedidosDao itemsDao = new PedidosDao(ConexaoSQL.getInstance(contexto));
        PreferencesUsuario preferencesUsuario = new PreferencesUsuario(contexto);
        call.enqueue(new Callback<DataJson>() {
            @Override
            public void onResponse(Call<DataJson> call, Response<DataJson> response) {
            //    Log.d("Teste 1", String.valueOf(response.toString()));
                if(response.code() == 200) {
                    setDataIS(response.body().getData());
                    DataHoje(getData());

                }else{
                    Calendar dataTime = Calendar.getInstance();
                    String datahoje = dataTime.get(Calendar.YEAR)+"-"+dataTime.get(Calendar.MONTH)+"-"+
                            dataTime.get(Calendar.DAY_OF_MONTH)+"T"+dataTime.get(Calendar.HOUR_OF_DAY)+
                            ":"+dataTime.get(Calendar.MINUTE)+":"+dataTime.get(Calendar.SECOND);
                    setDataIS(datahoje);
                    DataHoje(datahoje);
                }

            }
            @Override
            public void onFailure(Call<DataJson> call, Throwable t) {
                Calendar dataTime = Calendar.getInstance();
                String datahoje = dataTime.get(Calendar.YEAR)+"-"+dataTime.get(Calendar.MONTH)+"-"+
                        dataTime.get(Calendar.DAY_OF_MONTH)+"T"+dataTime.get(Calendar.HOUR_OF_DAY)+
                        ":"+dataTime.get(Calendar.MINUTE)+":"+dataTime.get(Calendar.SECOND);
                setDataIS(datahoje);
                DataHoje(datahoje);
                Log.e("onFailure", t.toString());
            }

        });
    }
    public void DataHoje(String dataP) {
        String novoData = "2020-7-9T13:20:14";
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        try {
            date = dataFormat.parse(dataP);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Erro","DataHoje");
        }

       // String currentDate = formatDate.format(date);
       // setData(currentDate);
       //   Log.d("getHorario", data);

        DateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
        String currentTime = formatTime.format(date);
        setHorario(currentTime);


        DateFormat formatHora = new SimpleDateFormat("HH");
        String currentHora = formatHora.format(date);
        int agora = Integer.parseInt(currentHora);
        if(agora >= 13) {
            setAberto(true);
        }



    }
    public String AbreEm(){
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        if(data != null)
        try {
            date = dataFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatHora = new SimpleDateFormat("HH");
        String currentHora = formatHora.format(date);
        String abre = "";
        int agora = Integer.parseInt(currentHora);
        int abertura = 16 - agora;
       // abre = "Abriremos em "+ String.valueOf(abertura) +" horas";
        abre = "Abriremos em breve";
        return abre;
    }
    public void AbreJanela(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent paginaInicial = new  Intent(actividade, PaginaInicio.class);
                paginaInicial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                paginaInicial.putExtra("aberto",  false);
                actividade.startActivity(paginaInicial);
                actividade.finish();
            }
        }, 1000);
    }

}
