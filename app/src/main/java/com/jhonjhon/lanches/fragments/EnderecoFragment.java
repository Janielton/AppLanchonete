package com.jhonjhon.lanches.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.Alertas;
import com.jhonjhon.lanches.classes.PreferencesUsuario;

import org.json.JSONException;
import org.json.JSONObject;

public class EnderecoFragment extends Fragment {
    private EditText Rua, Bairro, Numero, Referecencia;
    private Button salvar;
    private TextView TextTopo;
    private boolean endereco;
    private Spinner cidades;
    int indice = 0;
    int numero = 0;
    private String selecaocat, referencia = "";
    PreferencesUsuario preferencesUsuario;
    Alertas alertas;

    public EnderecoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertas = new Alertas(getContext(), true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_endereco, container, false);
        Rua = (EditText) view.findViewById(R.id.editTextRua);
        Bairro = (EditText) view.findViewById(R.id.editTextBairro);
        Numero = (EditText) view.findViewById(R.id.editTextNumero);
        Referecencia = (EditText) view.findViewById(R.id.editTextReferencia);
        TextTopo = (TextView) view.findViewById(R.id.TextTopo);
        preferencesUsuario = new PreferencesUsuario(getActivity());
        cidades = (Spinner) view.findViewById(R.id.spinnerCidades);


        salvar = (Button) view.findViewById(R.id.salvarEndereco);
        cidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                selecaocat = item.toString();
                indice = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalvarEndereco();
            }
        });

        VericaEndereco();

        return view;
    }

    private void VericaEndereco() {
        if (preferencesUsuario.getEndereco() != "") {
            String enderecoSon = preferencesUsuario.getEnderecoJson();

            try {
                JSONObject jsonObject = new JSONObject(enderecoSon);
                Rua.setText(jsonObject.getString("rua"));
                Bairro.setText(jsonObject.getString("bairro"));
                Numero.setText(jsonObject.getString("numero"));
                if(jsonObject.getString("referencia") != ""){
                    Referecencia.setText(jsonObject.getString("referencia"));
                }
                cidades.setSelection(Integer.parseInt(jsonObject.getString("idcidade")));
                salvar.setText("Atualizar");
                TextTopo.setText("Confirme seu endereço");
                Log.d("Teste", jsonObject.getString("cidade"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void SalvarEndereco() {
        if (ValidaForm() == true) {

            String enderecoSon = "{\"cidade\":\"" + selecaocat + "\",\"idcidade\":\"" + indice + "\",\"rua\":\"" + String.valueOf(Rua.getText()) + "\",\"bairro\":\"" + String.valueOf(Bairro.getText()) + "\",\"numero\":\"" + numero + "\",\"referencia\":\"" + referencia + "\"}";
            String endereco = selecaocat + " - " + String.valueOf(Rua.getText()) + ", " + String.valueOf(Bairro.getText()) + ", " + numero + ", Referência: " + referencia;

            preferencesUsuario.addEndereco(endereco, enderecoSon);
            salvar.setText("Atualizar");
            alertas.ExiberAlertaSucesso("Endereço salvo");

            Log.d("Cat", preferencesUsuario.getEndereco());
            Log.d("CatJson", preferencesUsuario.getEnderecoJson());

        } else {
            Log.d("Erro", "Acorreu algum erro");
        }
    }

    private boolean ValidaForm() {
        if (indice == 0) {
            alertas.ExiberAlertaErro("Escolha a cidade");
            return false;
        }
        if (Rua.getText().toString().isEmpty() == true) {
            alertas.ExiberAlertaErro("Digite sua Rua");
            Rua.requestFocus();
            return false;
        }
        if (Bairro.getText().toString().isEmpty() == true) {
            Bairro.requestFocus();
            alertas.ExiberAlertaErro("Digite o Bairro");
            return false;
        }
        if (Numero.getText().toString().isEmpty() == false) {
            numero = Integer.parseInt(Numero.getText().toString());
        }
        if (Referecencia.getText().toString().isEmpty() == false) {
            referencia = Referecencia.getText().toString();
        }

        return true;
    }

}