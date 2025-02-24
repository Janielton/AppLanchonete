package com.jhonjhon.lanches.models

import com.google.gson.annotations.SerializedName

class RecordsIngre() {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("fields")
    var fildes: FildesIngre? = null

    class FildesIngre {
        @SerializedName("Nome")
        var nome: String? = null

        @SerializedName("Valor")
        var valor: String? = null
    }
}