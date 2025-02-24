package com.jhonjhon.lanches.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUsuario {

    public static final String PREFERENCE_NAME = "dados_usuario";

    private SharedPreferences prefs;

    private Context context;

    public PreferencesUsuario(Context contexto){
        this.context = contexto;
    }

    public PreferencesUsuario() {

    }

    //adiciona
    public void addNome(String nome){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("nome", nome);
        editor.apply();
    }
    public void setPrimeiroAcesso(boolean primeiro){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putBoolean("primeiro", primeiro);
        editor.apply();
    }
    public void addPedidos(String product){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("pedidosfeitos", product);
        editor.apply();
    }
    public void addEndereco(String endereco, String endJson){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("endereco", endereco);
        editor.putString("enderecojson", endJson);
        editor.apply();
    }
    public void addLocal(String latitude, String longitude){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.apply();
    }
    public void addCelular(String celular){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("celular", celular);
        editor.apply();
    }
    public void DadosSalvos(boolean salvo, int cat){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putBoolean("dadossalvos_"+String.valueOf(cat), salvo);
        editor.apply();
    }

    public void UltimaAtualiza(String dado){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("ultima", dado);
        editor.apply();
    }
    public void UltimaAtualizaIngre(String dado){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("ultima_ingre", dado);
        editor.apply();
    }
    public void setIngreAtualizado(boolean dado){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putBoolean("ingredientes", dado);
        editor.apply();
    }

    public void setIDDevice(String id){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("id_device", id);
        editor.apply();
    }
    public void setPainelDevice(String id){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("id_painel", id);
        editor.apply();
    }

    public void setPainelDeviceUP(String id){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("painel_up", id);
        editor.apply();
    }

    //pega dados
    public String getPainelDeviceUP(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("painel_up", "");
    }
    public String getPainelDevice(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("id_painel", "");
    }
    public String getIDDevice(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("id_device", "");
    }
    public String getCelular(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("celular", "77999201204");
    }
    public boolean isIngreAtualizado(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("ingredientes", false);
    }
    public String getUltimaAtualiza(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("ultima", "");
    }
    public String getUltimaAtualizaIngre(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("ultima_ingre", "");
    }
    public String getNome(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("nome", "");
    }
    public String getEndereco(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("endereco", "");
    }
    public String getEnderecoJson(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("enderecojson", "");
    }
    public boolean getPrimeiroAcesso(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("primeiro", true);
    }

    public boolean getDadosSalvos(int cat){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("dadossalvos_"+String.valueOf(cat), false);
    }
    public String getLogitude(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("longitude", "");
    }
    public String getLatitude(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("latitude", "");
    }
    public String getProdutosFeitos(){
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("pedidosfeitos", "");
    }

    public void limparDadosTodos() {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor =  prefs.edit().clear();
        editor.commit();
    }
    public void limparLanches() {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor =  prefs.edit();
        editor.remove("ultima");
        editor.remove("dadossalvos_1");
        editor.remove("dadossalvos_2");
        editor.commit();
    }
    public void limparIngre() {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor =  prefs.edit().remove("ultima_ingre");
        editor.commit();
    }
}


