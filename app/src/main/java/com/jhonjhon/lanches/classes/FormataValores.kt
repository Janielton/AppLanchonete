package com.jhonjhon.lanches.classes

import java.math.BigDecimal

class FormataValores {
    fun ValorInterface(valor: String) :String{
        var valortemp = valor.replace(",",".")
        var novovalor = "%.2f".format(valortemp.toDouble())
        novovalor = "R$ " + novovalor.replace(".",",")
        return novovalor
    }
    fun ValorSegundoPlano(valor: Double, quant: Int) :Double{
        var novovalor = valor * quant
        return novovalor
    }
    fun ValorMenos(valor: String, total: BigDecimal) :BigDecimal{
        var novovalor = total.subtract(BigDecimal(valor))
        return novovalor
    }
    fun ValorMais(valor: String, total: BigDecimal) :BigDecimal{
        var novovalor = total.add(BigDecimal(valor))
        return novovalor
    }
    fun TraformarPraDouble(valor: Double) :String{
        var novovalor = "%.2f".format(valor)
        return novovalor
    }
}