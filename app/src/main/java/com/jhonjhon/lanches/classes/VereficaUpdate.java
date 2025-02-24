package com.jhonjhon.lanches.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.fragments.FragmentPromo;
import com.jhonjhon.lanches.intefaces.iRetroService;
import com.jhonjhon.lanches.models.RecordsLanches;
import com.jhonjhon.lanches.models.RecordsUpdate;
import com.jhonjhon.lanches.models.Update;
import com.jhonjhon.lanches.models.osPedidosFeitos;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VereficaUpdate {
    private Context contexto;
    private Activity activity;
    private int category;
    private boolean IsUpdated = true;
    private String linkPromo;
    private String linkUpdate;
    int versaoAtual = 2;

    public boolean isUpdated() {
        return IsUpdated;
    }

    public void setUpdated(boolean updated) {
        IsUpdated = updated;
    }

    public boolean isPromo() {
        return IsPromo;
    }

    public void setPromo(boolean promo) {
        IsPromo = promo;
    }

    private boolean IsPromo;


    List<RecordsUpdate> listasnova = new ArrayList<>();
    List<RecordsLanches> listapassada = new ArrayList<>();
    private String ultimaA;

    public VereficaUpdate(Context context, Activity activiti) {
        this.contexto = context;
        this.activity = activiti;

    }

    public VereficaUpdate(Context context) {
        this.contexto = context;
    }

    public void Versao() {
        VerificaHorario verificaHora = new VerificaHorario(contexto, activity);
        iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
        final Call<Update> call = pegadados.getUpdate();
        final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(activity));
        PreferencesUsuario preferencesUsuario = new PreferencesUsuario(contexto);
        CarrinhoPreferences carrinhoPreferences = new CarrinhoPreferences(contexto);
        call.enqueue(new Callback<Update>() {

            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                List<RecordsUpdate> listasnova = new ArrayList<>();
                if(response.code() == 200) {
                    listasnova = response.body().getResult();
                    for (RecordsUpdate records : listasnova) {
                        if (records.getUpdate().getNome().equals("promo")) {
                            if (Integer.parseInt(records.getUpdate().getDado()) == 1) {
                                setPromo(true);
                                linkPromo = records.getUpdate().getValor();
                            } else {
                                Log.d("promo", "Não");
                                setPromo(false);
                            }
                        }
                        else if (records.getUpdate().getNome().equals("versao")) {
                            if (Integer.parseInt(records.getUpdate().getDado()) != versaoAtual) {
                                setUpdated(false);
                                linkUpdate = records.getUpdate().getValor();
                                Log.d("Atualizado", "Não " + records.getUpdate().getDado());
                            } else {
                                setUpdated(true);
                                Log.d("Atualizado", "Sim");
                            }
                        }
                        else if (records.getUpdate().getNome().equals("ingreatualizado")) {
                            if(preferencesUsuario.isIngreAtualizado()) {

                                if (records.getUpdate().getDado().equals(preferencesUsuario.getUltimaAtualizaIngre())) {
                                    Log.d("Ingredientes", " Atualizado online " + records.getUpdate().getDado());
                                    preferencesUsuario.setIngreAtualizado(true);

                                } else {
                                    preferencesUsuario.UltimaAtualizaIngre(records.getUpdate().getDado());
                                    preferencesUsuario.setIngreAtualizado(false);
                                    itemsDao.LimparTabela("ingredientes");

                                    Log.d("Ingredientes", " Não atualizado online " + records.getUpdate().getDado());
                                }
                            }

                        }

                        else if (records.getUpdate().getNome().equalsIgnoreCase("idevice")) {

                            if (!records.getUpdate().getDado().equalsIgnoreCase(preferencesUsuario.getPainelDeviceUP())) {
                                Log.d("Passou", "sim");
                                preferencesUsuario.setPainelDevice(records.getUpdate().getValor());
                                preferencesUsuario.setPainelDeviceUP(records.getUpdate().getDado());

                            }

                        }
                    }


                }
                if (isUpdated() && isPromo() == false) {
                    verificaHora.AbreJanela();
                } else if (isPromo()) {
                    Log.d("Promo", linkPromo);
                    Bundle bundle = new Bundle();

                    FragmentPromo novaacao = FragmentPromo.newInstance();
                    if (linkPromo != null) {
                        bundle.putString("url", linkPromo);
                        novaacao.setArguments(bundle);
                    }
                    novaacao.show(((AppCompatActivity) contexto).getSupportFragmentManager(), "AbrirPromo");
                } else if (!isUpdated()) {
                    AtualizacaoDisponivel(linkUpdate);
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Alertas alertas = new Alertas(contexto, true);
                alertas.ExiberAlertaErro("Houve algum erro");
            }

        });
    }

    public void UltimaAtualizacao() {
        VerificaHorario verificaHora = new VerificaHorario(contexto, activity);
        iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
        final Call<Update> call = pegadados.getUpdate();
        final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(activity));
        PreferencesUsuario preferencesUsuario = new PreferencesUsuario(contexto);
        CarrinhoPreferences carrinhoPreferences = new CarrinhoPreferences(contexto);

        //preferencesUsuario.limparDados();
        call.enqueue(new Callback<Update>() {

            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {

                listasnova = response.body().getResult();
                for (RecordsUpdate records : listasnova) {
                    if (records.getUpdate().getNome().equalsIgnoreCase("versao")) {
                        if (Integer.parseInt(records.getUpdate().getDado()) != versaoAtual) {
                            setUpdated(false);
                            linkUpdate = records.getUpdate().getValor();
                            Log.d("Atualizado", "Não");
                        } else {
                            setUpdated(true);
                            Log.d("Atualizado", "Sim");
                        }
                    }
                    else if (records.getUpdate().getNome().equalsIgnoreCase("promo")) {
                        if (Integer.parseInt(records.getUpdate().getDado()) == 1) {
                            setPromo(true);
                            linkPromo = records.getUpdate().getValor();
                        } else {
                            setPromo(false);
                            Log.d("promo", "Não");
                        }
                    }
                    else if (records.getUpdate().getNome().equalsIgnoreCase("atualizado")) {
                        if (records.getUpdate().getDado().equals(preferencesUsuario.getUltimaAtualiza())) {
                            Log.d("Atualizado online", records.getUpdate().getDado());
                        } else {
                            preferencesUsuario.limparLanches();
                            carrinhoPreferences.LimparCarrinho();
                            itemsDao.LimparTabela("dadositems");
                            Log.d("Nao atualizado online", records.getUpdate().getDado());
                        }

                    }
                    else if (records.getUpdate().getNome().equalsIgnoreCase("ingreatualizado")) {
                        if(preferencesUsuario.isIngreAtualizado()) {

                            if (records.getUpdate().getDado().equals(preferencesUsuario.getUltimaAtualizaIngre())) {
                                Log.d("Ingredientes", " Atualizado online " + records.getUpdate().getDado());
                                preferencesUsuario.setIngreAtualizado(true);

                            } else {
                                preferencesUsuario.UltimaAtualizaIngre(records.getUpdate().getDado());
                                preferencesUsuario.setIngreAtualizado(false);
                                itemsDao.LimparTabela("ingredientes");
                                Log.d("Ingredientes", " Não atualizado online " + records.getUpdate().getDado());
                            }
                        }

                    }
                    else if (records.getUpdate().getNome().equalsIgnoreCase("idevice")) {
                        Log.d("Passou", "sim");
                        if (!records.getUpdate().getDado().equalsIgnoreCase(preferencesUsuario.getPainelDeviceUP())) {
                            preferencesUsuario.setPainelDevice(records.getUpdate().getValor());
                            preferencesUsuario.setPainelDeviceUP(records.getUpdate().getDado());

                        }

                    }

                }
                if (isUpdated() && isPromo() == false) {
                    verificaHora.AbreJanela();
                }else if (isPromo()) {
                    Log.d("Promo", linkPromo);
                    Bundle bundle = new Bundle();

                    FragmentPromo novaacao = FragmentPromo.newInstance();
                    if (linkPromo != null) {
                        bundle.putString("url", linkPromo);
                        novaacao.setArguments(bundle);
                    }
                    novaacao.show(((AppCompatActivity) contexto).getSupportFragmentManager(), "AbrirPromo");
                } else if (!isUpdated()) {
                    AtualizacaoDisponivel(linkUpdate);
                }


            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Alertas alertas = new Alertas(contexto, true);
                alertas.ExiberAlertaErro("Houve algum erro");
            }

        });
    }

    private class SalvaAsync extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            //PreferencesUsuario preferencesUsuario = new PreferencesUsuario(context);
            //  preferencesUsuario.limparDados();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            PreferencesUsuario preferencesUsuario = new PreferencesUsuario(contexto);
            String formataData = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (!preferencesUsuario.getDadosSalvos(category)) {
                if (contexto != null) {
                    final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(contexto));

                    String resumo;
                    osPedidosFeitos lista = null;
                    long itensquant = 0;
                    for (RecordsLanches pedidoItem : listapassada) {
                        lista = new osPedidosFeitos();
                        lista.setValorTotal(Double.valueOf(pedidoItem.getLanche().getValor().replace(",", ".")));
                        lista.setCategoria(category);
                        lista.setData(formataData);
                        lista.setItems(pedidoItem.getLanche().getNome());
                        resumo = pedidoItem.getLanche().getResumo();
//                    if (resumo != null && resumo.contains("%")) {
//                        String resumoresut = null;
//                        try {
//                            resumoresut = URLDecoder.decode(resumo, StandardCharsets.UTF_8.toString());
//                            resumo = resumoresut;
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
                        lista.setResumo(resumo);
                        lista.setIDitems(pedidoItem.getId());
                        lista.setImagem(ImageParaByte(pedidoItem.getLanche().getImagem()));
                        itensquant = itemsDao.salvarItensDAO(lista);



                    }

                    if (itensquant > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    Log.d("contexto", "null");
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado) {
                PreferencesUsuario preferencesUsuario = new PreferencesUsuario(contexto);
                preferencesUsuario.DadosSalvos(true, category);
                preferencesUsuario.UltimaAtualiza(ultimaA);
                Log.d("preferencesUsuario:", "Dados salvos DB " + String.valueOf(category));
            }

        }
    }

    private void AtualizacaoDisponivel(String url) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                contexto, R.style.TemaBottomSheetDialog
        );
        bottomSheetDialog.setCancelable(false);
        View sheetview = LayoutInflater.from(contexto).inflate(R.layout.fragment_update, null);

        Button BtSim = sheetview.findViewById(R.id.BtAtualizarSim);
        Button BtNao = sheetview.findViewById(R.id.BtAtualizarNao);
        Button BtClose = sheetview.findViewById(R.id.fecharSheet);

        BtSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setDismissWithAnimation(true);
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
            }
        });

        BtNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setDismissWithAnimation(true);
                bottomSheetDialog.dismiss();
                VerificaHorario vericaHora = new VerificaHorario(contexto, activity);
                vericaHora.AbreJanela();
            }
        });
        BtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setDismissWithAnimation(true);
                bottomSheetDialog.dismiss();
                VerificaHorario vericaHora = new VerificaHorario(contexto, activity);
                vericaHora.AbreJanela();
            }
        });
        bottomSheetDialog.setContentView(sheetview);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    public void SalvarInDB(List<RecordsLanches> produtosList, int categoria) {
        this.category = categoria;
        this.listapassada = produtosList;

        iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
        final Call<Update> call = pegadados.getUpdate();
        final String[] ultima = {null};
        call.enqueue(new Callback<Update>() {

            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                List<RecordsUpdate> listasnova = new ArrayList<>();
                listasnova = response.body().getResult();
                for (RecordsUpdate records : listasnova) {
                    if (records.getUpdate().getNome().equals("atualizado")) {
                        ultima[0] = records.getUpdate().getDado().replace(".", "");
                    }
                }

                ultimaA = ultima[0];
                new SalvaAsync().execute();

            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {

            }

        });
    }

    private Bitmap GerarBitmap(String imageurl){
        try {
            URL url = new URL(imageurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    private byte[] ImageParaByte(String imageurl) {

//        URL url = null;
//        try {
//            url = new URL(imageurl);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        URLConnection conn = null;
//        try {
//            conn = url.openConnection();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        conn.setRequestProperty("User-Agent", "Firefox");
//
//        try (InputStream inputStream = conn.getInputStream()) {
//            int n = 0;
//            byte[] buffer = new byte[1024];
//            while (-1 != (n = inputStream.read(buffer))) {
//                output.write(buffer, 0, n);
//            }
//            output.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

       // byte[] img = output.toByteArray();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(GerarBitmap(imageurl), 200, 200);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] img = stream.toByteArray();
        return img;
    }
}
