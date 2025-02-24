package com.jhonjhon.lanches.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.Alertas;
import com.jhonjhon.lanches.classes.ConstantManager;
import com.jhonjhon.lanches.tabs.TabPersonalizado;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandeAdapterPedidos extends BaseExpandableListAdapter implements ExpandableListAdapter {
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
        ExpandeAdapterPedidos.count = count;
    }

    static int count = 1;
    TabPersonalizado fragment;



    private boolean Click = false;



    public ExpandeAdapterPedidos(Context context, ArrayList<HashMap<String, String>> parentItems,
                                 ArrayList<ArrayList<HashMap<String, String>>> childItems) {

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
    public long getGroupId(int group) {
        return group;
    }

    @Override
    public long getChildId(int group, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean b, View convertView, ViewGroup viewGroup) {
        final ViewHolderParent viewHolderParent;

        if (convertView == null) {


            convertView = inflater.inflate(R.layout.list_group_pedidos, null);

            viewHolderParent = new ViewHolderParent();
            viewHolderParent.tvNome = convertView.findViewById(R.id.lblListHeader);
            viewHolderParent.tvData = convertView.findViewById(R.id.data);
            viewHolderParent.tvValor = convertView.findViewById(R.id.lblValor);


            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }

        viewHolderParent.tvNome.setText(parentItems.get(groupPosition).get("CATEGORY_NAME"));
        viewHolderParent.tvData.setText(parentItems.get(groupPosition).get("DATA"));
        viewHolderParent.tvValor.setText(parentItems.get(groupPosition).get("VALOR"));


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {

        final ViewHolderChild viewHolderChild;
        child = childItems.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expande_items_pedido, null);

            viewHolderChild = new ViewHolderChild();

            viewHolderChild.nome = convertView.findViewById(R.id.lblListItens);
            viewHolderChild.layChild = convertView.findViewById(R.id.layChild);

            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }
        viewHolderChild.nome.setText(child.get("SUB_CATEGORY_ITEMS"));

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
        TextView tvNome, tvData, tvValor;

    }

    public class ViewHolderChild {
        LinearLayout layChild;
        TextView nome;
    }




}