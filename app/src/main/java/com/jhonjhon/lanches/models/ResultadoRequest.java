package com.jhonjhon.lanches.models;

import com.google.gson.annotations.SerializedName;

public class ResultadoRequest {
    @SerializedName("sucesso")
    boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
