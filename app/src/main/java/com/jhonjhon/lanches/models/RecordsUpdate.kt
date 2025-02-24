package com.jhonjhon.lanches.models

import com.google.gson.annotations.SerializedName

class RecordsUpdate() {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("fields")
    var update: FildesUpdate? = null

    class FildesUpdate {
        @SerializedName("Nome")
        var nome: String? = null

        @SerializedName("NumeroVersao")
        var dado: String? = null

        @SerializedName("Link")
        var valor: String? = null

    }
}