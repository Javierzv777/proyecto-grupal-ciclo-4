package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        mapController = (MapController) map.getController();
        GeoPoint colombia = new GeoPoint(33.749000,-84.387980);
        mapController.setCenter(colombia);
        mapController.setZoom(10);
        map.setMultiTouchControls(true);
       // Context context = getApplicationContext();
        Intent intentIn = getIntent();
        String consulta = intentIn.getStringExtra("consulta");
        imagen = intentIn.getStringExtra("imagen");
        nombre = intentIn.getStringExtra("nombre");
        descripcion = intentIn.getStringExtra("descripcion");


        if (consulta.compareTo("consulta")==0){
            MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {
                    Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                    String latitud = String.valueOf(p.getLatitude());
                    String longitude = String.valueOf(p.getLongitude());
                    intent.putExtra("latitud", latitud);
                    intent.putExtra("longitud",  longitude);
                    intent.putExtra("imagen", imagen);
                    intent.putExtra("nombre",  nombre);
                    intent.putExtra("descripcion",  descripcion);


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
                       Toast.makeText(this, "ijaaaaa", Toast.LENGTH_SHORT).show();
                       GeoPoint geoPoint = new GeoPoint(latitud, longitud);
                       Marker marker = new Marker(map);
                       marker.setPosition(geoPoint);
                       marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                       map.getOverlays().add(marker);
                   }










        }

    }
}