package com.jhonjhon.lanches.tabs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.adapters.adapterLanches;
import com.jhonjhon.lanches.adapters.expandeAdapter;
import com.jhonjhon.lanches.classes.Alertas;
import com.jhonjhon.lanches.classes.CarrinhoPreferences;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.ConstantManager;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.classes.FormataValores;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.intefaces.InicioComunica;
import com.jhonjhon.lanches.intefaces.iRetroService;
import com.jhonjhon.lanches.models.IngreModel;
import com.jhonjhon.lanches.models.Ingredientes;
import com.jhonjhon.lanches.models.RecordsIngre;



import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabPersonalizado extends Fragment {
    public String nome;
    public ListView listViewSelecao;
    public TextView ValortotralAdd;
    List<RecordsIngre> listasnova = new ArrayList<>();
    List<IngreModel> listaIngre = new ArrayList<>();
    PreferencesUsuario preferencesUsuario;
    CarrinhoPreferences carrinhoPreferences;
    private expandeAdapter listAdapter;
    private ExpandableListView listViewEpan;
    public BigDecimal ValorTotal;
    public String valor;
    LinearLayout LinerAcrescenta, LinearPerson;
    public static Button BtOkAdd;
    public static boolean pronto = false;
    InicioComunica dadosactiv;
    private int lastExpandedPosition = -1;
    private ArrayList<IngreModel> arCategory;
    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    Alertas alertas;
    ArrayList<String> ingredientes;
    FormataValores formataValores;
    CardView CardPedido;
    Button mais, menos;
    public static EditText quantiadeEdit;
    String valorPronto;
    TextView PersonaText;
    ProgressBar processoBar;
    public static int pedidoPersonalizado;
    static int quantPerso = 1;

    public static int getCount() {
        return quantPerso;
    }

    public static void setCount(int counte) {
        TabPersonalizado.quantPerso = counte;
    }



    public static boolean isPronto() {
        return pronto;
    }

    public void setPronto(boolean pronto) {
        this.pronto = pronto;
    }

    public BigDecimal getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        ValorTotal = valorTotal;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            dadosactiv = (InicioComunica) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementar Comunica");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_personalizado, container, false);
        ValortotralAdd = (TextView) view.findViewById(R.id.ValortotralAdd);
        preferencesUsuario = new PreferencesUsuario(getActivity().getApplicationContext());
        carrinhoPreferences = new CarrinhoPreferences(getActivity().getApplicationContext());
        listViewEpan = (ExpandableListView) view.findViewById(R.id.lvExp);
        listViewSelecao = (ListView) view.findViewById(R.id.listSelecao);
        processoBar = (ProgressBar) view.findViewById(R.id.processoBar);

        ValortotralAdd = (TextView) view.findViewById(R.id.ValortotralAdd);
        LinerAcrescenta = (LinearLayout) view.findViewById(R.id.LinerAcrescenta);
        LinearPerson = (LinearLayout) view.findViewById(R.id.LinearPerson);
        CardPedido = (CardView) view.findViewById(R.id.CardPedido);
        BtOkAdd = (Button) view.findViewById(R.id.BtOkAdd);
        mais = (Button) view.findViewById(R.id.buttonMais);
        menos = (Button) view.findViewById(R.id.buttonMenos);
        quantiadeEdit = (EditText) view.findViewById(R.id.editTextQuantidade);
        PersonaText = (TextView) view.findViewById(R.id.PersonaText);

        alertas = new Alertas(getActivity().getApplicationContext(), true);
        formataValores = new FormataValores();
      //  carrinhoPreferences.setPedidoPersonalizado(0);
       // preferencesUsuario.limparDados();
        pedidoPersonalizado = carrinhoPreferences.getPedidoPersonalizado();

        listViewEpan.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listViewEpan.collapseGroup(lastExpandedPosition);

                }
                lastExpandedPosition = groupPosition;
            }


        });

        listViewEpan.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
              //  listAdapter.Expande();
                return false;
            }
        });

        listViewEpan.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        BtOkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandeAdapter.getCount() > 2){
                if(BtOkAdd.getText().toString().equalsIgnoreCase("Cancelar")){
                    listViewEpan.setVisibility(View.VISIBLE);
                    listViewSelecao.setVisibility(View.GONE);
                    BtOkAdd.setText("Pronto");
                    dadosactiv.MostraBarra(false);
                    setValorTotal(new BigDecimal(valorPronto));
                    ValortotralAdd.setText(formataValores.ValorInterface(valorPronto));
                    PersonaText.setVisibility(View.GONE);
                    quantiadeEdit.setText("1");
                    setPronto(false);
                }else if(BtOkAdd.getText().toString().equalsIgnoreCase("Pronto")){
                    setPronto(true);
                    PedidoPronto();
                }else{
                    Recarrega();
                }
            }else{
                    int sele = 3 - expandeAdapter.getCount();
                    alertas.ExiberAlertaErro("Você precisa escolher pelo menos mais " + String.valueOf(sele)+" ingredientes");
                }
            }
        });
        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(quantiadeEdit.getText().toString());
                count++;

                quantiadeEdit.setText(String.valueOf(count));
                Operacao(true, valorPronto);
                dadosactiv.ProdutoAdd("Personalizado #"+ String.valueOf(pedidoPersonalizado),
                        getValorTotal().toString(),
                        "persona",
                        count,
                        ingredientes);
            }
        });
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(quantiadeEdit.getText().toString());
                if (quant != 1) {
                    quant--;
                    setCount(quant);
                    quantiadeEdit.setText(String.valueOf(quant));
                    Operacao(false, valorPronto);
                    dadosactiv.ProdutoAdd("Personalizado #"+ String.valueOf(pedidoPersonalizado),
                            getValorTotal().toString(),
                            "persona",
                            quant,
                            ingredientes);
                }


            }
        });

        return view;
    }

    public void Expande(int posicao, boolean expande) {
        if(expande) {
            listViewEpan.expandGroup(posicao, true);
        }else{
            listViewEpan.collapseGroup(posicao);
        }
    }

    public void Operacao(boolean soma, String valor){
        FormataValores formataValores = new FormataValores();
        if(soma){
            setValorTotal(formataValores.ValorMais(valor, getValorTotal()));
            ValortotralAdd.setText(formataValores.ValorInterface(String.valueOf(getValorTotal())));
        }else{
            setValorTotal(formataValores.ValorMenos(valor, getValorTotal()));
            ValortotralAdd.setText(formataValores.ValorInterface(String.valueOf(getValorTotal())));
        }

    }

    private void CarregaIngre(List<IngreModel> listaIngrediente) {


        arCategory = new ArrayList<>();

        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        IngreModel dataItem2 = null;
        for (IngreModel listaIngre: listaIngrediente){
            dataItem2 = new IngreModel();
            dataItem2.setId(listaIngre.getId());
            dataItem2.setNome(listaIngre.getNome());
            dataItem2.setValor(listaIngre.getValor());
            arCategory.add(dataItem2);
        }


        for (IngreModel data : arCategory) {
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID, String.valueOf(data.getId()));
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getNome());
            mapParent.put(ConstantManager.Parameter.CATEGORY_VALOR, data.getValor());
            if(data.getId() == 1) {
                mapParent.put(ConstantManager.Parameter.IS_CHECKED, "YES");
                mapParent.put(ConstantManager.Parameter.CATEGORY_QT, "1");
                FormataValores valores = new FormataValores();
                ValortotralAdd.setText(valores.ValorInterface(data.getValor()));
                setValorTotal(new BigDecimal(data.getValor()));
            }else{
                mapParent.put(ConstantManager.Parameter.IS_CHECKED, "NO");
                mapParent.put(ConstantManager.Parameter.CATEGORY_QT, "0");
            }
            HashMap<String, String> mapChild = new HashMap<String, String>();
            mapChild.put(ConstantManager.Parameter.SUB_ID, "1");
            mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, "sub");

            childArrayList.add(mapChild);

            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
       ConstantManager.childItems = childItems;

        listAdapter = new expandeAdapter(getContext(), parentItems, childItems, this);
        listViewEpan.setAdapter(listAdapter);
        processoBar.setVisibility(View.GONE);
        listViewEpan.setVisibility(View.VISIBLE);
    }
    private void CarregaProdutos(){
        iRetroService pegadados = iRetroService.retrofit.create(iRetroService.class);
        final Call<Ingredientes> call = pegadados.getIngredientes();
        call.enqueue(new Callback<Ingredientes>() {

            @Override
            public void onResponse(Call<Ingredientes> call, Response<Ingredientes> response) {

                if(response.code() == 200) {
                    listasnova = response.body().getResult();
                    new SalvaAsync().execute();
                }
            }
            @Override
            public void onFailure(Call<Ingredientes> call, Throwable t) {

            }

        });
    }
    public void PedidoPronto(){
        List<IngreModel> listaIngrediente = new ArrayList<>();
        IngreModel ingreModel = null;
        for (int i = 0; i < listAdapter.parentItems.size(); i++ ){

            String isChildChecked = listAdapter.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);

            if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
            {
                ingreModel = new IngreModel();
                ingreModel.setNome(listAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
                ingreModel.setValor(listAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_VALOR));
                ingreModel.setQuantidade(listAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_QT));
                listaIngrediente.add(ingreModel);

            }

        }
        if(pedidoPersonalizado != 1){
            pedidoPersonalizado++;
            PersonaText.setText("Personalizado #"+String.valueOf(pedidoPersonalizado));
        }
        LinearPerson.setVisibility(View.VISIBLE);
        PopularLista(listaIngrediente);

    }


    public void PopularLista(List<IngreModel> listaIngrediente){ ingredientes = new ArrayList<String>();
            if(listaIngrediente.size() > 0){
                for (IngreModel ingreModel:listaIngrediente){
              //      Log.d("Nome", ingreModel.getNome());
              //      Log.d("Quant", ingreModel.getQuantidade());
                    ingredientes.add(ingreModel.getNome()+" ("+ingreModel.getQuantidade()+")");
                }
            }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.simple_lista, ingredientes);

        listViewSelecao.setAdapter(arrayAdapter);
        listViewSelecao.setVisibility(View.VISIBLE);
        listViewEpan.setVisibility(View.GONE);
        LinerAcrescenta.setVisibility(View.VISIBLE);
        dadosactiv.MostraBarra(true);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) CardPedido.getLayoutParams();
        layoutParams.setMargins(10, 10, 10, 170);
        CardPedido.requestLayout();
        BtOkAdd.setText("Cancelar");
        valorPronto = getValorTotal().toString();

        dadosactiv.ProdutoAdd("Personalizado #"+ String.valueOf(pedidoPersonalizado),
                getValorTotal().toString(),
                "persona",
                quantPerso,
                ingredientes);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isPronto() && listaIngre.size() == 0) {
            final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(getActivity().getApplicationContext()));
            if (preferencesUsuario.isIngreAtualizado() == false) {
                Log.d("false", "teste");
                CarregaProdutos();
            } else {
                listaIngre = itemsDao.getListaIngre();
                CarregaIngre(listaIngre);
            }
        }
        if(BtOkAdd.getText().toString().equalsIgnoreCase("novo pedido")){
            BtOkAdd.callOnClick();
        }
    }
    public void Recarrega(){
        CarregaIngre(listaIngre);
        listViewEpan.setVisibility(View.VISIBLE);
        listViewSelecao.setVisibility(View.GONE);
        BtOkAdd.setText("Pronto");
        expandeAdapter.setCount(1);
        setPronto(false);
    }

    private class SalvaAsync extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            //PreferencesUsuario preferencesUsuario = new PreferencesUsuario(context);
            //  preferencesUsuario.limparDados();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(getActivity().getApplicationContext()));
            IngreModel dataItem = new IngreModel();
            dataItem.setNome("Pão");
            dataItem.setValor("1.00");
            long itensquant2 = itemsDao.salvarIngredientesDB(dataItem);

            IngreModel lista = null;
            long itensquant = 0;

             if(listasnova.size() >0) {
                    for (RecordsIngre pedidoItem : listasnova) {
                        lista = new IngreModel();
                        lista.setNome(pedidoItem.getFildes().getNome());
                        lista.setValor(pedidoItem.getFildes().getValor());

                        itensquant = itemsDao.salvarIngredientesDB(lista);

                    }

                    if (itensquant > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado) {
                preferencesUsuario.setIngreAtualizado(true);
                final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(getActivity().getApplicationContext()));
                Log.d("setIngreAtualizado:", "Dados salvos DB");
                listaIngre = itemsDao.getListaIngre();
                CarregaIngre(listaIngre);

            }

        }
    }



}
