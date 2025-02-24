package com.jhonjhon.lanches.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.VerificaHorario;


public class DialogoAppMania extends DialogFragment {

    private Context ctx;

    public DialogoAppMania(){

    }
    public DialogoAppMania(Context ctx){
        this.ctx = ctx;
    }

    public static DialogoAppMania newInstance() {
        DialogoAppMania frag = new DialogoAppMania();
        return frag;
    }
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogtema);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogo_appmania, container);
        Button fechar = view.findViewById(R.id.closedialog);
        Button btContato = view.findViewById(R.id.btContato);
        TextView texto = view.findViewById(R.id.textprincial);


        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://appmania.com.br/contato")));
                dismiss();
            }
        });
        return view;
    }

  @Override
  public Animation onCreateAnimation(int transit, boolean enter, int nextAnim){
      return super.onCreateAnimation(transit, enter, nextAnim);
  }
}
