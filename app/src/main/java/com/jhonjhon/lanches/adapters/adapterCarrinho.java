package com.jhonjhon.lanches.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.models.PedidoItem;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.util.List;

public class adapterCarrinho extends BaseAdapter {
    private Context context;
    private List<PedidoItem> produtosList;
    public adapterCarrinho(Context context, List<PedidoItem> produtosList) {
        this.context = context;
        this.produtosList = produtosList;
    }
    public adapterCarrinho() {

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
    public void removerProduto (int posicao){
        this.produtosList.remove(posicao);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {

        View v = View.inflate(this.context, R.layout.listview_carrinho, null);
        TextView Nome = (TextView) v.findViewById(R.id.CarNome);
        TextView Valor = (TextView) v.findViewById(R.id.ValorCar);
        TextView Quantidade = (TextView) v.findViewById(R.id.carQuantidade);
        TextView ingredientes = (TextView) v.findViewById(R.id.ingredientes);
        ImageView imagemlistaprodutos = (ImageView) v.findViewById(R.id.imagemlistaprodutos);



        Valor.setText("R$ " + produtosList.get(posicao).getValorItem());
        Nome.setText(produtosList.get(posicao).getNomeItem());
        Quantidade.setText(produtosList.get(posicao).QuantidadeItem);
        if(produtosList.get(posicao).getImagemItem().length() > 10) {
        if(produtosList.get(posicao).getImagemItem() != null){
        Picasso.get().load(produtosList.get(posicao).getImagemItem()).placeholder(R.drawable.imagempadrao).into(imagemlistaprodutos);
        }else{
          Picasso.get().load(R.drawable.imagempadrao).into(imagemlistaprodutos);
        }
        } else if (produtosList.get(posicao).getImagemItem().equalsIgnoreCase("persona")) {
            Log.d("Imagem", "persona");
            imagemlistaprodutos.setImageDrawable(context.getDrawable(R.drawable.personalizado));
           ingredientes.setText(produtosList.get(posicao).getIngredientesItem());
            ingredientes.setVisibility(View.VISIBLE);
        } else {
            final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(context));
            byte[] outImage = itemsDao.ImagemBlob(Integer.parseInt(produtosList.get(posicao).getImagemItem()));
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            imagemlistaprodutos.setImageBitmap(imageBitmap);
        }

        return v;
    }
    public void atualizar(List<PedidoItem> pProdutos){
        this.produtosList.clear();
        this.produtosList = pProdutos;
        this.notifyDataSetChanged();
    }
}
