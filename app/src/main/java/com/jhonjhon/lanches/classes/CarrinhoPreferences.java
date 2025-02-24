package com.jhonjhon.lanches.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CarrinhoPreferences {

    public static final String PREFERENCE_NAME = "my_prefs_data";

    private SharedPreferences prefs;

    private Context context;

    public CarrinhoPreferences(Context contexto){
        this.context = contexto;
    }

    public CarrinhoPreferences() {

    }

    //sets
    public void addProdutoCar(String product){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("Pedido", product);
        editor.apply();
    }


    public void addProductCount(int productCount){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor edits = prefs.edit();
        edits.putInt("PedidoQuantidade", productCount);
        edits.apply();
    }

    public void setPedidoCount(int productCount){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor edits = prefs.edit();
        edits.putInt("numeropedido", productCount);
        edits.apply();
    }

    public void setPedidoPersonalizado(int productCount){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor edits = prefs.edit();
        edits.putInt("numeropedido_person", productCount);
        edits.apply();
    }

    //gets
    public String ProdutosCarrinho(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("Pedido", "");
    }

    public int QuantidadeItensPedido(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("PedidoQuantidade", 0);
    }

    public int getPedidoCount(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("numeropedido", 0);
    }
    public int getPedidoPersonalizado(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("numeropedido_person", 1);
    }

    public void clearPedido() {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public void LimparCarrinho() {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.remove("Pedido");
        editor.remove("PedidoQuantidade");
        editor.commit();
    }
}


