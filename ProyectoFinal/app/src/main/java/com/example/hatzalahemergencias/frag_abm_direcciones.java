package com.example.hatzalahemergencias;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_abm_direcciones extends Fragment implements View.OnClickListener
{
    private ProgressDialog dialog;
    private Button butAgregar;
    private ImageButton butaTras;
    private ListView MyList;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ArrayAdapter<String> MiAdaptador;
    private ArrayList<String> MiLIsta;
    private List<Direccion> ListaDeDirecciones = new ArrayList<>();
    private View MyView;
    private ActividadPrincipal actividadPrincipal;
    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle Datos)
    {
        dialog= ProgressDialog.show(getActivity(), "Cargando",
                "Cargando. porfavor espere...", true);
        dialog.show();
        actividadPrincipal=(ActividadPrincipal) getActivity();
        MyView = infladorDeLayout.inflate(R.layout.act_abm_direcciones, grupoDeVista, false);
        MiLIsta = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        butAgregar = MyView.findViewById(R.id.btnAgregarFamiliar);
        butaTras = MyView.findViewById(R.id.imgbAtrasListaDirecciones);

        butAgregar.setOnClickListener(this);
        butaTras.setOnClickListener(this);

        getDirecciones();

        return MyView;


    }
    AdapterView.OnItemClickListener escuchadorParaListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Acceso API", "Posicion seleccionada:" + position);
            Log.d("Acceso API", "Elemento seleccinado:" + ListaDeDirecciones.get(position).getIdDirecciones());
            actividadPrincipal.setIdDireccionPrincipal(ListaDeDirecciones.get(position).getIdDirecciones());
            actividadPrincipal.setEstadoABM("Update");
            actividadPrincipal.IrAlFormDeDirecciones();
        }
    };







    public void onClick(View vistaRecibida) {

        Log.d("Json", "Mi ID que llego es: " + vistaRecibida.getId());

        // getFragmentManager().popBackStackImmediate();

        switch (vistaRecibida.getId()){
            case R.id.btnAgregarFamiliar:
                Log.d("Json", "ME LO VOY A GUARDAR");
                actividadPrincipal.setEstadoABM("Insert");
                actividadPrincipal.IrAlFormDeDirecciones();
                break;
            case R.id.imgbAtrasListaDirecciones:
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }

    private void getDirecciones() {
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
                    Log.d("Json", "Mi Direccion essss: " + MiLIsta.get(0));
                }
                MiAdaptador = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, MiLIsta);

                MyList = MyView.findViewById(R.id.ListView_ListaDeDirecciones);
                MyList.setAdapter(MiAdaptador);
                MyList.setOnItemClickListener(escuchadorParaListView);
                dialog.cancel();
            }

            @Override
            public void onFailure(Call<List<Direccion>> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());

            }

        });
        Log.d("Json", "(2) Lista de direcciobes tiene: " + ListaDeDirecciones.size() + " items");
    }
}
