package com.example.hatzalahemergencias;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_abm_circulo_form extends Fragment implements View.OnClickListener {

    private ActividadPrincipal actividadPrincipal;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<UsuarioInsert> miUsuario = new ArrayList<>();
    List<CirculoFamiliar> miCirculo = new ArrayList<>();
    private EditText edtxDNI;
    private EditText edtxNombre;
    private EditText edtxApellido;
    Boolean Flag;
    Dialog dialog;
    private Button botonAgregar;
    @Override
    public View onCreateView(LayoutInflater infladorDeLAyout, ViewGroup grupoDeVista, Bundle savedInstanceState) {
        View view;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        actividadPrincipal=(ActividadPrincipal)getActivity();
        view = infladorDeLAyout.inflate(R.layout.act_abm_circulo_form, grupoDeVista, false);

        edtxDNI = view.findViewById(R.id.edtxRegistroEmail);
        edtxApellido = view.findViewById(R.id.edtxApellido);
        edtxNombre = view.findViewById(R.id.edtxNombre);

        botonAgregar=view.findViewById(R.id.btnGuardarFamiliar);
        botonAgregar.setOnClickListener(this);

        ImageButton flechaAtras=view.findViewById(R.id.imgbAtrasFormFamiliar);
        flechaAtras.setOnClickListener(this);

        Button buscarPorDni = view.findViewById(R.id.btnBuscar);
        buscarPorDni.setOnClickListener(this);

        return view;
    }

    private void getCirculosFamiliares() {
        Flag = false;
        Call<List<CirculoFamiliar>> call = jsonPlaceHolderApi.getCirculosFamiliaresporId(actividadPrincipal.getUsuario().getIdUsuario());
        call.enqueue(new Callback<List<CirculoFamiliar>>()
        {
            @Override
            public void onResponse(Call<List<CirculoFamiliar>> call, Response<List<CirculoFamiliar>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                miCirculo = response.body();
                if(miCirculo!=null&&!miCirculo.isEmpty()) {
                    Log.d("Json", "Usuario Encontrado: " + miCirculo.get(0).getIdFamiliar());
                    for (CirculoFamiliar milocoCirculo : miCirculo) {
                        if (milocoCirculo.getIdFamiliar() == miUsuario.get(0).getIdUsuario()) {
                            Flag = true;
                        }
                    }
                }
                if (miUsuario.get(0).getIdUsuario() == actividadPrincipal.getUsuario().getIdUsuario()) {
                    dialog.cancel();
                    edtxNombre.setText("");
                    edtxApellido.setText("");
                    edtxDNI.setText("");
                    Toast toast = Toast.makeText(getActivity(), "No te podes añadir a vos mismo", Toast.LENGTH_SHORT);
                    toast.setMargin(50, 50);
                    toast.show();
                } else {
                    if (Flag) {
                        dialog.cancel();
                        edtxNombre.setText("");
                        edtxApellido.setText("");
                        edtxDNI.setText("");
                        Toast toast = Toast.makeText(getActivity(), "Ya añadio a este usuario", Toast.LENGTH_SHORT);
                        toast.setMargin(50, 50);
                        toast.show();
                    } else {
                        createcirculo();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CirculoFamiliar>> call, Throwable t)
            {
                Log.d("Json", "Error en getCirculos: " + t.getMessage());
            }
        });

    }

    private void createcirculo(){
        CirculoFamiliar MiCirculo = new CirculoFamiliar(actividadPrincipal.getUsuario().getIdUsuario(),miUsuario.get(0).getIdUsuario(),"Verde");
        Call<List<Usuario>> call = jsonPlaceHolderApi.createCirculo(MiCirculo);
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                final List<Usuario> miUsuario = response.body();
                Log.d("Json", "Se ha insertado El circulo con id: " + actividadPrincipal.getUsuario().getIdUsuario());
                Log.d("Json", "Se ha insertado El circulo con id: " + miUsuario.get(0).getIdUsuario());
                Log.d("Json", "Se ha insertado El circulo con direccion: " + miUsuario.get(0).getDireccionesUsuario());

                Call<List<Direccion>> call1=jsonPlaceHolderApi.getDireccionesAbm(miUsuario.get(0).getIdUsuario());
                call1.enqueue(new Callback<List<Direccion>>() {
                    @Override
                    public void onResponse(Call<List<Direccion>> call, Response<List<Direccion>> response) {
                        if (!response.isSuccessful())
                        {
                            Log.d("Json", "Code: " + response.code());
                            return;
                        }
                        if(response.body()!=null){
                            ArrayList<Direccion> arrayList=new ArrayList<>(response.body());
                            miUsuario.get(0).setDireccionesUsuario(arrayList);
                        }else{
                            Log.d("Json","Direcciones vacias");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Direccion>> call, Throwable t) {
                        Log.d("Json","Error en direccion: "+t.getMessage());
                    }
                });

                actividadPrincipal.getFamiliaresUsuario().add(miUsuario.get(0));
                dialog.cancel();
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }

    private void getUsuarioUnoPorDNI(int dni) {
        Call<List<UsuarioInsert>> call = jsonPlaceHolderApi.ObtenerUsuarioPorDNI(dni);
        call.enqueue(new Callback<List<UsuarioInsert>>()
        {
            @Override
            public void onResponse(Call<List<UsuarioInsert>> call, Response<List<UsuarioInsert>> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                miUsuario  = response.body();
                if (miUsuario.size() == 0){
                    dialog.cancel();
                    edtxNombre.setText("");
                    edtxApellido.setText("");
                    edtxDNI.setText("");
                    Toast toast=Toast.makeText(getActivity(),"No se ha encontrado personas con ese DNI", Toast.LENGTH_SHORT);
                     toast.setMargin(50,50);
                    toast.show();

                }else {
                    dialog.cancel();
                    Log.d("Json", "Usuario Encontrado: " + miUsuario.get(0).getNombreUsuario());
                    edtxNombre.setText(miUsuario.get(0).getNombreUsuario());
                    edtxApellido.setText(miUsuario.get(0).getApellidoUsuario());

                    botonAgregar.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioInsert>> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGuardarFamiliar:
                Log.d("Json", "Me lo voy a guardar ");
                dialog= ProgressDialog.show(getActivity(),"Guardando",
                        "Guardando. porfavor espere...", true);
                dialog.show();
                getCirculosFamiliares();
                break;
            case R.id.imgbAtrasFormFamiliar:
                getFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnBuscar:
                if (edtxDNI.length() == 0){
                    Toast toast=Toast.makeText(getActivity(),"Debe ingresar algun DNI", Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                }else {
                    dialog= ProgressDialog.show(getActivity(),"Cargando",
                            "Cargando. porfavor espere...", true);
                    dialog.show();
                    getUsuarioUnoPorDNI(Integer.parseInt(edtxDNI.getText().toString()));

                }
                break;
        }
    }
}
