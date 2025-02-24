package com.jhonjhon.lanches.intefaces;

import java.util.ArrayList;

public interface InicioComunica {
    public void AcaoDetalhe(boolean lanche, boolean bebidas, String idItem);
    public void MostraBarra(boolean mostra);
    public void ProdutoAdd(String nome, String valor, String imagem, int quantidade, ArrayList<String> ingrediente);
}
