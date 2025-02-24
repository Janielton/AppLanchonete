package com.jhonjhon.lanches.paginas;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.classes.VerificaHorario;

import java.util.concurrent.TimeUnit;

public class CadastroActivity extends AppCompatActivity {
    Button btnAvancar, btnVerificar, btAlterar;
    String phoneNumber, otp;
    EditText etPhoneNumber, etOTP, edNome;
    TextView tvLegenda, tvLegendaDois;
    FirebaseAuth auth;
    LinearLayout primeiraEtapa, segundaEtapa;
    PreferencesUsuario preferencesUsuario;
    public static String celular = "";
    private String verificationCode;
    AlertDialog alertaDialog;
    VerificaHorario verificaHora;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        findViews();
        StartFirebaseLogin();

        verificaHora = new VerificaHorario(this, this);
    }

    private void findViews() {

        tvLegenda = findViewById(R.id.tvLegenda);
        tvLegendaDois = findViewById(R.id.tvLegendaDois);

        btnAvancar = findViewById(R.id.btAvancar);
        btnVerificar = findViewById(R.id.btVerificar);
        btAlterar = findViewById(R.id.btAlterar);

        primeiraEtapa = findViewById(R.id.primeiraEtapa);
        segundaEtapa = findViewById(R.id.segundaEtapa);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etOTP = findViewById(R.id.et_otp);
        edNome = findViewById(R.id.edNome);


        setupdata();
    }

    public void setupdata() {
        preferencesUsuario = new PreferencesUsuario(this);

        phoneNumber = String.valueOf(etPhoneNumber.getText());

        btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLegenda.setText("Digite seu nome e celular");
                btAlterar.setVisibility(View.GONE);
                tvLegendaDois.setVisibility(View.GONE);
                segundaEtapa.setVisibility(View.GONE);
                primeiraEtapa.setVisibility(View.VISIBLE);
            }
        });
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = String.valueOf(etPhoneNumber.getText());


                if (phoneNumber.length() == 0 || edNome.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Você precisa digitar nome e celular", Toast.LENGTH_SHORT).show();
                }
                else{

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CadastroActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
                    View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_dialog, (LinearLayout) findViewById(R.id.SheetEnviar));
                    Button BtEnviar = sheetview.findViewById(R.id.BtEnviar);
                    Button BtEditar = sheetview.findViewById(R.id.BtEditar);
                    TextView textNumero = sheetview.findViewById(R.id.numero);
                    final LinearLayout linearConteudo = sheetview.findViewById(R.id.linearConteudo);
                    final LinearLayout carregando = sheetview.findViewById(R.id.carregando);
                    final LinearLayout linearBotoes = sheetview.findViewById(R.id.linearBotoes);
                    textNumero.setText(phoneNumber);
                    BtEnviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            linearConteudo.setVisibility(View.GONE);
                            linearBotoes.setVisibility(View.GONE);
                            carregando.setVisibility(View.VISIBLE);
                            EnviarSMS();

                        }
                    });

                    alertDialog.setView(sheetview);
                    alertaDialog = alertDialog.show();

                    BtEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertaDialog.dismiss();
                        }
                    });

//                    AlertDialog.Builder janelaEscolha = new AlertDialog.Builder(NovaActivity.this);
//                    janelaEscolha.setTitle("Precisamos verifiar o número: " + etPhoneNumber.getText().toString() + "");
//                    janelaEscolha.setMessage("Esse número está correto ou dejeja edita-lo?");
//                    janelaEscolha.setNegativeButton("Editar", null);
//                    janelaEscolha.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            EnviarSMS();
//
//                        }
//                    });
//                    janelaEscolha.create().show();
                }
            }
        });
        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etOTP.getText().toString();
                if (otp.length() == 0) {
                    Toast.makeText(CadastroActivity.this, "Digite o código recebido", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    SigninWithPhone(credential);
                }
            }
        });
    }

    private void EnviarSMS(){

        celular = phoneNumber.contains("+55") ? phoneNumber : "+55"+phoneNumber;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                celular,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                CadastroActivity.this,        // Activity (for callback binding)
                mCallback);                      // OnVerificationStateChangedCallbacks

    }

    private void SigninWithPhone(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            preferencesUsuario.setPrimeiroAcesso(false);
                            verificaHora.AbreJanela();
                        } else {
                            Toast.makeText(CadastroActivity.this, "Código incorreto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                alertaDialog.dismiss();
                Toast.makeText(CadastroActivity.this, "Vericação completa", Toast.LENGTH_SHORT).show();
              //  Intent intent = new Intent(CadastroActivity.this, PaginaInicio.class);
                preferencesUsuario.addCelular(celular);
                preferencesUsuario.addNome(edNome.getText().toString());
                preferencesUsuario.setPrimeiroAcesso(false);
            //    startActivity(intent);
             //   finish();
                verificaHora.AbreJanela();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("FirebaseException", e.getMessage());
                Toast.makeText(CadastroActivity.this, "Vericação falhou: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                alertaDialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(CadastroActivity.this, "Código enviado", Toast.LENGTH_SHORT).show();
                tvLegenda.setText("Verificar número de celular");
                tvLegendaDois.setText("Aguardando para detectar um SMS enviado para o numero " + etPhoneNumber.getText().toString());
                btAlterar.setVisibility(View.VISIBLE);
                tvLegendaDois.setVisibility(View.VISIBLE);
                segundaEtapa.setVisibility(View.VISIBLE);
                primeiraEtapa.setVisibility(View.GONE);
                alertaDialog.dismiss();
            }
        };
    }


}
