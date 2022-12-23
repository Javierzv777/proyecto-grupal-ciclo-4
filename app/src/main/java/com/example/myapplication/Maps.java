package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class Maps extends AppCompatActivity {
    private MapView map;
    private MapController mapController;
    private String imagen;
    private String nombre;
    private String descripcion;
    private Button volver;
    private String metodo;
    private String id;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        volver = findViewById(R.id.volver);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        mapController = (MapController) map.getController();
        GeoPoint colombia = new GeoPoint(6.749000,-75.387980);
        mapController.setCenter(colombia);
        mapController.setZoom(6);
        map.setMultiTouchControls(true);
       // Context context = getApplicationContext();
        Intent intentIn = getIntent();
        String consulta = intentIn.getStringExtra("consulta");
        imagen = intentIn.getStringExtra("imagen");
        nombre = intentIn.getStringExtra("nombre");
        descripcion = intentIn.getStringExtra("descripcion");
        descripcion = intentIn.getStringExtra("descripcion");
        metodo =  intentIn.getStringExtra("metodo");
        id =  intentIn.getStringExtra("id");
        uuid =  intentIn.getStringExtra("uuid");



        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
        if (consulta != null){
            MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {
                    Intent intent = new Intent(getApplicationContext(), Crud.class);
                    String latitud = String.valueOf(p.getLatitude());
                    String longitude = String.valueOf(p.getLongitude());
                    intent.putExtra("latitud", latitud);
                    intent.putExtra("longitud",  longitude);
                    intent.putExtra("imagen", imagen);
                    intent.putExtra("nombre",  nombre);
                    intent.putExtra("descripcion",  descripcion);
                    intent.putExtra("metodo",  metodo);
                    intent.putExtra("id",  id);
                    intent.putExtra("uuid",  uuid);


                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean longPressHelper(GeoPoint p) {
                    return false;
                }
            };
            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, mapEventsReceiver);
            map.getOverlays().add(mapEventsOverlay);
        } else {
                   Intent intent = getIntent();
                   Double latitud = Double.parseDouble(intent.getStringExtra("latitud"));
                   Double longitud = Double.parseDouble(intent.getStringExtra("longitud"));

                   if(latitud != 0 && longitud != 0) {
                       GeoPoint geoPoint = new GeoPoint(latitud, longitud);
                       mapController.setCenter(geoPoint);
                       mapController.setZoom(8);
                       Marker marker = new Marker(map);
                       marker.setPosition(geoPoint);
                       marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                       map.getOverlays().add(marker);
                   }

        }

    }
}