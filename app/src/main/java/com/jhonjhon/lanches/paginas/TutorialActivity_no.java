package com.jhonjhon.lanches.paginas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.jhonjhon.lanches.R;


public class TutorialActivity extends YouTubeBaseActivity {
    TextView titulo;
    Button btAbrirLink;
    YouTubePlayerView youtubePlayer;
    private YouTubePlayer player;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayer.OnFullscreenListener onFullListener;
    Toolbar toolbar;
    String IDvideo = "4NRu1wwxQZ0";
    String ApiKey = "chave";
    boolean FullScreem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        titulo = (TextView) findViewById(R.id.tvTitulo);
        btAbrirLink = (Button) findViewById(R.id.btAbrirLink);
        youtubePlayer = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        toolbar = (Toolbar) findViewById(R.id.toolbarTitle);

        titulo.setText("Como usar o App");
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(IDvideo);
                youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                youTubePlayer.setOnFullscreenListener(onFullListener);
                player = youTubePlayer;

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("Erro", youTubeInitializationResult.toString());

                btAbrirLink.setVisibility(View.VISIBLE);
             //   youtubePlayer.setVisibility(View.GONE);
            }
        };

        onFullListener = new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                FullScreem = b;
            }
        };

        youtubePlayer.initialize(ApiKey, onInitializedListener);

        btAbrirLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v="+IDvideo)));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (FullScreem) {
            FullScreem = false;
            player.setFullscreen(false);

        } else {
            finish();
        }
    }

    public void eventFechar(View view) {
        finish();
    }
}