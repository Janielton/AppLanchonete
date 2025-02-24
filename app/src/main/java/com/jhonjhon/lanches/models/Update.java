package com.jhonjhon.lanches.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Update {
    //@SerializedName("products")

    @SerializedName("records")
    private List<RecordsUpdate> result;

    public List<RecordsUpdate> getResult() {
        return result;
    }


}
