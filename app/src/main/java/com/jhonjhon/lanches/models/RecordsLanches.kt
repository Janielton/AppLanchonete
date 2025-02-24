package com.jhonjhon.lanches.models

import com.google.gson.annotations.SerializedName

class RecordsLanches() {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("fields")
    var lanche: FildesRecords? = null

    class FildesRecords {
        @SerializedName("Nome")
        var nome: String? = null

        @SerializedName("Resumo")
        var resumo: String? = null

        @SerializedName("Valor")
        var valor: String? = null

        @SerializedName("Imagem")
        var imagem: String? = null

    }
}