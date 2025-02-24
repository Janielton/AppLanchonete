package com.jhonjhon.lanches.paginas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.adapters.ExpandeAdapterPedidos;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.classes.FormataValores;
import com.jhonjhon.lanches.models.osPedidosFeitos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {
    TextView titulo;
    private ExpandableListView listViewEpan;
    private ExpandeAdapterPedidos listAdapter;
    private int lastExpandedPosition = -1;
    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    List<osPedidosFeitos> listapedidos;
    LinearLayout linearNoPedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        listViewEpan = (ExpandableListView) findViewById(R.id.ListaExpandePedidos);
        linearNoPedido =  (LinearLayout) findViewById(R.id.linearNoPedido);
        titulo = (TextView) findViewById(R.id.tvTitulo);
        titulo.setText("Pedidos feitos");
        CarregaPedidos();
    }

    private List<String> formataJson(String json){
        List<String> dados = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
         //   JSONObject dadoOBJ = jsonArray.getJSONObject(1);
         //   dados =  dadoOBJ.getString("item");
            JSONObject dadoOBJ = null;
            for(int i=0; i <jsonArray.length(); i++) {
                 dadoOBJ = jsonArray.getJSONObject(i);
                 dados.add(dadoOBJ.getString("item"));
            }

        } catch (JSONException e) {
           Log.e("JSONException", e.getMessage());
        }

        return dados;
    }
    private void CarregaPedidos() {
        final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(PedidosActivity.this));

        listapedidos = itemsDao.getListaPedidos();
        if(listapedidos.size() > 0){
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();
        FormataValores formataValores = new FormataValores();

        for (osPedidosFeitos data : listapedidos) {


            HashMap<String, String> mapParent = new HashMap<String, String>();
            mapParent.put("CATEGORY_ID", String.valueOf(data.getId()));
            mapParent.put("CATEGORY_NAME", "Pedido #" + String.valueOf(data.getId()));
            mapParent.put("DATA", data.getData());
            mapParent.put("VALOR", formataValores.ValorInterface(String.valueOf(data.getValorTotal())));

            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();


            List<String> items = formataJson(data.getItems());
            for (int j = 0; j < items.size(); j++) {
                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put("SUB_ID", String.valueOf(j));
                mapChild.put("SUB_CATEGORY_ITEMS", items.get(j));
                childArrayList.add(mapChild);
            }

            childItems.add(childArrayList);
            parentItems.add(mapParent);
        }

        listAdapter = new ExpandeAdapterPedidos(PedidosActivity.this, parentItems, childItems);
        listViewEpan.setAdapter(listAdapter);

        listViewEpan.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return false;
            }
        });

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
        }else{
            linearNoPedido.setVisibility(View.VISIBLE);
            listViewEpan.setVisibility(View.GONE);

        }

    }
    public void eventFechar(View view){
        finish();
    }
}