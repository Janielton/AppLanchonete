package com.jhonjhon.lanches.fragments;

import android.animation.TimeInterpolator;
import android.content.Context;
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


public class FragmentDialogo extends DialogFragment {

    private Context ctx;
    private boolean Fechado;
    private String Titulo;

    public FragmentDialogo(){

    }
    public FragmentDialogo(Context ctx){
        this.ctx = ctx;
    }

    public static FragmentDialogo newInstance(String title, boolean fechado) {
        FragmentDialogo frag = new FragmentDialogo();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("fechado", fechado);
        frag.setArguments(args);
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
        Fechado = getArguments().getBoolean("fechado");
        Titulo = getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogo, container);
        Button fechar = view.findViewById(R.id.closedialog);
        TextView texto = view.findViewById(R.id.textprincial);
        TextView titulo = view.findViewById(R.id.tvTitulo);

        titulo.setText(Titulo);

        if(Fechado) {
            VerificaHorario verificaHorario = new VerificaHorario(ctx, getActivity());
            texto.setText(verificaHorario.AbreEm());
        }else{
            texto.setText("Sem acesso a internet");
        }

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
