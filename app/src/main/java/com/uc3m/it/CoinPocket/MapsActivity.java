package com.uc3m.it.CoinPocket;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
//NOTA: Se dejan comentarios de parte del codigo usado proveniente del material subido por los profesores yq ue para esta activity se ha reutilizado
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;//Creacion del mapa
    private Location localizacion;//Variable usada para manejar la localizacion que se tenga y sobre la que se varia en caso de cambiar
    private String mensaje ="UBICACIÓN ACTUAL";
    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Boton que nos devolvera a la activity anterior con la latitud y longitud asignadas
        Button devolverLocalizacion = (Button)findViewById(R.id.id_button_location);

        // Comprobamos si tenemos permiso para acceder a la localización
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("APLICACIÓN: Tenemos permisos...");
        } else {
            // no tiene permiso, solicitarlo
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            // cuando se nos conceda el permiso se llamará a onRequestPermissionsResult()
        }

        //Accedemos al servicio de localización
        LocationManager servicioLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la lista de proveedores disponibles (activos)
        boolean soloActivos = true;
        List<String> proveedores = servicioLoc.getProviders(soloActivos);

        //actualizarTextoProveedores(proveedores);
        if (proveedores.isEmpty()) { // No hay ninguno activo y no se puede hacer nada
            return;
        }

        // Vemos si está disponible el proveedor de localización que queremos usar
        String proveedorElegido = LocationManager.GPS_PROVIDER;
        boolean disponible = proveedores.contains(proveedorElegido);

        // Otra opción es utilizar uno cualquiera de la lista (por ejemplo, el primero)
        if (!disponible) {
            proveedorElegido = proveedores.get(0);
        }

        //Pedimos la última localización conocida por el proveedor
        localizacion = servicioLoc.getLastKnownLocation(proveedorElegido);
        //Tiempo mínimo entre escuchas de nueva posición
        int tiempo = 1000; //milisegundos
        //Distancia mínima entre escuchas de nueva posición
        int distancia = 10; //metros

        //Pedimos escuchar actualizaciones de posicion que reciba el proveedor elegido, cada
        //1000ms o 100m, que serán procesadas por el escuchador (implementado en esta misma clase)
        servicioLoc.requestLocationUpdates(proveedorElegido, tiempo, distancia, this);
        //Se añade el mapa a la activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Asignamos el mapa al de google creado en el inicio
        mMap = googleMap;
        //Escuchador para los clicks sobre el mapa a la hora de seleccionar ubucacion
        mMap.setOnMapClickListener(mClickListener);
        //Configuraciones A la hroa de cargar el mapa
        UiSettings settings = mMap.getUiSettings();

        settings.setZoomControlsEnabled(true); // botones para hacer zoom

        settings.setCompassEnabled(true); // brújula (sólo se muestra el icono si se rota el mapa con los dedos)
        //Obtenemos la ubiccion que se encuenta en la locaalizacion para situar el marcador al arrancarlo
        double latitud = localizacion.getLatitude();
        double longitud = localizacion.getLongitude();

        //Introduciomos la latitud y la longitud para manejarla con una variable tipo latlng
        LatLng coordenadas = new LatLng(latitud, longitud);


        int zoomlevel = 17; // nivel de zoom (1: mundo, 5: continente,  10: ciudad,  15: calle,  20: edificios)

        CameraPosition camPos= new CameraPosition.Builder()
                .target(coordenadas)//Centramos el mapa en la ubicacion donde nos encontramos
                .zoom(zoomlevel)//Establecemos el zoom en 17 al igual que cuando asignamos otro para no notar cambio
                .build();
        CameraUpdate camUpd  = CameraUpdateFactory.newCameraPosition(camPos);
        //Situamos la camara del mapa donde las coordenadas
        mMap.moveCamera(camUpd);
        //Se añade marcadro de manera automatica cuando cargamos el mapa
        mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title(mensaje))
                .showInfoWindow(); // si no se hace ésto sólo muestra mensaje cuando se pulsa marcador

        // Comprobamos si tenemos permiso para acceder a la localización
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); // botón "My Location"
        } else {
            // no tiene permiso, solicitarlo
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            // cuando se nos conceda el permiso se llamará a onRequestPermissionsResult()
        }

    }

    private final GoogleMap.OnMapClickListener mClickListener = new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(LatLng position) {

            // Eliminamos cualquier marcador sobre el mapa antes de posicionar el nuevo
            mMap.clear();
            //Al tocar sobre el mapa cargamso la localizacion de donde hemos hecho click
            localizacion.setLatitude(position.latitude);
            localizacion.setLongitude(position.longitude);

            // Recibimos las coordenadas del punto del mapa que pulsó el usuario.
            // Para realizar la concatenación de texto forma eficiente, usaremos un objeto de la clase StringBuffer:
            StringBuffer text = new StringBuffer();
            text.append("GPS: ");
            text.append(localizacion.getLatitude()).append(", ").append(localizacion.getLongitude());
            String coordText = text.toString();

            // Ponemos un marcador en dicha posición.
            // Para esto creamos un objeto MarkerOptions, al que le damos los detalles del nuevo marcador.
            // Después lo añadimos al mapa.
            // Más información sobre marcadores en:
            // https://developers.google.com/maps/documentation/android/marker
            MarkerOptions markerOpts = new MarkerOptions();
            markerOpts.position(position); // ubicación en el mapa (único requisito imprescindible)

            markerOpts.title("UBICACIÓN SELECCIONADA"); // título
            markerOpts.snippet(coordText); // texto complementario al título
            //Añadimos el marcador
            Marker marker = mMap.addMarker(markerOpts);
            //Centramos el mapa en el lugar seleccionado
            centerMap(position.latitude, position.longitude);
        }

    };

    public void centerMap(double latitude, double longitude){

        // A partir de una pareja de coordenadas (tipo double) creamos un objeto LatLng,
        // que es el tipo de dato que debemos usar al tratar con mapas
        LatLng position = new LatLng(latitude, longitude);

        // Obtenemos un objeto CameraUpdate que indique el movimiento de cámara que queremos;
        // en este caso, centrar el mapa en unas coordenadas con el método newLatLng()
        CameraUpdate update;

        // Alternativamente, se puede hacer lo mismo a la vez que se cambia el nivel de zoom
        // (comentar si se desea evitar el zoom)
        float zoom = 17;
        update = CameraUpdateFactory.newLatLngZoom(position, zoom);

        // Más información sobre distintos movimientos de cámara aquí:
        // http://developer.android.com/reference/com/google/android/gms/maps/CameraUpdateFactory.html

        // Pasamos el tipo de actualización configurada al método del mapa que mueve la cámara
        //Se encuentra comentado dado que la primera vez siempre falla, posteriormente si que funciona correctamenmte
        //No se averigua el motivo, el error salta porque dice apuntar a un null en el mapa que quiere centrar (solo en la primera ocasion)

        //mMap.moveCamera(update);

    }
    //Metodo atraves del cual se devuelven a la activity anterior la latitud y la longitud con el fin de ser usadas por el geocoder
    public void devolverLocalizacion(View view) {


        double lati = localizacion.getLatitude();
        double longi = localizacion.getLongitude();

        Intent result = new Intent();
        result.putExtra("LATITUD", lati);
        result.putExtra("LONGITUD",longi);


        setResult(RESULT_OK, result);
        finish();
    }



    @Override
    public void onLocationChanged(Location location) {
        //En caso de cambiar la localizacion vuelve a centrar el mapa
        centerMap(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onProviderDisabled(String provider) {
        // Lo dejamos vacío porque no queremos realizar ninguna acción
        //cuando el proveedor provider se desactiva
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Lo dejamos vacío porque no queremos realizar ninguna acción
        //cuando el proveedor provider se activa
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Lo dejamos vacío porque no queremos realizar ninguna acción
        //cuando el proveedor provider cambia de estado
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Puesto que la aplicación va a dejar de correr en el sistema, nos desuscribimos de las
        //actualizaciones
        LocationManager servicioLoc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        servicioLoc.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    finish();
                    startActivity(new Intent(this, this.getClass()));

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
