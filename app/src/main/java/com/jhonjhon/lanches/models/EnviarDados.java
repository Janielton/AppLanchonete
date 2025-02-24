package com.jhonjhon.lanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnviarDados {
    @SerializedName("dados")
    @Expose
    private DadosPedido dados;

    public DadosPedido getDados() {
        return dados;
    }

    public void setDados(DadosPedido dados) {
        this.dados = dados;
}
}
