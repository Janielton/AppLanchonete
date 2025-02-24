package com.jhonjhon.lanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DadosPedido {
    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("celular")
    @Expose
    private String celular;
    @SerializedName("endereco")
    @Expose
    private String endereco;
    @SerializedName("numero")
    @Expose
    private String numero;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("observacao")
    @Expose
    private String observacao;
    @SerializedName("hora")
    @Expose
    private String hora;
    @SerializedName("pagamento")
    @Expose
    private String pagamento;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("lanches")
    @Expose
    private String lanches;
    @SerializedName("idevice")
    @Expose
    private String IDevice;
    @SerializedName("versao")
    @Expose
    private String versao;

    public String getIDevice() {
        return IDevice;
    }

    public void setIDevice(String IDevice) {
        this.IDevice = IDevice;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLanches() {
        return lanches;
    }

    public void setLanches(String lanches) {
        this.lanches = lanches;
    }


}
