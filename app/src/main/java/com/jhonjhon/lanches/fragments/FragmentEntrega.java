package com.jhonjhon.lanches.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jhonjhon.lanches.R;
import com.jhonjhon.lanches.classes.PreferencesUsuario;
import com.jhonjhon.lanches.intefaces.InterCarrinho;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

public class FragmentEntrega extends Fragment implements OnMapReadyCallback {
    PreferencesUsuario preferencesUsuario;
    public String nome;
    private double latitude = -14.30772;
    private double longitude = -43.76646;
    SupportMapFragment suportMapFragment;
    FusedLocationProviderClient client;
    BitmapDescriptor myIcon;
    private GoogleMap mMap;
    InterCarrinho dadosactiv;
    private boolean endereco;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            dadosactiv = (InterCarrinho) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementar Comunica");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesUsuario = new PreferencesUsuario(getActivity());

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrega, container, false);
        FloatingActionButton atualizar = (FloatingActionButton) view.findViewById(R.id.atualizar);
        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  getLocal();
                getCurrentLocation();
            }
        });
        Drawable Marcador = getResources().getDrawable(R.drawable.ic_marcadar_casa);
        myIcon = getMarkerIconFromDrawable(Marcador);


        suportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
        getCurrentLocation();
        nome = preferencesUsuario.getNome();
        alert("Confirme o local de entrega");
        dadosactiv.Frag(true);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_SATELLITE);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions().position(latLng).title(nome).draggable(true).icon(myIcon);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        mMap.addMarker(options);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng carinhanha = new LatLng(latitude, longitude);
        //mMap.addMarker(new MarkerOptions().position(carinhanha).title("Aqui carinhanha"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(carinhanha, 15));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d("LocalInicial",   String.valueOf(marker.getPosition()));

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("LocalFinal", marker.getPosition().latitude + ", "+marker.getPosition().longitude);
                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                dadosactiv.Local(latitude, longitude);
            }
        });

    }
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        mMap.clear();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("Local", longitude + " | "+ latitude);
                        LatLng latLng = new LatLng(latitude, longitude);
                        MarkerOptions options = new MarkerOptions().position(latLng).title(nome).draggable(true).icon(myIcon);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                        mMap.addMarker(options);

                    }
                }

            });
        } else {
            alert("Sem permisão");
        }

    }
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void alert(String msg) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.parseColor("#FF5252"), PorterDuff.Mode.SRC_IN);
        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 200);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        toast.show();


    }


}