package com.jhonjhon.lanches.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.classes.FormataValores;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.classes.VereficaUpdate;
import com.jhonjhon.lanches.models.RecordsLanches;
import com.jhonjhon.lanches.models.osPedidosFeitos;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class adapterLanches extends BaseAdapter {
    Context contexto;
    private List<RecordsLanches> produtosList;
    private View Viewlist;
    private int categoria;

    public adapterLanches(Context context, List<RecordsLanches> produtosList, int cat) {
        this.contexto = context;
        this.produtosList = produtosList;
        this.categoria = cat;


    }

    @Override
    public int getCount() {
        return this.produtosList.size();
    }

    @Override
    public Object getItem(int posicao) {
        return this.produtosList.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        return posicao;
    }

    public void removerProduto(int posicao) {
        this.produtosList.remove(posicao);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {

        View v = View.inflate(this.contexto, R.layout.layout_lanches_list, null);
        TextView ViewNome = (TextView) v.findViewById(R.id.ViewNome);
        TextView ViewValor = (TextView) v.findViewById(R.id.ValorText);
      CardView CardLache = (CardView) v.findViewById(R.id.CardLache);
        ImageView imagemlistaprodutos = (ImageView) v.findViewById(R.id.imagemlistaprodutos);
        Viewlist = (View) v.findViewById(R.id.Viewlist);
        CardLache.setCardBackgroundColor(contexto.getResources().getColor(R.color.corBranca));
        //ViewValor.setTextColor(contexto.getResources().getColor(R.color.corIcones));

        FormataValores formataValores = new FormataValores();
        PreferencesUsuario preferencesUsuario = new PreferencesUsuario(contexto);

        ViewValor.setText(formataValores.ValorInterface(produtosList.get(posicao).getLanche().getValor()));

        ViewNome.setText(produtosList.get(posicao).getLanche().getNome());
        if (produtosList.get(posicao).getLanche().getImagem() != null && produtosList.get(posicao).getLanche().getImagem().length() > 0) {
            if (categoria == 1) {
                Picasso.get().load(produtosList.get(posicao).getLanche().getImagem()).placeholder(R.drawable.imagempadrao).into(imagemlistaprodutos);
            }else{
                Picasso.get().load(produtosList.get(posicao).getLanche().getImagem()).placeholder(R.drawable.imagembebidas).into(imagemlistaprodutos);
            }
       } else {
           Picasso.get().load(R.drawable.imagempadrao).into(imagemlistaprodutos);
        }
        //imagemlistaprodutos.setImageDrawable(contexto.getDrawable(R.drawable.imagempadrao));
        posicao++;
        if (posicao == 10) {
            if (!preferencesUsuario.getDadosSalvos(categoria)) {
                VereficaUpdate vereficaUpdate = new VereficaUpdate(contexto);
                if(categoria == 2 && preferencesUsuario.getDadosSalvos(1)){
                  vereficaUpdate.SalvarInDB(produtosList, categoria);
                }else {
                  vereficaUpdate.SalvarInDB(produtosList, categoria);
                }
            }
        }
        if (posicao == produtosList.size()) {
            Viewlist.setVisibility(View.VISIBLE);
        }
        return v;
    }

    public void atualizar(List<RecordsLanches> pProdutos) {
        this.produtosList.clear();
        this.produtosList = pProdutos;
        this.notifyDataSetChanged();
    }

}
