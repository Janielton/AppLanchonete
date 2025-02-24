package com.jhonjhon.lanches.intefaces;

import com.jhonjhon.lanches.models.DadosPedido;
import com.jhonjhon.lanches.models.DataJson;
import com.jhonjhon.lanches.models.EnviarDados;
import com.jhonjhon.lanches.models.Ingredientes;
import com.jhonjhon.lanches.models.PedidoItem;
import com.jhonjhon.lanches.models.ResultadoRequest;
import com.jhonjhon.lanches.models.Update;
import com.jhonjhon.lanches.models.onlineJson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface iRetroService {


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://site.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/lanches")
    Call<onlineJson> getLanches();

    @GET("/bebidas")
    Call<onlineJson> getBebidas();

    @GET("/info-app")
    Call<Update> getUpdate();


    @GET("/ingredientes")
    Call<Ingredientes> getIngredientes();

    @GET("/data-atual")
    Call<DataJson> getDataOnline();

    @Headers({"Content-Type: application/json", "Authorization: Basic RWx0b246MDUwMnRhdHk="})
    @POST("/salva-pedido")
    Call<ResultadoRequest> PostPedido(@Body EnviarDados body);


}

