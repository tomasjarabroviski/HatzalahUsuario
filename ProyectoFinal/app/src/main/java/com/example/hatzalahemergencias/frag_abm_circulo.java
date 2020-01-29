package com.example.hatzalahemergencias;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_abm_circulo extends Fragment implements View.OnClickListener {

    private ActividadPrincipal actividadPrincipal;
    private ListView ListaFamiliares;
    private AdaptadorFamiliares adapter;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<CirculoFamiliar> miCirculo = new ArrayList<CirculoFamiliar>();
    private List<UsuarioInsert> miUsuario = new ArrayList<UsuarioInsert>();
    private int idFamiliar;
    private int idCirculo;
    @Override
    public void onStart() {
        super.onStart();
        if (actividadPrincipal.getFamiliaresUsuario() != null) {
            Log.d("Familiares","Familiares size: "+actividadPrincipal.getFamiliaresUsuario().size());
            adapter = new AdaptadorFamiliares(actividadPrincipal.getFamiliaresUsuario(), getContext());
            ListaFamiliares.setAdapter(adapter);

        }
    }

    @Override
    public View onCreateView(LayoutInflater infladorDeLAyout, ViewGroup grupoDeVista, Bundle savedInstanceState) {
        View view;
        actividadPrincipal = (ActividadPrincipal) getActivity();
        view = infladorDeLAyout.inflate(R.layout.act_abm_circulo, grupoDeVista, false);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Button botonAgregar = view.findViewById(R.id.btnAgregarFamiliar);
        botonAgregar.setOnClickListener(this);

        ImageButton flechaAtras = view.findViewById(R.id.imgbAtrasListaFamiliares);
        flechaAtras.setOnClickListener(this);

        ListaFamiliares = view.findViewById(R.id.ListView_ListaDeFamiliares);
        ListaFamiliares.setOnItemClickListener(escuchadorParaListView);
        return view;
    }

    AdapterView.OnItemClickListener escuchadorParaListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Acceso API", "Posicion seleccionada:" + position);
            idFamiliar = actividadPrincipal.getFamiliaresUsuario().get(position).getIdUsuario();
            Log.d("Acceso API", "Usuario seleccionado:" + idFamiliar);
            AlertDialog.Builder mensaje;
            mensaje = new AlertDialog.Builder(getActivity());
            mensaje.setMessage("Puede Eliminar al Familiar oprimiendo el boton Eliminar");
            mensaje.setTitle("Eliminar Familiar");
            mensaje.setPositiveButton("Eliminar", escuchador);
            mensaje.setNegativeButton("Cancelar", escuchador);
            mensaje.create();
            mensaje.show();


        }


    };

    DialogInterface.OnClickListener escuchador = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int cualBotonTocado) {
            if (cualBotonTocado == -1){
                //Boton Positivo
                Log.d("Acceso API", "ELijio Eliminar");
                    getCirculoFamiliarPorIDiFamiliar();
            } else {
                //Boton Negativo
                Log.d("Acceso API", "ELijio Cancelar");
            }
        }
    };


    private void getCirculoFamiliarPorIDiFamiliar() {
        Call<List<CirculoFamiliar>> call = jsonPlaceHolderApi.getCirculofamiliarporIdyFamiliar(actividadPrincipal.getUsuario().getIdUsuario() ,idFamiliar);

        call.enqueue(new Callback<List<CirculoFamiliar>>()
        {
            @Override
            public void onResponse(Call<List<CirculoFamiliar>> call, Response<List<CirculoFamiliar>> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                miCirculo  = response.body();
                Log.d("Json", "Circulo encontrado familiar: " + miCirculo.get(0).getIdFamiliar());
                Log.d("Json", "Circulo encontrado usuario: " + miCirculo.get(0).getIdUsuario());

             idCirculo = miCirculo.get(0).getIdCirculoFamiliar();
             deleteCirculo();

            }

            @Override
            public void onFailure(Call<List<CirculoFamiliar>> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });

    }

    public void deleteCirculo() {
        Log.d("Json", "id: " + idCirculo);
        Call<List<UsuarioInsert>> call = jsonPlaceHolderApi.deleteDCirculo(idCirculo);
        call.enqueue(new Callback<List<UsuarioInsert>>()
        {
            @Override
            public void onResponse(Call<List<UsuarioInsert>> call, Response<List<UsuarioInsert>> response)
            {
                Log.d("Json", "Code: " + response.code());
                miUsuario =  response.body();
                Log.d("Json", "Usuario encontrado: " + miUsuario.get(0).getMailUsuario());
                Log.d("Json", "id encontrado: " + miUsuario.get(0).getIdUsuario());
                ArrayList<Usuario> Circulo =actividadPrincipal.getFamiliaresUsuario();
                int x=-1;
                if(Circulo.size()!=0) {
                    do{
                        x++;
                        Log.d("Json", "si: " + Circulo.get(x).getIdUsuario() + " El igual a: " +  miUsuario.get(0).getIdUsuario());
                        if (Circulo.get(x).getIdUsuario() == miUsuario.get(0).getIdUsuario()) {
                            Log.d("Json", "UBoy a elimianr al:  " + Circulo.get(x).getIdUsuario());
                            actividadPrincipal.getFamiliaresUsuario().remove(Circulo.get(x));
                        }
                    }while (x+1<Circulo.size()&&Circulo.get(x).getIdUsuario()!=miUsuario.get(0).getIdUsuario());
                }
                getFragmentManager().popBackStackImmediate();
            }


            @Override
            public void onFailure(Call<List<UsuarioInsert>> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }

    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAgregarFamiliar:
                    actividadPrincipal.CirculoDeEmergenciaFormulario();
                    break;
                case R.id.imgbAtrasListaFamiliares:
                    getFragmentManager().popBackStackImmediate();
                    break;
            }
        }

    }

