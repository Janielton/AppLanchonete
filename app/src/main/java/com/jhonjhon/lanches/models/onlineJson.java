package com.jhonjhon.lanches.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class onlineJson {
    //@SerializedName("products")

    @SerializedName("records")
    private List<RecordsLanches> result;

    public List<RecordsLanches> getResult() {
        return result;
    }

    public void setResult(List<RecordsLanches> result) {
        this.result = result;
    }

}
