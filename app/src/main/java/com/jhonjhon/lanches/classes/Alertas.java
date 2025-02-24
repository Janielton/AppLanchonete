package com.jhonjhon.lanches.classes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhonjhon.lanches.R;


public class Alertas {
    private Context context;
    private boolean tipolongo;

    public Alertas(Context contexto, boolean longo){
        this.context = contexto;
       this.tipolongo = longo;
    }
    public void ExiberAlertaSucesso(String mensagem){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.alertas, null);
        TextView Mensagem = view.findViewById(R.id.textMSG);
        Mensagem.setText(mensagem);
        Toast custToast = new Toast(context);
        custToast.setDuration(Toast.LENGTH_LONG);
        if(tipolongo){
            custToast.setDuration(Toast.LENGTH_LONG);
        }else{
            custToast.setDuration(Toast.LENGTH_SHORT);
        }
     //   custToast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        custToast.setView(view);
        custToast.show();
    }
    public void ExiberAlertaErro(String mensagem){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.alertas, null);
        TextView Mensagem = view.findViewById(R.id.textMSG);
        ImageView Imagem = view.findViewById(R.id.imageview);
        LinearLayout Alertlayout =view.findViewById(R.id.Alertlayout);
        Alertlayout.setBackground(context.getResources().getDrawable(R.drawable.fundo_erro));

        Imagem.setBackground(context.getDrawable(R.drawable.ic_erro));
        Mensagem.setText(mensagem);
        Mensagem.setPadding(10,10,10,10);
        Toast custToast = new Toast(context);
        custToast.setDuration(Toast.LENGTH_LONG);
        if(tipolongo){
            custToast.setDuration(Toast.LENGTH_LONG);
        }else{
            custToast.setDuration(Toast.LENGTH_SHORT);
        }
     //   custToast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        custToast.setView(view);
        custToast.show();
    }
}
