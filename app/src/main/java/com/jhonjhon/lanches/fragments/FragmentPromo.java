package com.jhonjhon.lanches.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.VerificaHorario;
import com.squareup.picasso.Picasso;

public class FragmentPromo extends DialogFragment {

    private Context ctx;
    private String urlimg;
    public FragmentPromo(){

    }
    public FragmentPromo(Context ctx){
        this.ctx = ctx;
    }
    public static FragmentPromo newInstance() {
        FragmentPromo frag = new FragmentPromo();
        frag.setShowsDialog(false);
        frag.setRetainInstance(true);

        frag.setCancelable(true);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogtema);
        if(getArguments()!= null){
            urlimg =  getArguments().getString("url");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo, container);
        Button fechar = view.findViewById(R.id.closedialog);
        ImageView imagem = view.findViewById(R.id.imgPromo);


        if (urlimg != null && urlimg.length() > 5) {
            Picasso.get().load(urlimg).placeholder(R.drawable.imagempadrao).into(imagem);
        }else {
            Log.e("Erro", "Picasso " + urlimg);
        }

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificaHorario verificaHora = new VerificaHorario(ctx, getActivity());
                verificaHora.AbreJanela();
                dismiss();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
