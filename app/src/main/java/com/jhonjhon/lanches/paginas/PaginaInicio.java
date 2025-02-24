package com.jhonjhon.lanches.paginas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import com.andremion.counterfab.CounterFab;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.adapters.AdpaterTabMenu;
import com.jhonjhon.lanches.classes.Alertas;
import com.jhonjhon.lanches.classes.CarrinhoPreferences;
import com.jhonjhon.lanches.classes.ConexaoSQL;
import com.jhonjhon.lanches.classes.FormataValores;
import com.jhonjhon.lanches.classes.DBOperador;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.classes.VerificaHorario;
import com.jhonjhon.lanches.fragments.DialogoAppMania;
import com.jhonjhon.lanches.fragments.FragmentDialogo;
import com.jhonjhon.lanches.intefaces.InicioComunica;
import com.jhonjhon.lanches.models.PedidoItem;
import com.jhonjhon.lanches.tabs.TabBebidas;
import com.jhonjhon.lanches.tabs.TabLanches;
import com.jhonjhon.lanches.tabs.TabPersonalizado;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class PaginaInicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InicioComunica {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toogle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem lanche, bebidas, personalizado;
    AdpaterTabMenu adpaterTabMenu;
    Boolean detalheLanche = false;
    Boolean detalheBebidas = false;
    private Button menuabre;
    private CounterFab BtCarrinho;
    public Button BotaoVoltar, BotaoFinalizar, BotaoAberto;
    PedidoItem pedidoItem;
    private String NomeItem, ValorItem, ImgItem;
    private int QuantidadeItem = 1;
    private int cartProductNumber = 0;
    FrameLayout layoutbaixo;
    LinearLayout BarraBaixo;
    Boolean isAberto = false;
    Alertas alerta;
    String TodoIngredientes = "null";
    int TabAtual;
    // OneSignal oneSignal = new OneSignal();
    String idItem = "";
    FormataValores formataValores = new FormataValores();
    PreferencesUsuario preferencesUsuario = new PreferencesUsuario(PaginaInicio.this);
    private CarrinhoPreferences addcarrinho = new CarrinhoPreferences(PaginaInicio.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio);
        Toolbar toolbar = findViewById(R.id.toolbarTabmenu);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        isAberto = bundle.getBoolean("aberto");
        Log.e("Aberto", String.valueOf(bundle.getBoolean("aberto")));



        if (preferencesUsuario.getIDDevice().isEmpty()) {
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            OneSignal.setSubscription(true);
            if (status != null) {
                preferencesUsuario.setIDDevice(status.getSubscriptionStatus().getUserId());
            }
        }
        // Log.d("ID ONE", preferencesUsuario.getIDDevice());


        this.setTitle("");
        pager = findViewById(R.id.viewPagerTab);
        mTabLayout = findViewById(R.id.tabLayout);

        lanche = findViewById(R.id.lanche);
        bebidas = findViewById(R.id.bebidas);
        personalizado = findViewById(R.id.personalizado);

        layoutbaixo = findViewById(R.id.layoutbaixo);
        BarraBaixo = findViewById(R.id.BarraBaixo);
        drawerLayout = findViewById(R.id.drawer_tab);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.setDrawerIndicatorEnabled(true);
        toogle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.corBranca));
        toogle.syncState();

        BotaoAberto = (Button) findViewById(R.id.BtAberto);
        BtCarrinho = (CounterFab) findViewById(R.id.BtCarrinho);


        BtCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrircarrinho = new Intent(PaginaInicio.this, CarrinhoActivity.class);
                startActivity(abrircarrinho);
            }
        });
        // addcarrinho.limparLanches();

        final DBOperador itemsDao = new DBOperador(ConexaoSQL.getInstance(PaginaInicio.this));
        Log.d("itemsDao", String.valueOf(itemsDao.getNumeroPedidos()));

        BotaoVoltar = (Button) findViewById(R.id.BtVoltar);
        BotaoFinalizar = (Button) findViewById(R.id.BtFinalizar);

        BotaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voltar();
            }
        });
        BotaoFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdicionarCarrinho();

            }
        });

        adpaterTabMenu = new AdpaterTabMenu(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        pager.setAdapter(adpaterTabMenu);
        pager.setOffscreenPageLimit(2);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                TabAtual = tab.getPosition();
                if (tab.getPosition() == 0) {
                    if (detalheLanche) {
                        BarraBaixo.setVisibility(View.VISIBLE);
                    }
                }
                if (tab.getPosition() == 1) {
                    if (detalheBebidas) {
                        BarraBaixo.setVisibility(View.VISIBLE);
                    }
                }
                if (tab.getPosition() == 2) {
                    if (TabPersonalizado.isPronto()) {
                        BarraBaixo.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                BarraBaixo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        alerta = new Alertas(this, false);
        VerificaHorario verificaHorario = new VerificaHorario(this, this);

        BotaoAberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAberto == false) {
                    showDialog(true);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (addcarrinho.QuantidadeItensPedido() > 0) {
            BtCarrinho.setCount(addcarrinho.QuantidadeItensPedido());
            BtCarrinho.setVisibility(View.VISIBLE);
        } else {
            BtCarrinho.setVisibility(View.GONE);
        }
        if (isAberto) {
            BotaoAberto.setText("Aberto");
            BotaoAberto.setTextColor(getResources().getColor(R.color.corBranca));
            BotaoAberto.setBackground(getResources().getDrawable(R.drawable.corner_butao_verde));
        }
    }

    private void showDialog(boolean status) {
        if(status) {
            FragmentDialogo novaacao = FragmentDialogo.newInstance("Estamos fechados", true);
            novaacao.show(getSupportFragmentManager(), "FechadoAviso");
        }else {
            DialogoAppMania novaacao = DialogoAppMania.newInstance();
            novaacao.show(getSupportFragmentManager(), "AppMania");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.pedidos) {
            Intent novaPagina = new Intent(PaginaInicio.this, PedidosActivity.class);
            startActivity(novaPagina);
        }
        if (item.getItemId() == R.id.onde) {
            Intent novaPagina = new Intent(PaginaInicio.this, OndeActivity.class);
            startActivity(novaPagina);
        }
        if (item.getItemId() == R.id.compartilhe) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

            //   ArrayList<Uri> tmpList = new ArrayList<>();
            //  tmpList.add(Uri.parse("android.resource://"+appPackageName+"/" + R.drawable.ic_app));
            //   ArrayList<String> textList = new ArrayList<>();
            //  textList.add("App oficial da Lanchonete Jhon Jhon - https://play.google.com/store/apps/details?id="+ appPackageName);
            String textList = "App oficial da Lanchonete Jhon Jhon - https://play.google.com/store/apps/details?id=" + appPackageName;
            Uri tmpList = Uri.parse("android.resource://" + appPackageName + "/" + R.drawable.ic_app);

            Intent acao = new Intent(Intent.ACTION_SEND);
            acao.setType("*/*");
            acao.putExtra(Intent.EXTRA_TEXT, textList);
            acao.putExtra(Intent.EXTRA_STREAM, tmpList);
            startActivity(Intent.createChooser(acao, "Compartilhar App com amigos..."));
        }
        if (item.getItemId() == R.id.avaliar) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
//
//            Intent acao = new Intent(Intent.ACTION_INSERT);
//            acao.putExtra(android.content.Intent.EXTRA_SUBJECT, "Product name");
//            acao.putExtra(android.content.Intent.EXTRA_TEXT, "market://details?id=com.jhonjhon.lanches");
//            startActivity(acao);
        }
        if (item.getItemId() == R.id.contato) {
            Intent novaPagina = new Intent(PaginaInicio.this, ContatoActivity.class);
            startActivity(novaPagina);
        }
        if (item.getItemId() == R.id.tutorial) {
            Intent novaPagina = new Intent(PaginaInicio.this, TutorialActivity.class);
            startActivity(novaPagina);
        }
        if (item.getItemId() == R.id.appmania) {
            showDialog(false);
        }
        return false;
    }

    public void FecharMenu(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void AcaoDetalhe(boolean lanche, boolean bebidas, String iditem) {
        if (iditem != null) {
            idItem = iditem;
        }
        if (TabAtual == 0) {
            detalheLanche = lanche;
            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_up));
            BarraBaixo.setVisibility(View.VISIBLE);
        }
        if (TabAtual == 1) {
            detalheBebidas = bebidas;
            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_up));
            BarraBaixo.setVisibility(View.VISIBLE);
        } else {
            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_up));
            BarraBaixo.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void MostraBarra(boolean mostra) {
        if (mostra) {
            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_up));
            BarraBaixo.setVisibility(View.VISIBLE);
        } else {
            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_down));
            BarraBaixo.setVisibility(View.GONE);
        }
    }

    @Override
    public void ProdutoAdd(String nome, String valor, String imagem, int quantidade, ArrayList<String> ingredientes) {
        NomeItem = nome;
        ImgItem = imagem;
        QuantidadeItem = quantidade;

        String novovalor = formataValores.TraformarPraDouble(Double.parseDouble(valor.replace(",", ".")));
        ValorItem = novovalor;

        if (ingredientes != null) {
            TodoIngredientes = "";
            for (int i = 1; i < ingredientes.size(); i++) {
                if (TodoIngredientes.isEmpty()) {
                    TodoIngredientes = ingredientes.get(i);
                } else {
                    TodoIngredientes = TodoIngredientes + ", " + ingredientes.get(i);
                }
            }
        } else {
            TodoIngredientes = "null";
        }
    }

    @Override
    public void onBackPressed() {
        if (TabAtual == 0) {
            finish();
        }
        if (TabAtual == 1) {

            pager.setCurrentItem(0);
        }
        if (TabAtual == 2) {
            pager.setCurrentItem(1);
        }
    }

    public void Voltar() {
        if (TabAtual == 0) {
            detalheLanche = false;
            TabLanches.LinearLista.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
            TabLanches.LinearLista.setVisibility(View.VISIBLE);

            TabLanches.FrameDetalhe.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_in_left, R.anim.fragment_out_left, R.anim.fragment_out_right, R.anim.fragment_in_right);
            ft.remove(getSupportFragmentManager().findFragmentByTag("FrameDetalhe"));
            ft.commit();

            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_down));
            BarraBaixo.setVisibility(View.INVISIBLE);

        } else if (TabAtual == 1) {
            detalheBebidas = false;
            TabBebidas.LinearLista.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
            TabBebidas.LinearLista.setVisibility(View.VISIBLE);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_in_left, R.anim.fragment_out_left, R.anim.fragment_out_right, R.anim.fragment_in_right);
            ft.remove(getSupportFragmentManager().findFragmentByTag("FrameDetalheBebidas"));
            ft.commitAllowingStateLoss();

            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_down));
            BarraBaixo.setVisibility(View.INVISIBLE);
        } else if (TabAtual == 2) {
            BarraBaixo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.barra_down));
            BarraBaixo.setVisibility(View.INVISIBLE);
        }

    }

    public void AdicionarCarrinho() {
        //   addcarrinho.LimparCarrinho();

        String dadosfinal = "";
        String productsCarrinho = addcarrinho.ProdutosCarrinho();
        int quantidade = addcarrinho.QuantidadeItensPedido();
        String itempedido = "[{\"nome\":\"" + NomeItem + "\",\"imagem\":\"" + ImgItem + "\",\"valor\":\"" + ValorItem + "\",\"quantidade\":" + QuantidadeItem + ", \"ingredientes\":\"" + TodoIngredientes + "\", \"id_item\":\"" + idItem + "\"}]";
        if (quantidade == 0) {
            addcarrinho.addProdutoCar(itempedido);
            cartProductNumber = 1;

        } else {
            String novoitempedido = "{\"nome\":\"" + NomeItem + "\",\"imagem\":\"" + ImgItem + "\",\"valor\":\"" + ValorItem + "\",\"quantidade\":" + QuantidadeItem + ", \"ingredientes\":\"" + TodoIngredientes + "\", \"id_item\":\"" + idItem + "\"}]";
            productsCarrinho = productsCarrinho.replace("]", ",");
            dadosfinal = productsCarrinho + "" + novoitempedido;
            cartProductNumber = addcarrinho.QuantidadeItensPedido();
            cartProductNumber++;
            addcarrinho.addProdutoCar(dadosfinal);

        }
        BtCarrinho.setVisibility(View.VISIBLE);
        BtCarrinho.setCount(addcarrinho.QuantidadeItensPedido());
        addcarrinho.addProductCount(cartProductNumber);
        BtCarrinho.increase();

        if (TabAtual == 2) {
            TabPersonalizado.BtOkAdd.callOnClick();
            TabPersonalizado.BtOkAdd.setText("Novo pedido");
            TabPersonalizado.setCount(1);
            int temp = addcarrinho.getPedidoPersonalizado();
            temp++;
            addcarrinho.setPedidoPersonalizado(temp);
        }
        Voltar();

    }

    private void AnimaFragment(View view) {
        int anim = R.anim.slide_out_right;
        Animation animation = AnimationUtils.loadAnimation(this, anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });
        view.startAnimation(animation);
    }
}