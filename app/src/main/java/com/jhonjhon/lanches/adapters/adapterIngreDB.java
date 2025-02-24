package com.jhonjhon.lanches.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.FormataValores;
import com.jhonjhon.lanches.models.osPedidosFeitos;

import java.io.ByteArrayInputStream;
import java.util.List;

public class adapterIngreDB extends BaseAdapter {
    private Context context;
    private List<osPedidosFeitos> produtosList;
    private View Viewlist;
    private int categoria;

    public adapterIngreDB(Context context, List<osPedidosFeitos> produtosList, int cat) {
        this.context = context;
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

        View v = View.inflate(this.context, R.layout.layout_lanches_list, null);
        TextView ViewNome = (TextView) v.findViewById(R.id.ViewNome);
        TextView ViewValor = (TextView) v.findViewById(R.id.ValorText);
        ImageView imagemlistaprodutos = (ImageView) v.findViewById(R.id.imagemlistaprodutos);
        Viewlist = (View) v.findViewById(R.id.Viewlist);

        byte[] outImage = produtosList.get(posicao).getImagem();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
        imagemlistaprodutos.setImageBitmap(imageBitmap);

        FormataValores formataValores = new FormataValores();

        ViewValor.setText(formataValores.ValorInterface(String.valueOf(produtosList.get(posicao).getValorTotal())));

        ViewNome.setText(produtosList.get(posicao).getItems());

        posicao++;
        if (posicao == produtosList.size()) {
            Viewlist.setVisibility(View.VISIBLE);

        }
        return v;
    }

    public void atualizar(List<osPedidosFeitos> pProdutos) {
        this.produtosList.clear();
        this.produtosList = pProdutos;
        this.notifyDataSetChanged();
    }



}
