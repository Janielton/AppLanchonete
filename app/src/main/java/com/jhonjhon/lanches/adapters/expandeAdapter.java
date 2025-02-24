package com.jhonjhon.lanches.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;


import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.Alertas;
import com.jhonjhon.lanches.classes.ConstantManager;
import com.jhonjhon.lanches.models.PedidoItem;
import com.jhonjhon.lanches.tabs.TabPersonalizado;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class expandeAdapter extends BaseExpandableListAdapter implements ExpandableListAdapter {
    public static ArrayList<ArrayList<HashMap<String, String>>> childItems;
    public static ArrayList<HashMap<String, String>> parentItems;
    //    private final ArrayList<HashMap<String, String>> childItems;
    private LayoutInflater inflater;
    private Context activity;
    private HashMap<String, String> child;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        expandeAdapter.count = count;
    }

    static int count = 1;
    TabPersonalizado fragment;



    private boolean Click = false;



    public expandeAdapter(Context context, ArrayList<HashMap<String, String>> parentItems,
                          ArrayList<ArrayList<HashMap<String, String>>> childItems, TabPersonalizado fragment) {

        this.parentItems = parentItems;
        this.childItems = childItems;
        this.activity = context;
        this.fragment = fragment;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean b, View convertView, ViewGroup viewGroup) {
        final ViewHolderParent viewHolderParent;
        if (convertView == null) {


            convertView = inflater.inflate(R.layout.list_group_expande, null);

            viewHolderParent = new ViewHolderParent();
            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.lblListHeader);
            viewHolderParent.cbCategory = convertView.findViewById(R.id.cbCategory);


            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }

        ConstantManager.childItems = childItems;
        ConstantManager.parentItems = parentItems;

        viewHolderParent.tvMainCategoryName.setText(parentItems.get(groupPosition).get(ConstantManager.Parameter.CATEGORY_NAME));

        if (parentItems.get(groupPosition).get(ConstantManager.Parameter.IS_CHECKED).equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
            viewHolderParent.cbCategory.setChecked(true);
            notifyDataSetChanged();
        } else {
            viewHolderParent.cbCategory.setChecked(false);
            notifyDataSetChanged();
        }

        viewHolderParent.cbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChildChekBox(groupPosition);
                AcaoCheck(groupPosition);

            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {

        final ViewHolderChild viewHolderChild;
        child = childItems.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandelist_item, null);

            viewHolderChild = new ViewHolderChild();
            viewHolderChild.mais = convertView.findViewById(R.id.buttonMais);
            viewHolderChild.menos = convertView.findViewById(R.id.buttonMenos);
            viewHolderChild.quantidade = convertView.findViewById(R.id.editTextQuantidade);
            viewHolderChild.layChild = convertView.findViewById(R.id.layChild);

            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

         viewHolderChild.quantidade.setText(parentItems.get(groupPosition).get(ConstantManager.Parameter.CATEGORY_QT));
         notifyDataSetChanged();


        viewHolderChild.mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorcampo = viewHolderChild.quantidade.getText().toString();
                if(!valorcampo.isEmpty()){
                int quant = Integer.parseInt(valorcampo);
                if(quant == 0){
                    parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
                    notifyDataSetChanged();
                }
                quant++;
                valorcampo = String.valueOf(quant);
                viewHolderChild.quantidade.setText(valorcampo);
                parentItems.get(groupPosition).put(ConstantManager.Parameter.CATEGORY_QT, valorcampo);

                notifyDataSetChanged();
                AcaoMais(groupPosition);

                }
            }
        });
        viewHolderChild.menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(viewHolderChild.quantidade.getText().toString());
                if (quant != 1 && quant != 0) {
                    quant--;
                    viewHolderChild.quantidade.setText(String.valueOf(quant));
                    parentItems.get(groupPosition).put(ConstantManager.Parameter.CATEGORY_QT, String.valueOf(quant));

                    notifyDataSetChanged();
                    AcaoMenos(groupPosition);

                }


            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    private class ViewHolderParent {
        TextView tvMainCategoryName;
        CheckBox cbCategory;
    }

    public class ViewHolderChild {
        LinearLayout layChild;
        Button mais, menos;
        EditText quantidade;
    }

    public void UmCheck() {
        if (Click == false) {
            childItems.get(0).get(0).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
            childItems.get(0).get(1).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
            childItems.get(0).get(2).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
            notifyDataSetChanged();
            Click = true;
        }
    }


    public void AcaoCheck(int groupPosicao){
        String Checado = parentItems.get(groupPosicao).get(ConstantManager.Parameter.IS_CHECKED);

        if (Checado == "YES" && groupPosicao != 0) {
            fragment.Expande(groupPosicao, true);
            String valor = parentItems.get(groupPosicao).get(ConstantManager.Parameter.CATEGORY_VALOR);
            fragment.Operacao(true, valor);

        } else if (groupPosicao == 0) {

        } else {
            fragment.Expande(groupPosicao, false);
            String valor = parentItems.get(groupPosicao).get(ConstantManager.Parameter.CATEGORY_VALOR);
            String qt = parentItems.get(groupPosicao).get(ConstantManager.Parameter.CATEGORY_QT);
            if(qt != null ){
                int qttemp = Integer.parseInt(qt);
                if(qttemp > 1){
                    Double valortemp = Double.parseDouble(valor);
                    valortemp = qttemp * valortemp;
                    valor = String.valueOf(valortemp);
                }
            }
            parentItems.get(groupPosicao).put(ConstantManager.Parameter.CATEGORY_QT, "0");
            fragment.Operacao(false, valor);

        }
    }

    public void AcaoMais(int groupPosicao){
        String valor = parentItems.get(groupPosicao).get(ConstantManager.Parameter.CATEGORY_VALOR);
        fragment.Operacao(true, valor);
        int coun = getCount();
        coun++;
        setCount(coun);


    }
    public void AcaoMenos(int groupPosicao){
        String qt = parentItems.get(groupPosicao).get(ConstantManager.Parameter.CATEGORY_VALOR);
        fragment.Operacao(false, qt);
        int coun = getCount();
        coun--;
        setCount(coun);

    }

    public void Alerta(String msg, boolean sucesso) {
        Alertas alertas = new Alertas(activity, false);
        if (sucesso) {
            alertas.ExiberAlertaSucesso(msg);
        } else {
            alertas.ExiberAlertaErro(msg);
        }

    }

    public void ChildChekBox(int groupPosition) {

        String Checado = parentItems.get(groupPosition).get(ConstantManager.Parameter.IS_CHECKED);
        if (Checado == "NO") {
            parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
            parentItems.get(groupPosition).put(ConstantManager.Parameter.CATEGORY_QT, "1");
            notifyDataSetChanged();
            int coun = getCount();
            coun++;
            setCount(coun);;

        } else if (groupPosition == 0) {
            parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
            notifyDataSetChanged();

        } else {
            parentItems.get(groupPosition).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
            notifyDataSetChanged();
            int coun = getCount();
            coun--;
            setCount(coun);
        }

        ConstantManager.childItems = childItems;
        ConstantManager.parentItems = parentItems;

    }


}