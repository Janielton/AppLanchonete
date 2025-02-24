package com.jhonjhon.lanches.models;

import java.sql.Blob;

public class osPedidosFeitos {
    private int id = 0;
    private String items;
    private String IDitems;
    private String data;
    private String resumo;
    private int quantidadeitens = 1;
    private int categoria;
    private double valorTotal = 0.0;
    private byte[] imagem;

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getIDitems() {
        return IDitems;
    }

    public void setIDitems(String IDitems) {
        this.IDitems = IDitems;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getQuantidadeitens() {
        return quantidadeitens;
    }

    public void setQuantidadeitens(int quantidadeitens) {
        this.quantidadeitens = quantidadeitens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
}
