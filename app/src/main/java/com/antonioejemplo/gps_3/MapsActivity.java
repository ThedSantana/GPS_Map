package com.antonioejemplo.gps_3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.widget.Toast.makeText;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener {

    private static final long TIEMPO_MIN = 10 * 1000; // 10 segundos
    private static final long DISTANCIA_MIN = 5; // 5 metros
    private static final String[] A = {"n/d", "preciso", "impreciso"};
    private static final String[] P = {"n/d", "bajo", "medio", "alto"};
    private static final String[] E = {"fuera de servicio",
            "temporalmente no disponible ", "disponible"};

    private static String LOGCAT;
    private LocationManager manejador;
    private String proveedor;

    private double latitud;
    private double longitud;
    //private double altitud;

    /////////////////////////////////
    private GoogleMap mMap;
    private ImageButton btnmapanormal,btnmapahibrido,btnmapaterreno,btnzoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnmapaterreno=(ImageButton)findViewById(R.id.btnterreno);
        btnmapahibrido=(ImageButton)findViewById(R.id.btnhibrido);
        btnmapanormal=(ImageButton)findViewById(R.id.btnmapanormal);
        btnzoom=(ImageButton)findViewById(R.id.btnzoom);


        btnmapaterreno.setOnClickListener(this);
        btnmapahibrido.setOnClickListener(this);
        btnmapanormal.setOnClickListener(this);
        btnzoom.setOnClickListener(this);


        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);



/*CRITERIOS PARA ELEGIR EL PROVEEDOR:SIN COSTE, QUE MUESTRE ALTITUD, Y QUE TENGA PRECISIÓN FINA. CON ESTOS
        * SERÁ ELEGIDO AUTOMÁTICAMENTE EL PROVEEDOR A UTILIZAR POR EL PROPIO TERMINAL*/
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        Log.v(LOGCAT, "Mejor proveedor: " + proveedor + "\n");
        Log.v(LOGCAT, "Comenzamos con la última localización conocida:");


        //CHEQUEAMOS LOS PERMISOS PARA ANDROID M
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            else{

                Location localizacion = manejador.getLastKnownLocation(proveedor);
            }

        }


        Location localizacion = manejador.getLastKnownLocation(proveedor);

        if(localizacion!=null) {
            muestraLocalizacion(localizacion);

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void muestraLocalizacion(Location localizacion) {
        if (localizacion == null)
            makeText(getApplicationContext(),"Localización desconocida", Toast.LENGTH_LONG).show();
        else
            makeText(getApplicationContext(),localizacion.toString(), Toast.LENGTH_LONG).show();

         latitud=localizacion.getLatitude();
         longitud=localizacion.getLongitude();

         //altitud=localizacion.getAltitude();


    }


    //Los botones deferencian los tres tipos de mapas al ser pulsados
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnmapanormal:
                //Tipo satélite sin zomm
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud),0));

                break;

            case R.id.btnhibrido:
                //Tipo híbrido sin zoom
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud),0));
                break;

            case R.id.btnterreno:
                //Tipo terreno(calles) sin zomm
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud),0));
                break;

            case R.id.btnzoom:
                //Poniendo un zomm alto por defecto
                //Google ha realizado también mapas de interiores de algunos edificios.Coordenadas,profundidad Se pone un ejemplo....
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud),15));



                break;


            default:
                break;


        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera...ESTO LO TRAE POR DEFECTO
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


        LatLng posicion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(posicion).title("Esta es tu posición actual"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));




        //Añadir marcador al hacer pulsación prolongad click. El icono del marcador puede ser modificado o poner uno por defecto....
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng posicion) {
                mMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconoquepongas...))
                .anchor(0.0f,1.0f)
                .position(posicion));
            }
        });


        //Sacar un toast al pulsador sobre un marcador
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                makeText(getApplicationContext(),"Se ha pulsado un marcador", Toast.LENGTH_LONG).show();

                return false;


            }
        });



    }//Fin OnReady



}//Fin clase
