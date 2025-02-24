package com.jhonjhon.lanches.models;

public class PedidoItem {
     String NomeItem;
     String ValorItem;
     String IngredientesItem;
     String ImagemItem;

    public String getIDitem() {
        return IDitem;
    }

    public void setIDitem(String IDitem) {
        this.IDitem = IDitem;
    }

    String IDitem;


    public String QuantidadeItem = "1";
    public String ValorTotalPedido = "0";

    public String getNomeItem() {
        return NomeItem;
    }

    public void setNomeItem(String nomeItem) {
        NomeItem = nomeItem;
    }

    public String getValorItem() {
        return ValorItem;
    }

    public void setValorItem(String valorItem) {
        ValorItem = valorItem;
    }

    public String getIngredientesItem() {
        return IngredientesItem;
    }

    public void setIngredientesItem(String ingredientesItem) {
        IngredientesItem = ingredientesItem;
    }

    public String getImagemItem() {
        return ImagemItem;
    }

    public void setImagemItem(String imagemItem) {
        ImagemItem = imagemItem;
    }

    public String getQuantidadeItem() {
        return QuantidadeItem;
    }

    public void setQuantidadeItem(String quantidadeItem) {
        QuantidadeItem = quantidadeItem;
    }

    public String getValorTotalPedido() {
        return ValorTotalPedido;
    }

    public void setValorTotalPedido(String valorTotalPedido) {
        ValorTotalPedido = valorTotalPedido;
    }

    public PedidoItem() {
    }

}
