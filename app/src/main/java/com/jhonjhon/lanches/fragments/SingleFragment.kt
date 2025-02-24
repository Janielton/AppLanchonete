package com.jhonjhon.lanches.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jhonjhon.lanches.R
import com.jhonjhon.lanches.classes.ConexaoSQL
import com.jhonjhon.lanches.classes.FormataValores
import com.jhonjhon.lanches.classes.DBOperador
import com.jhonjhon.lanches.intefaces.InicioComunica
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_single.view.*
import java.io.ByteArrayInputStream

private const val ARG_LANCHE = "lanche"
private const val ARG_NOME = "nome"
private const val ARG_VALOR = "valor"
private const val ARG_RESUMO = "resumo"
private const val ARG_IMAGEM = "imagem"
private const val ARG_ID = "id_item"

class SingleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var nome: String? = null
    private var valor: String? = null
    private var resumo: String? = null
    private var imagem: String? = null
    private var lanche: Boolean = false
    private var idItemS: String? = null;
    private var idItem: Int = 1;
    private var salvo: Boolean = false
    var dadosactiv: InicioComunica? = null
    private var fortamaString = FormataValores()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                salvo = it.getBoolean("salvo")
            }
        }
        if(salvo) {
           arguments?.let {
               lanche = it.getBoolean(ARG_LANCHE)
               nome = it.getString(ARG_NOME)
               valor = it.getString(ARG_VALOR)
               resumo = it.getString(ARG_RESUMO)
               idItem = it.getInt("id")
               imagem = it.getInt("id").toString()


            }
            dadosactiv?.ProdutoAdd(nome, valor, imagem, 1, null)
        }else{
                arguments?.let {
                lanche = it.getBoolean(ARG_LANCHE)
                nome = it.getString(ARG_NOME)
                valor = it.getString(ARG_VALOR)
                resumo = it.getString(ARG_RESUMO)
                idItemS = it.getString(ARG_ID)
                imagem = it.getString(ARG_IMAGEM)
            }
            dadosactiv?.ProdutoAdd(nome, valor, imagem, 1, null)
        }

    }

    override fun onResume() {
        super.onResume()

    }
    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        try {
            dadosactiv = activity as InicioComunica
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " deve implementar Comunica"
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_single, container, false)
        view.titulo.setText(nome)

        if (lanche) {
            view.resumoText.setText(resumo)
            view.resumoText.visibility = View.VISIBLE
        }

        var Mais = view.buttonMais
        var Menos = view.buttonMenos
        var Quantidade = view.editTextQuantidade

        var ValorInteface = view.valorText
        ValorInteface.setText(valor?.let { fortamaString.ValorInterface(it) })

        if(salvo) {
            val itemsDao = DBOperador(
                ConexaoSQL.getInstance(context)
            )

            val outImage: ByteArray = itemsDao.ImagemBlob(idItem)
            val imageStream = ByteArrayInputStream(outImage)
            val imageBitmap = BitmapFactory.decodeStream(imageStream)
            view.imagemView.setImageBitmap(imageBitmap)
            //var ValorTotalItem = valor?.replace(",", ".")?.toDouble()
        }else {
            if (imagem != null) {
                Picasso.get().load(imagem)
                    .placeholder(R.drawable.imagempadrao).into(view.imagemView)
            } else {
                Picasso.get().load(R.drawable.ic_launcher_background).into(view.imagemView)
            }
          //  var ValorTotalItem = valor?.replace(",", ".")?.toDouble()
        }


        Mais.setOnClickListener(View.OnClickListener {
            var quattemp: Int = Quantidade.text.toString().toInt()
            quattemp++
            Quantidade.setText(quattemp.toString())
            var ValorItem = valor?.replace(",", ".")?.toDouble()
            if (ValorItem != null) {
                ValorItem =
                    fortamaString.ValorSegundoPlano(ValorItem, Quantidade.text.toString().toInt())
            }
            ValorInteface.setText(fortamaString.ValorInterface(ValorItem.toString()))
            // ValorTotalItem = ValorItem
            dadosactiv?.ProdutoAdd(nome, ValorItem.toString(), imagem, quattemp, null)

        })
        Menos.setOnClickListener(View.OnClickListener {
            var quattemp: Int = Quantidade.text.toString().toInt()
            if (quattemp != 1) {
                quattemp--
                Quantidade.setText(quattemp.toString())
                var ValorItem = valor?.replace(",", ".")?.toDouble()
                if (ValorItem != null) {
                    ValorItem = fortamaString.ValorSegundoPlano(
                        ValorItem!!,
                        Quantidade.text.toString().toInt()
                    )
                }
                ValorInteface.setText(fortamaString.ValorInterface(ValorItem.toString()))
                //  ValorTotalItem = ValorItem
                dadosactiv?.ProdutoAdd(nome, ValorItem.toString(), imagem, quattemp, null)
            }

        })

        return view;
    }

    companion object {

        @JvmStatic
        fun newInstance(salvos: Boolean) = SingleFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("salvo", salvos)
                }
         }
    }
}