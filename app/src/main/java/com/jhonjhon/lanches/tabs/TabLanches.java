package com.jhonjhon.lanches.tabs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.adapters.adapterItensDB;
import com.jhonjhon.lanches.adapters.adapterLanches;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.fragments.SingleFragment;
import com.jhonjhon.lanches.intefaces.InicioComunica;
import com.jhonjhon.lanches.intefaces.iRetroService;
import com.jhonjhon.lanches.models.RecordsLanches;
import com.jhonjhon.lanches.models.onlineJson;
import com.jhonjhon.lanches.models.osPedidosFeitos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TabLanches extends Fragment {

    public String nome;
    public ListView listaView;
    RecordsLanches recordsLanches;
    osPedidosFeitos osSalvos;
    List<RecordsLanches> listasnova = new ArrayList<>();
    private adapterLanches adapterListaLanches;
    private adapterItensDB adapterListaDB;
    public static FrameLayout FrameDetalhe;
    public static LinearLayout LinearLista;
    LottieAnimationView loadingLotie;
    CardView LoadingCard;
    InicioComunica dadosactiv;
    private boolean Salvo;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lanches, container, false);
        listaView = (ListView) view.findViewById(R.id.listalocal);
        LinearLista = (LinearLayout) view.findViewById(R.id.LinearLista);
        FrameDetalhe = (FrameLayout) view.findViewById(R.id.FrameDetalhe);
        LoadingCard = (CardView) view.findViewById(R.id.loadingCard);
        loadingLotie = view.findViewById(R.id.loadingLotie);

        PreferencesUsuario preferencesUsuario = new PreferencesUsuario(getContext());
       // preferencesUsuario.limparLanches();
        Salvo = preferencesUsuario.getDadosSalvos(1);

        if(Salvo){
            new CarregaAsync().execute();
        }else {
            CarregaProdutos();
        }


        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                SingleFragment singleFragment = new SingleFragment();
                if(Salvo){
                osSalvos = (osPedidosFeitos) parent.getItemAtPosition(position);

                bundle.putBoolean("salvo", true);
                bundle.putBoolean("lanche", true);
                bundle.putString("valor", String.valueOf(osSalvos.getValorTotal()));
                bundle.putString("nome", osSalvos.getItems());
                bundle.putString("resumo", osSalvos.getResumo());
                bundle.putInt("id", osSalvos.getId());
                dadosactiv.AcaoDetalhe(true, false, osSalvos.getIDitems());

                }else{

                    recordsLanches = (RecordsLanches) parent.getItemAtPosition(position);
                    bundle.putBoolean("salvo", false);
                    bundle.putBoolean("lanche", true);
                    bundle.putString("nome", recordsLanches.getLanche().getNome());
                    bundle.putString("id_item", recordsLanches.getId());
                    bundle.putString("valor", recordsLanches.getLanche().getValor());
                    bundle.putString("imagem", recordsLanches.getLanche().getImagem());
                    bundle.putString("resumo", recordsLanches.getLanche().getResumo());
                 dadosactiv.AcaoDetalhe(true, false, recordsLanches.getId());
                }
                singleFragment.setArguments(bundle);

                LinearLista.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_left));
                LinearLista.setVisibility(View.GONE);
                FrameDetalhe.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in_left, R.anim.fragment_out_left);
                ft.replace(R.id.FrameDetalhe, singleFragment, "FrameDetalhe");
                ft.commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            dadosactiv = (InicioComunica) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementar Comunica");
        }
    }

    private void CarregaProdutos(){
        iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
        final Call<onlineJson> call = pegadados.getLanches();
        call.enqueue(new Callback<onlineJson>() {

            @Override
            public void onResponse(Call<onlineJson> call, Response<onlineJson> response) {
                if(response.code() == 200) {
                    listasnova = response.body().getResult();
                    PopularlistaView(listasnova);
                }
            }
            @Override
            public void onFailure(Call<onlineJson> call, Throwable t) {

            }

        });
    }

    private void PopularlistaView (List<RecordsLanches> produtoslista){
        adapterListaLanches = new adapterLanches(getContext().getApplicationContext(), produtoslista, 1);
        listaView.setAdapter(adapterListaLanches);
        adapterListaLanches.notifyDataSetChanged();
        LoadingCard.setVisibility(View.GONE);
        listaView.setVisibility(View.VISIBLE);
        loadingLotie.pauseAnimation();
    }
    private class CarregaAsync extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            final DBOperador itemsDaoLanche = new DBOperador(ConexaoSQL.getInstance(getActivity().getApplicationContext()));

            List<osPedidosFeitos> produtoslista = new ArrayList<>();
            produtoslista = itemsDaoLanche.getListaItens(1);
            adapterListaDB = new adapterItensDB(getContext(), produtoslista, 1);

            if(produtoslista.size() > 0){
                return true;
            }else{
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if(resultado){
                listaView.setAdapter(adapterListaDB);
                adapterListaDB.notifyDataSetChanged();
                LoadingCard.setVisibility(View.GONE);
                listaView.setVisibility(View.VISIBLE);
                loadingLotie.pauseAnimation();
            }

        }
    }

}
