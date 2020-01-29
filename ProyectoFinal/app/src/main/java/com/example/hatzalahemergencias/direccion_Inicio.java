package com.example.hatzalahemergencias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class direccion_Inicio extends AppCompatActivity {
    int miIdUsuario;
    Bundle Resultado;
    FirebaseUser currentUser;
    ProgressDialog dialog;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    String direccion;
    double Lon;
    double Lat;
    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion__inicio);
        String apiKey = "AIzaSyAMDDnJln8IWhhatQoj2PkpCAGKmGssBEw";

        if (!Places.isInitialized()){
            Places.initialize(this,apiKey);
        }
        placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentPlace);


        if (autocompleteFragment != null){
            Log.d("APIMaps" , "No es null");
        } else {
            Log.d("APIMaps" , "es null");
        }
        // autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        Log.d("Algo",""+autocompleteFragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latlng = place.getLatLng();
                final String adr = place.getAddress();
                Log.d("APIMaps" , "Lugar: " + adr);
                Lat = latlng.latitude;
                Lon = latlng.longitude;
                direccion = adr;
                Log.d("APIMaps" , "onPlaceSelected: " + latlng.latitude +  "  Lon: " + latlng.longitude);
                EditText MiDireccion;
                MiDireccion = findViewById(R.id.textDireccion);
                MiDireccion.setText(adr);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });





        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        currentUser = getIntent().getParcelableExtra("Usuario");

        Resultado = this.getIntent().getExtras();
        miIdUsuario = Resultado.getInt("idUsuario");
        Log.d("Insercion ficha", "mi id esssasa: " + miIdUsuario);
    }

    public void guardari(View miVIsta) {
        dialog= ProgressDialog.show(this, "Cargando",
                "Cargando. porfavor espere...", true);
        dialog.show();
        EditText MiEntre1;
        MiEntre1 = findViewById(R.id.txtEntre1);
        EditText MiEntre2;
        MiEntre2 = findViewById(R.id.txtEntre2);
        EditText MiEtiquets;
        MiEtiquets = findViewById(R.id.txtEtiqueta);
        Direccion MiDireccion1 = new Direccion(miIdUsuario, direccion, MiEntre1.getText().toString(), MiEntre2.getText().toString(), MiEtiquets.getText().toString(), Lat, Lon);
        createDireccion(MiDireccion1);

    }


    private void mevoydeaquitambien() {
        Bundle paqueteDeDatos;
        paqueteDeDatos = new Bundle();
        paqueteDeDatos.putInt("idUsuario", miIdUsuario);

        Intent actividad = new Intent(this, ActividadPrincipal.class);
        actividad.putExtra("Usuario", currentUser);
        actividad.putExtras(paqueteDeDatos);
        startActivity(actividad);
        finish();
    }

    private void createDireccion(Direccion MiDireccion) {

        Call<Direccion> call = jsonPlaceHolderApi.createDireccion(MiDireccion);
        call.enqueue(new Callback<Direccion>() {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response) {
                if (!response.isSuccessful()) {
                    Log.d("Json", "Code1: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                Direccion direccion = response.body();
                Log.d("Json", "Se ha insertado la direccion:" + direccion.getDireccion() + " ,Entres: " + direccion.getEntre1() + " y " + direccion.getEntre2()+" Lon: "+direccion.getLon()+" Lat: "+direccion.getLat());
                dialog.cancel();
                mevoydeaquitambien();

            }


            @Override
            public void onFailure(Call<Direccion> call, Throwable t) {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }


    /*private class TareaAsincronica2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("Acceso API busqueda", "Mi direccion para recorrer el segundo Json es: " + direccion);
                URL miRuta = new URL("http://servicios.usig.buenosaires.gob.ar/normalizar/?direccion=" + direccion + ",caba&geocodificar=true");
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();
                Log.d("Acceso API busqueda", "Accedi a la API");

                if (miConexion.getResponseCode() == 200) {
                    Log.d("Acceso API busqueda", "Coneccion OK");
                    InputStream cuerpoResource = miConexion.getInputStream();
                    InputStreamReader LectorRespuesta = new InputStreamReader(cuerpoResource, "UTF-8");
                    procesarJSONleido2(LectorRespuesta);
                } else {
                    Log.d("Acceso API", "Error en la coneccion");
                }
                miConexion.disconnect();
            } catch (Exception Error) {
                Log.d("Acceso API", "Hubo un Error en la coneccion" + Error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Acceso API", "Estoy en el OnPost");


          //  if (Validas) {
            //    Validas = false;


                Log.d("Acceso API", "Llegue al onPostExecute del segundo JSon");

                Log.d("Acceso API", "Mi Lonnn es:  " + Lon);
                Log.d("Acceso API", "Mi Lattt es:  " + Lat);
                EditText MiEntre1;
                MiEntre1 = findViewById(R.id.txtEntre1);
                EditText MiEntre2;
                MiEntre2 = findViewById(R.id.txtEntre2);
                EditText MiEtiquets;
                MiEtiquets = findViewById(R.id.txtEtiqueta);
                Direccion MiDireccion1 = new Direccion(miIdUsuario, direccion, MiEntre1.getText().toString(), MiEntre2.getText().toString(), MiEtiquets.getText().toString(), Lat, Lon);
                createDireccion(MiDireccion1);
                Log.d("Acceso API", "TENGO MI LAT Y LON;");

           // } else {
             //   Toast toast = Toast.makeText(getApplicationContext(), "Direccion invalida", Toast.LENGTH_SHORT);
               // toast.setMargin(50, 50);
                //toast.show();
            //}
        }
    }
        private void procesarJSONleido2(InputStreamReader streamLeido) {
            JsonReader JsonLeido = new JsonReader(streamLeido);
            try {
                Log.d("Acceso API", "Estoy en el JSONnnnn");
                JsonLeido.beginObject();
                while (JsonLeido.hasNext()) {
                    if (JsonLeido.nextName().equals("direccionesNormalizadas")) {
                        Log.d("Acceso API", "Llegue a direccionesNormalizadas");
                        JsonLeido.beginArray();
                        while (JsonLeido.hasNext()) {
                            JsonLeido.beginObject();
                            while (JsonLeido.hasNext()) {
                                Log.d("Acceso API", "Llegue al has next");
                                if (JsonLeido.nextName().equals("coordenadas")) {
                                    Log.d("Acceso API", "Llegue a coordenadas");
                                    JsonLeido.beginObject();
                                    Log.d("Acceso API", "Llegue despues del begin Object");
                                    while (JsonLeido.hasNext()) {
                                        Log.d("Acceso API", "Estyo en el has next de coordenadas");
                                        if (JsonLeido.nextName().equals("y")) {
                                            Lon = JsonLeido.nextDouble();
                                            Log.d("Acceso API", "Mi Lon es:  " + Lon);
                                        } else {
                                            JsonLeido.skipValue();
                                        }
                                        if (JsonLeido.nextName().equals("x")) {
                                            Lat = JsonLeido.nextDouble();
                                            Log.d("Acceso API", "Mi Lat es:  " + Lat);
                                            Validas = true;
                                        } else {
                                            JsonLeido.skipValue();
                                        }
                                    }
                                    JsonLeido.endObject();

                                } else {
                                    JsonLeido.skipValue();
                                }
                            }
                            JsonLeido.endObject();

                        }
                        JsonLeido.endArray();

                    } else {
                        JsonLeido.skipValue();
                    }

                }
            } catch (Exception Error) {
                Log.d("Error", "Error: " + Error.getMessage());

            }
        }
*/
}




