package com.example.hatzalahemergencias;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class frag_abm_direcciones_form extends Fragment implements View.OnClickListener
{
    ProgressDialog dialog;
    int numus;
    View MyView;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    Button MiBoton;
    Button Eliminarbut;
    ImageButton butaTras;
    String direccion;
    Double Lat;
    double Lon;
    Boolean Validas = false;
    ActividadPrincipal actividadPrincipal;
    String EstadoABM;
    String DireccionParaUpdate;
    private ArrayList<String> MiLIsta;
    private ArrayList<String> Etiquetas;
    private List<Direccion> ListaDeDirecciones = new ArrayList<Direccion>();
    PlacesClient placesClient;

    @Override
    public View onCreateView(LayoutInflater infladosDeLayout, ViewGroup grupoDeVista, Bundle Datos) {
        MiLIsta = new ArrayList<>();
        Etiquetas = new ArrayList<>();
        actividadPrincipal=(ActividadPrincipal) getActivity();

        MyView = infladosDeLayout.inflate(R.layout.act_abm_direcciones_form, grupoDeVista, false);

        String apiKey = "AIzaSyAMDDnJln8IWhhatQoj2PkpCAGKmGssBEw";

        if (!Places.isInitialized()){
            Places.initialize(getContext(),apiKey);
        }
       placesClient = Places.createClient(getActivity());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.fragmentPlace);


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
                MiDireccion = MyView.findViewById(R.id.textDireccion);
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

        MiBoton = MyView.findViewById(R.id.btnGuardarDireccion);
        butaTras = MyView.findViewById(R.id.AtrasFormDIreccion);
        Eliminarbut = MyView.findViewById(R.id.btnEliminarDireccion);

        MiBoton.setOnClickListener(this);
        butaTras.setOnClickListener(this);
        Eliminarbut.setOnClickListener(this);

        EstadoABM = actividadPrincipal.getEstadoABM();
        Log.d("Json", "El estado de mi ABM es: " + EstadoABM);
        Log.d("Json", "El Id de mi DIrecciones es: " + actividadPrincipal.getDireccionPrincipal());
        if (EstadoABM.equals("Update")){
            dialog= ProgressDialog.show(getActivity(), "Cargando",
                    "Cargando. porfavor espere...", true);
            dialog.show();
            getDireccionUno(actividadPrincipal.getDireccionPrincipal());
            Eliminarbut.setVisibility(View.VISIBLE);
        }else{
            if(EstadoABM.equals("Insert")){
                Eliminarbut.setVisibility(View.INVISIBLE);
            }
        }


     return MyView;
    }

    private void ExisteDireccion() {
        Call<List<Direccion>> call = jsonPlaceHolderApi.getDireccionesAbm(actividadPrincipal.getUsuario().getIdUsuario());
        call.enqueue(new Callback<List<Direccion>>()
        {
            @Override
            public void onResponse(Call<List<Direccion>> call, Response<List<Direccion>> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }

                ListaDeDirecciones   = response.body();
                Log.d("Json", "Lista de direcciobes tiene: " + ListaDeDirecciones.size() + " items");
                //  ArrayList ArrayLista = new ArrayList(ListaDeDirecciones);
                for (Direccion milocadireccion:ListaDeDirecciones )
                {

                    Log.d("Json", "Mi Lat es: " + milocadireccion.getLat());

                    MiLIsta.add(milocadireccion.getDireccion());
                    Etiquetas.add(milocadireccion.getEtiqueta());
                    Log.d("Json", "Mi Direccion essss: " + MiLIsta.get(0));
                }
                int cont = 0;
                boolean Flag = false;
                Log.d("Json", "La direccion puesta por el usuario es : " + direccion);
                EditText MiEtiquets;
                MiEtiquets = MyView.findViewById(R.id.txtEtiqueta);
                for (String Misdirecciones:MiLIsta){
                    Log.d("Json", "Estoy en el elemento. " + Misdirecciones);
                    if (Misdirecciones.toLowerCase().equals(direccion.toLowerCase())){
                        Flag = true;
                        Log.d("Json", "Mi Direccion ya existe!");
                    }
                    EditText MiEtiqueta;
                    MiEtiqueta = MyView.findViewById(R.id.txtEtiqueta);
                    if (Etiquetas.get(cont).toLowerCase().equals(MiEtiqueta.getText().toString().toLowerCase())){
                        Flag = true;
                        Log.d("Json", "Mi Direccion ya existe!");
                    }
                    cont++;
                }
                if (Flag){
                    dialog.cancel();
                    Log.d("Json", "Voy a mostrar un mensaje de error");
                    Toast toast=Toast.makeText(getActivity(),"Direccion o etiqueta Ya Ingresada", Toast.LENGTH_SHORT);
                       toast.setMargin(50,50);
                     toast.show();
                     Flag = false;


                } else {
                    Flag = false;
                    Log.d("Json", "VEsta direccion no es igual me voy a hacerle un insert");
                    EditText MiEntre1;
                    MiEntre1 = MyView.findViewById(R.id.txtEntre1);
                    EditText MiEntre2;
                    MiEntre2 = MyView.findViewById(R.id.txtEntre2);
                    EditText MiEtiquetsa;
                    MiEtiquetsa = MyView.findViewById(R.id.txtEtiqueta);
                    Direccion MiDireccion1 = new Direccion(actividadPrincipal.getUsuario().getIdUsuario(), direccion, MiEntre1.getText().toString(), MiEntre2.getText().toString(), MiEtiquets.getText().toString(), Lat, Lon);
                    createDireccion(MiDireccion1);
                }


            }

            @Override
            public void onFailure(Call<List<Direccion>> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());

            }

        });
        Log.d("Json", "(2) Lista de direcciobes tiene: " + ListaDeDirecciones.size() + " items");
    }

    private void getDireccionUno(int idd) {
        Call<Direccion> call = jsonPlaceHolderApi.updateDireccion1(idd);
        call.enqueue(new Callback<Direccion>()
        {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                Direccion direccion = response.body();
                Log.d("Json", "Direccion Encontrado: " + direccion.getDireccion());
                EditText MiDireccion;
                MiDireccion = MyView.findViewById(R.id.textDireccion);
                EditText MiEntre1;
                MiEntre1 = MyView.findViewById(R.id.txtEntre1);
                EditText MiEntre2;
                MiEntre2 = MyView.findViewById(R.id.txtEntre2);
                EditText MiEtiqueta;
                MiEtiqueta = MyView.findViewById(R.id.txtEtiqueta);
                DireccionParaUpdate = direccion.getDireccion();
                MiDireccion.setText(direccion.getDireccion());
                MiEntre1.setText(direccion.getEntre1());
                MiEntre2.setText(direccion.getEntre2());
                MiEtiqueta.setText(direccion.getEtiqueta());
                Lon = direccion.getLon();
                Lat = direccion.getLat();
                dialog.cancel();


            }

            @Override
            public void onFailure(Call<Direccion> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());

            }
        });

    }

    public void onClick(View vistaRecibida) {
        switch (vistaRecibida.getId())
        {
            case R.id.btnGuardarDireccion:

                if (EstadoABM.equals("Insert")) {
                    dialog= ProgressDialog.show(getActivity(), "Cargando",
                            "Cargando. porfavor espere...", true);
                    dialog.show();
                    Log.d("Json", "ME LO VOY A GUARDAR");
                    EditText MiTexto;
                    MiTexto = MyView.findViewById(R.id.textDireccion);
                    direccion = MiTexto.getText().toString();

                    ExisteDireccion();

                } else {
                    EditText MiEntre1;
                    MiEntre1 = MyView.findViewById(R.id.txtEntre1);
                    EditText MiEntre2;
                    MiEntre2 = MyView.findViewById(R.id.txtEntre2);
                    EditText MiEtiqueta;
                    MiEtiqueta = MyView.findViewById(R.id.txtEtiqueta);
                    EditText MiDireccion;
                    MiDireccion = MyView.findViewById(R.id.textDireccion);
                    Direccion MiDireccionATR = new Direccion(actividadPrincipal.getUsuario().getIdUsuario(), direccion, MiEntre1.getText().toString(), MiEntre2.getText().toString(), MiEtiqueta.getText().toString(), Lat, Lon);
                    updateDireccion(MiDireccionATR);

                }

                break;
            case R.id.AtrasFormDIreccion:
                getFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnEliminarDireccion:
                 numus = actividadPrincipal.getDireccionPrincipal();
                dialog= ProgressDialog.show(getActivity(), "Cargando",
                        "Cargando. porfavor espere...", true);
                dialog.show();
                deleteDireccion(numus);
                break;
        }


    }


    private void updateDireccion(Direccion MiDireccion1) {

        int num = actividadPrincipal.getDireccionPrincipal();

        //Log.d("Json", "id Usuario: " + IdUsuarioanterior);
        //Log.d("Json", "id Usuario: " + direccionAnterior);
        //  Direcciones_abm MiDireccion1 = new Direcciones_abm(IdUsuarioanterior,direccionAnterior, MiEntre1.getText().toString(),"cambioentre2", "cambioEtiqueta", -56.234, -54.678);
        //  Direcciones_abm MiDireccion1 = new Direcciones_abm(3,"Cambio", "Cabioentre1","cambioentre2", "cambioEtiqueta", -56.234, -54.678);
        Call<Direccion> call = jsonPlaceHolderApi.updateDireccion2(num, MiDireccion1);

        call.enqueue(new Callback<Direccion>()
        {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Codeeee: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                Direccion direccion = response.body();
                Log.d("Json", "Se ha insertado la direccion:" + direccion.getDireccion() + " ,Entres: " + direccion.getEntre1() + " y " + direccion.getEntre2()
                        + "Con la etiqueta: " + direccion.getEtiqueta() + "En el usuario :" + direccion.getIdUsuario());
                Log.d("Json", "Code: " + "He logrado Insertar La Direccion");
                Usuario usuario=actividadPrincipal.getUsuario();
                ArrayList<Direccion> direcciones=usuario.getDireccionesUsuario();
                int x=0;
                if(direcciones.size()!=0) {
                    do {
                        if (direccion.getIdDirecciones() == direccion.getIdDirecciones()) {
                            direcciones.set(x, direccion);
                        } else {
                            x++;
                        }
                    }while (x < direcciones.size() && direcciones.get(x).getIdDirecciones() != direccion.getIdDirecciones());
                }
                dialog.cancel();
                getFragmentManager().popBackStackImmediate();
            }

            @Override
            public void onFailure(Call<Direccion> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });

    }

    private void createDireccion(Direccion MiDireccion) {

        Call<Direccion> call = jsonPlaceHolderApi.createDireccion(MiDireccion);
        call.enqueue(new Callback<Direccion>()
        {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code1: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                Direccion direccion = response.body();
                Log.d("Json", "Se ha insertado la direccion:" + direccion.getDireccion() + " ,Entres: " + direccion.getEntre1() + " y " + direccion.getEntre2());
                dialog.cancel();
                Usuario usuario=actividadPrincipal.getUsuario();
                ArrayList<Direccion> direcciones=usuario.getDireccionesUsuario();
                direcciones.add(direccion);
                getFragmentManager().popBackStackImmediate();

            }


            @Override
            public void onFailure(Call<Direccion> call, Throwable t) {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }

    private void deleteDireccion(int idDireccionDelete) {
        Call<Void> call = jsonPlaceHolderApi.deleteDireccion(idDireccionDelete);
        call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)

            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Codeeee: " + response.code());
                    return;
                }
                dialog.cancel();
                getFragmentManager().popBackStackImmediate();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }



}
