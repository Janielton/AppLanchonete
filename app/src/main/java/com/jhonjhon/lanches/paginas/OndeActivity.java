package com.jhonjhon.lanches.paginas;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jhonjhon.lanches.R;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

public class OndeActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView titulo;
    private double latitude = -14.30772;
    private double longitude = -43.76646;
    SupportMapFragment suportMapFragment;
    FusedLocationProviderClient client;
    BitmapDescriptor myIcon;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onde);
        titulo = (TextView) findViewById(R.id.tvTitulo);
        titulo.setText("Onde estamos");
        Drawable Marcador = getResources().getDrawable(R.drawable.ic_marcador_onde);
        myIcon = getMarkerIconFromDrawable(Marcador);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        suportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_SATELLITE);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions().position(latLng)
                .title(getResources().getString(R.string.app_name))
                .snippet("R. Treze de Maio, Centro")
                .draggable(false).icon(myIcon);
        mMap.addMarker(options);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

    }
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void eventFechar(View view){
        finish();
    }
}