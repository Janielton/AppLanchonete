package com.jhonjhon.lanches.models

import com.google.gson.annotations.SerializedName

class Ingredientes() {

    @SerializedName("records")
    private val result: List<RecordsIngre?>? = null

    fun getResult(): List<RecordsIngre?>? {
        return result
    }


}