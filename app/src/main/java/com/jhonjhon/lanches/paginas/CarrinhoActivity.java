package com.jhonjhon.lanches.paginas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.adapters.adapterCarrinho;
import com.jhonjhon.lanches.classes.Alertas;
import com.jhonjhon.lanches.classes.CarrinhoPreferences;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.classes.EnviarNotificacao;
import com.jhonjhon.lanches.classes.FormataValores;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.classes.VerificaHorario;
import com.jhonjhon.lanches.classes.VerificaNET;
import com.jhonjhon.lanches.fragments.EnderecoFragment;
import com.jhonjhon.lanches.fragments.FragmentDialogo;
import com.jhonjhon.lanches.fragments.FragmentEntrega;
import com.jhonjhon.lanches.intefaces.InterCarrinho;
import com.jhonjhon.lanches.intefaces.iRetroService;
import com.jhonjhon.lanches.models.DadosPedido;
import com.jhonjhon.lanches.models.EnviarDados;
import com.jhonjhon.lanches.models.PedidoItem;
import com.jhonjhon.lanches.models.ResultadoRequest;
import com.jhonjhon.lanches.models.onlineJson;
import com.jhonjhon.lanches.models.osPedidosFeitos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CarrinhoActivity extends AppCompatActivity implements InterCarrinho {
    private CarrinhoPreferences produtoscarrinho = new CarrinhoPreferences(CarrinhoActivity.this);
    adapterCarrinho adaptercarrinho;
    private ListView listaCarrinho;
    private Button addendereco, apagarItem, finalizarPedido, adicinarItens, salvarNome, editarNome, confirmarLocal;
    private TextView totalPedido, LegendaEntrega, LegendaNome;
    private EditText nomeusuario, EditTroco, EditObeserva;
    private int selecao;
    private boolean editar = false;
    private double latitude, longitude;
    private boolean localizacao = true;
    private boolean endereco = false;
    private boolean Frag = false;
    PedidoItem pedidoItem;
    String formadePagamento = "Dinheiro";
    List<PedidoItem> pedidolista;
    ProgressDialog progressDialog;
    PreferencesUsuario preferencesUsuario;
    LinearLayout LayoutBaixoEntrga, LayoutTopo, LayoutTopEntrega, LayoutBaixo, layoutLocal, LayoutNome;
    FragmentManager fm = getSupportFragmentManager();
    FragmentEntrega fragmententrega = new FragmentEntrega();
    EnderecoFragment enderecoFragment = new EnderecoFragment();
    FrameLayout frameLayout;
    Alertas alertas = new Alertas(this, true);
    VerificaHorario verificaHorario;
    EnviarNotificacao enviarNotificacao;
    String idPedido;

    //adicionarDB
    private final DBOperador dbOperador = new DBOperador(ConexaoSQL.getInstance(CarrinhoActivity.this));
    private String items;
    private int quantidadeitens;
    Double valorTotal = 0.0;
    byte[] imagem;
    int numeropedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        listaCarrinho = (ListView) findViewById(R.id.lista_itens_carrinho);

        apagarItem = (Button) findViewById(R.id.apagarItem);
        adicinarItens = (Button) findViewById(R.id.additens);
        salvarNome = (Button) findViewById(R.id.salvarNome);
        finalizarPedido = (Button) findViewById(R.id.finalizarCarrinho);
        editarNome = (Button) findViewById(R.id.editarNome);
        confirmarLocal = (Button) findViewById(R.id.confirmarLocal);
        addendereco = (Button) findViewById(R.id.addendereco);

        LegendaNome = (TextView) findViewById(R.id.LegendaNome);
        LegendaEntrega = (TextView) findViewById(R.id.LegendaEntrega);
        totalPedido = (TextView) findViewById(R.id.totalPedido);
        nomeusuario = (EditText) findViewById(R.id.textNome);

        LayoutBaixoEntrga = (LinearLayout) findViewById(R.id.LayoutBaixoEntrga);
        LayoutTopEntrega = (LinearLayout) findViewById(R.id.LayoutTopEntrega);
        LayoutTopo = (LinearLayout) findViewById(R.id.LayoutTopo);
        LayoutBaixo = (LinearLayout) findViewById(R.id.LayoutBaixo);
        layoutLocal = (LinearLayout) findViewById(R.id.layoutLocal);
        LayoutNome = (LinearLayout) findViewById(R.id.LayoutNome);
        frameLayout = (FrameLayout) findViewById(R.id.fragment_conteudo);

        verificaHorario = new VerificaHorario(CarrinhoActivity.this, CarrinhoActivity.this);
        preferencesUsuario = new PreferencesUsuario(CarrinhoActivity.this);
        Log.d("getIDDevice", preferencesUsuario.getPainelDevice());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Enviando pedido");
        progressDialog.setMessage("Por favor aguarde...");

        pedidoItem = new PedidoItem();
        numeropedidos = dbOperador.getNumeroPedidos();
        numeropedidos++;

        if (preferencesUsuario.getLatitude() != "") {
            latitude = Double.parseDouble(preferencesUsuario.getLatitude());
            longitude = Double.parseDouble(preferencesUsuario.getLogitude());
        }

        apagarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApagarItem();
            }
        });

        addendereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsarEnderecoOuMapa();
            }
        });

        salvarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nomeusuario.getText().toString().isEmpty() == false) {
                    preferencesUsuario.addNome(nomeusuario.getText().toString());
                    LayoutNome.setVisibility(View.GONE);
                    if (editar == false) {
                        FinanizarPedido();
                    } else {
                        String nome = preferencesUsuario.getNome();
                        String[] splited = nome.split("\\s+");
                        if(splited.length > 2){
                            nome = splited[0]+" "+splited[1];
                        }
                        LegendaNome.setText(nome);
                        editarNome.setVisibility(View.GONE);
                    }
                } else {
                    alerta("Digite o nome");
                }
            }
        });

        confirmarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localizacao == true) {
                    preferencesUsuario.addLocal(String.valueOf(latitude), String.valueOf(longitude));
                    EnvivarPedido();
                } else {
                    if (!preferencesUsuario.getEndereco().isEmpty()) {
                        EnvivarPedido();
                    } else {
                        alertas.ExiberAlertaErro("Você precisar salvar/adicionar um endereço");
                    }
                }

            }
        });

        LegendaNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editarNome.getVisibility() == View.GONE) {
                    editarNome.setVisibility(View.VISIBLE);
                } else {
                    editarNome.setVisibility(View.GONE);
                    LayoutNome.setVisibility(View.GONE);
                }

            }
        });

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinanizarPedido();
            }
        });

        listaCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selecao = position;
                if (apagarItem.getVisibility() == View.VISIBLE) {
                    apagarItem.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            apagarItem.setVisibility(View.VISIBLE);

                        }
                    }, 200);
                } else {
                    apagarItem.setVisibility(View.VISIBLE);
                }

            }
        });

        if (produtoscarrinho.QuantidadeItensPedido() != 0) {
            String json = produtoscarrinho.ProdutosCarrinho();
            List<PedidoItem> ListadeProdutos = new ArrayList<>();
            try {
                JSONArray PedidoJosn = new JSONArray(json);
                BigDecimal total = new BigDecimal(0);
                PedidoItem Temporaria = null;
                for (int i = 0; i < PedidoJosn.length(); i++) {
                    Temporaria = new PedidoItem();
                    JSONObject LacoJson = PedidoJosn.getJSONObject(i);
                    BigDecimal valor = new BigDecimal(LacoJson.getString("valor").replace(",","."));
                    total = total.add(valor);
                    Temporaria.setNomeItem(LacoJson.getString("nome"));
                    Temporaria.setValorItem(LacoJson.getString("valor").replace(".", ","));
                    Temporaria.setQuantidadeItem(LacoJson.getString("quantidade"));
                    Temporaria.setIDitem(LacoJson.getString("id_item"));
                    Temporaria.setIngredientesItem(LacoJson.getString("ingredientes"));
                    Temporaria.setImagemItem(LacoJson.getString("imagem"));
                    //  Temporaria.setAcompanha(LacoJson.getString("acompanhamentos"));
                    //   Temporaria.setAdicionais(LacoJson.getString("adicional"));
                    ListadeProdutos.add(Temporaria);

                }
                PopularlistaView(ListadeProdutos);
                atualizarTotal(total);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Erro", "ao pegar dados do json");
            }

        } else {
            alertas.ExiberAlertaErro("Você ainda não adicionou item ao carrinho");
            finalizarPedido.setEnabled(false);
        }

        enviarNotificacao = new EnviarNotificacao(CarrinhoActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void UsarEnderecoOuMapa(){
        if (localizacao) {
            frameLayout.setVisibility(View.VISIBLE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

            ft.replace(R.id.fragment_conteudo, enderecoFragment, "FragEndereco");
            ft.addToBackStack(null);
            ft.commit();

            addendereco.setText("Localização");
            confirmarLocal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_endereco, 0);
            addendereco.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_botao_marcador, 0, 0, 0);
            localizacao = false;
        } else {
            if(endereco) {
                endereco = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right);
                ft.addToBackStack(null);
                ft.replace(R.id.fragment_conteudo, fragmententrega, "FragEntrega");
                ft.commit();
                addendereco.setText("Endereço");
                confirmarLocal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_botao_marcador, 0);
                addendereco.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_endereco, 0, 0, 0);
            }
            localizacao = true;

        }
    }

    private void EnvivarPedido() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                CarrinhoActivity.this, R.style.BottomSheetDialogTheme
        );
        View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_enviar, (LinearLayout) findViewById(R.id.SheetEnviar));
        Button BtEnviar = sheetview.findViewById(R.id.BtEnviar);
        Button BtClose = sheetview.findViewById(R.id.fecharSheet);
        EditTroco = sheetview.findViewById(R.id.EditTroco);
        EditObeserva = sheetview.findViewById(R.id.EditObeserva);
        TextView pedidonumero = sheetview.findViewById(R.id.pedidonumero);
        RadioButton checkboxDin = sheetview.findViewById(R.id.checkboxDin);
        RadioButton checkboxCar = sheetview.findViewById(R.id.checkboxCar);

        pedidonumero.setText("Pedido #" + String.valueOf(numeropedidos));

        checkboxDin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxCar.setChecked(false);
              //  EditTroco.setVisibility(View.VISIBLE);
                formadePagamento = "Dinheiro";
            }
        });
        checkboxCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxDin.setChecked(false);
             //   EditTroco.setVisibility(View.GONE);
                formadePagamento = "Cartão";
            }
        });
        BtEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificaNET verificaNET = new VerificaNET(CarrinhoActivity.this);
                boolean internet = verificaNET.IsInternet();

                if (verificaHorario.isAberto() && internet) {
                    bottomSheetDialog.setDismissWithAnimation(true);
                    bottomSheetDialog.dismiss();
                    EnviaPedidoOnline();


                } else {
                    bottomSheetDialog.dismiss();
                    if(internet){
                        FragmentDialogo novaacao = FragmentDialogo.newInstance("Estamos fechados", true);
                        novaacao.show(getSupportFragmentManager(), "FechadoAviso");
                    }else{
                        FragmentDialogo novaacao = FragmentDialogo.newInstance("Sem Internet", false);
                        novaacao.show(getSupportFragmentManager(), "SemInternet");
                    }

                }

            }
        });
        BtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setDismissWithAnimation(true);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(sheetview);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    public void SalvarPedido() {
        osPedidosFeitos pedidosFeitos = new osPedidosFeitos();
        Date data = new Date();
        SimpleDateFormat dataformat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String novadata = dataformat.format(data);
        pedidosFeitos.setData(novadata);
        pedidosFeitos.setValorTotal(valorTotal);
        pedidosFeitos.setImagem(imagem);
        pedidosFeitos.setQuantidadeitens(quantidadeitens);
        pedidosFeitos.setItems(items);
        dbOperador.salvarPedidosDAO(pedidosFeitos);
        produtoscarrinho.LimparCarrinho();
        progressDialog.dismiss();
        alertas.ExiberAlertaSucesso("Pedido enviado");

        finish();

    }

    private void EnviarNotificacao() {
        String[] params = new String[4];
        params[0] = preferencesUsuario.getPainelDevice();
        params[1] = "https://i.ibb.co/fMxKQ4C/pedido-app.jpg";
        params[2] = "Pedido ID-"+idPedido;
        params[3] = "Novo pedido feito no App";

        enviarNotificacao.execute(params);

    }

    private void EnviaPedidoOnline() {
        if(items != null) {
            preferencesUsuario.addPedidos(produtoscarrinho.ProdutosCarrinho());
            Log.d("preferencesUsuario", preferencesUsuario.getProdutosFeitos());
            progressDialog.show();
            iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
            Random gerador = new Random();
            int numero1 = gerador.nextInt(8) + 1;
            int numero2 = gerador.nextInt(9);
            int numero3 = gerador.nextInt(9);
            int numero4 = gerador.nextInt(9);
            idPedido = String.valueOf(numero1 + "" + numero2 + "" + numero3 + "" + numero4);

            Date data = new Date();
            // SimpleDateFormat formataID = new SimpleDateFormat("HHmmssyy");
            SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm");
            //String idPedido = formataID.format(data);
            String Hora = formataHora.format(data);
            String Data = formataData.format(data);

            //   FormataValores formataValores = new FormataValores();

            DadosPedido dadosPedido = new DadosPedido();
            dadosPedido.setNumero(idPedido);
            dadosPedido.setNome(preferencesUsuario.getNome());
            dadosPedido.setCelular(preferencesUsuario.getCelular());
            if (localizacao) {
                dadosPedido.setEndereco(preferencesUsuario.getLatitude() + "," + preferencesUsuario.getLogitude());
            } else {
                dadosPedido.setEndereco(preferencesUsuario.getEndereco().replaceAll("-", " / "));
            }

            dadosPedido.setData(Data);
            dadosPedido.setHora(Hora);

            if (EditObeserva.getText().toString().isEmpty()) {
                dadosPedido.setObservacao("Nenhuma");
            } else {
                String temp = EditObeserva.getText().toString();
                dadosPedido.setObservacao(temp.replace("\n", " "));
            }
            dadosPedido.setPagamento(formadePagamento);
            dadosPedido.setTotal(valorTotal.toString().replace(",", "."));
            dadosPedido.setLanches(items);
            dadosPedido.setIDevice(preferencesUsuario.getIDDevice());
            dadosPedido.setVersao("3.0");
            //   Log.d("getIDDevice", preferencesUsuario.getIDDevice());

            EnviarDados enviarDados = new EnviarDados();
            enviarDados.setDados(dadosPedido);

            Call<ResultadoRequest> callPost = pegadados.PostPedido(enviarDados);
            callPost.enqueue(new Callback<ResultadoRequest>() {

                @Override
                public void onResponse(Call<ResultadoRequest> call, Response<ResultadoRequest> response) {
                    if (response.body().isStatus()) {
                        Log.d("onResponse", "Pedido enviado");
                        EnviarNotificacao();
                        //  SalvarPedido();
                    } else {
                        Log.d("onResponse", "Pedido não enviado");
                        progressDialog.dismiss();
                    }


                }

                @Override
                public void onFailure(Call<ResultadoRequest> call, Throwable t) {
                    Log.e("Erro ao enviar pedido", t.getMessage());
                }

            });
        }

    }

    private void showDialog(boolean fechado) {

        FragmentDialogo novaacao = FragmentDialogo.newInstance("Enviar pedido", fechado);
        novaacao.show(getSupportFragmentManager(), "FechadoAviso");
    }

    private void FinanizarPedido() {
        if(pedidolista.size() > 0) {
            if (preferencesUsuario.getNome() == "") {
                LayoutNome.setVisibility(View.VISIBLE);
                nomeusuario.requestFocus();
            } else if (ActivityCompat.checkSelfPermission(CarrinhoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                items = null;
                new CarregaAsync().execute();
                listaCarrinho.setVisibility(View.GONE);
                LayoutBaixo.setVisibility(View.GONE);
                layoutLocal.setVisibility(View.GONE);
                LayoutBaixoEntrga.setVisibility(View.VISIBLE);
                LayoutTopEntrega.setVisibility(View.VISIBLE);
                LayoutTopo.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);

                String nome = preferencesUsuario.getNome();
                String[] splited = nome.split("\\s+");
                if (splited.length > 2) {
                    nome = splited[0] + " " + splited[1];
                }
                LegendaNome.setText(nome);
                //  LegendaNome.setText(preferencesUsuario.getNome());
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_out_right, R.anim.slide_in_right);
                ft.addToBackStack(null);
                ft.add(R.id.fragment_conteudo, fragmententrega, "FragEntrega");
                ft.commitAllowingStateLoss();

            } else {
                listaCarrinho.setVisibility(View.GONE);
                layoutLocal.setVisibility(View.VISIBLE);

            }
        }else{
            alertas.ExiberAlertaErro("Nenhum item no carrinho");
        }
    }

    private void PopularlistaView(List<PedidoItem> produtoslista) {
        adaptercarrinho = new adapterCarrinho(CarrinhoActivity.this, produtoslista);
        listaCarrinho.setAdapter(adaptercarrinho);
        adaptercarrinho.notifyDataSetChanged();
        pedidolista = produtoslista;

    }

    public void OnUsarEndereco(View view) {
      //  localizacao = false;
        endereco = true;
        listaCarrinho.setVisibility(View.GONE);
        LayoutBaixo.setVisibility(View.GONE);
        layoutLocal.setVisibility(View.GONE);
        LayoutBaixoEntrga.setVisibility(View.VISIBLE);
        LayoutTopEntrega.setVisibility(View.VISIBLE);
        LayoutTopo.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        String nome = preferencesUsuario.getNome();
        String[] splited = nome.split("\\s+");
        if(splited.length > 2){
            nome = splited[0]+" "+splited[1];
        }
        LegendaNome.setText(nome);
        UsarEnderecoOuMapa();
    }


    private class CarregaAsync extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            for (PedidoItem pedidoItem : pedidolista) {
                if (pedidoItem.getIngredientesItem().equalsIgnoreCase("null")) {

                    if (items != null) {
                        items = items.replace("]", "");
                        items = items + ", {\"tipo\":\"normal\", \"item\":\"" + pedidoItem.QuantidadeItem + " " + pedidoItem.getNomeItem() + " - R$ " + pedidoItem.getValorItem() + "\", \"id\":\"" + pedidoItem.getIDitem() + "\"}]";
                    } else {
                        items = "[{\"tipo\":\"normal\", \"item\":\"" + pedidoItem.QuantidadeItem + " " + pedidoItem.getNomeItem() + " - R$ " + pedidoItem.getValorItem() + "\", \"id\":\"" + pedidoItem.getIDitem() + "\"}]";
                    }

                } else {
                    if (items != null) {
                        items = items.replace("]", "");
                        items = items + ", {\"tipo\":\"personalizado\", \"item\":\"" + pedidoItem.QuantidadeItem + " " + pedidoItem.getNomeItem() + " - R$ " + pedidoItem.getValorItem() + "\", \"ingredientes\":\"" + pedidoItem.getIngredientesItem() + "\"}]";
                    } else {
                        items = "[{\"tipo\":\"personalizado\", \"item\":\"" + pedidoItem.QuantidadeItem + " " + pedidoItem.getNomeItem() + " - R$ " + pedidoItem.getValorItem() + "\", \"ingredientes\":\"" + pedidoItem.getIngredientesItem() + "\"}]";
                    }
                }
            }
            quantidadeitens = pedidolista.size();

            if (pedidolista.size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado) {
                Log.d("Valor", String.valueOf(valorTotal));
                Log.d("items", String.valueOf(items));
            } else {

            }

        }


    }

    public void eventFechar(View view) {
        finish();
    }

    private void alerta(String mensagem) {
        Toast.makeText(CarrinhoActivity.this, mensagem, Toast.LENGTH_LONG).show();
    }

    private void ApagarItem() {
        String jsonatual = produtoscarrinho.ProdutosCarrinho();
        BigDecimal total = new BigDecimal(pedidoItem.getValorTotalPedido().replace(",","."));
        String valor = "";
        //   produtoscarrinho.clearPedido();
        try {
            JSONArray PedidoJosn = new JSONArray(jsonatual);
            JSONObject valorjson = PedidoJosn.getJSONObject(selecao);
            valor = valorjson.getString("valor");
            PedidoJosn.remove(selecao);
            int quantatual = produtoscarrinho.QuantidadeItensPedido();
            quantatual--;
            produtoscarrinho.addProductCount(quantatual);
            produtoscarrinho.addProdutoCar(PedidoJosn.toString());
            adaptercarrinho.removerProduto(selecao);
            adaptercarrinho.notifyDataSetChanged();
            apagarItem.setVisibility(View.INVISIBLE);
            total = total.subtract(new BigDecimal(valor.replace(",",".")));
            pedidoItem.setValorTotalPedido(String.valueOf(total));

            atualizarTotal(total);
            alerta("Item apagado");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Erro", "ao apagar dados do json");
            alerta("Erro ao apagar item");
        }

    }

    private void atualizarTotal(BigDecimal pValorTotal) {
        if(pedidolista.size() > 0) {
            String valor = String.valueOf(pValorTotal);
            DecimalFormat decFormat = new DecimalFormat("#.00");
            valor = decFormat.format(pValorTotal);
            valorTotal = pValorTotal.doubleValue();
            pedidoItem.setValorTotalPedido(String.valueOf(valor));
            valor = "R$ " + valor.replace(".", ",");
            this.totalPedido.setText(String.valueOf(valor));
        }else{
            this.totalPedido.setText("R$ 0,00");
        }

    }

    public void eventEndereco(View view) {

    }

    public void OnPermitirLocal(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
    }

    public void eventEditarNome(View view) {
        if (LayoutNome.getVisibility() != View.VISIBLE) {
            nomeusuario.setText(preferencesUsuario.getNome());
            LayoutNome.setVisibility(View.VISIBLE);
            editar = true;
        } else {
            LayoutNome.setVisibility(View.GONE);
        }
    }

    @Override
    public void Local(double lat, double lng) {
        latitude = lat;
        longitude = lng;
        Log.d("Comunicação", String.valueOf(longitude));
    }

    @Override
    public void Endereco(String valor) {

    }

    @Override
    public void Frag(Boolean estado) {
        Frag = estado;
    }


    @Override
    public void onBackPressed() {
        if (Frag == true) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                    R.anim.slide_out_right, R.anim.slide_in_right);
            ft.remove(fragmententrega);
            ft.remove(enderecoFragment);
            ft.commit();

            frameLayout.setVisibility(View.GONE);
            listaCarrinho.setVisibility(View.VISIBLE);

            LayoutBaixo.setVisibility(View.VISIBLE);
            LayoutBaixoEntrga.setVisibility(View.GONE);

            LayoutTopo.setVisibility(View.VISIBLE);
            LayoutTopEntrega.setVisibility(View.GONE);
            Frag = false;
            localizacao = true;
            addendereco.setText("Endereço");
            confirmarLocal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_botao_marcador, 0);
            addendereco.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_endereco, 0, 0, 0);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FinanizarPedido();
            } else {
                layoutLocal.setVisibility(View.GONE);
                LayoutBaixo.setVisibility(View.GONE);
                LayoutBaixoEntrga.setVisibility(View.VISIBLE);
                LayoutTopEntrega.setVisibility(View.VISIBLE);
                LayoutTopo.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_in_left, R.anim.slide_out_left);
                //  ft.addToBackStack(null);
                ft.add(R.id.fragment_conteudo, enderecoFragment, "FragEntrega");
                ft.commitAllowingStateLoss();

            }
            return;
        }
    }
}
