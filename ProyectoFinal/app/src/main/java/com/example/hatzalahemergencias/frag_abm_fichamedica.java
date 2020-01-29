package com.example.hatzalahemergencias;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_abm_fichamedica extends Fragment implements View.OnClickListener{
    EditText Medicacion;
    EditText Alergias;
    ProgressDialog dialog;
    View MiVista;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    List<FichaMedica> miFIcha = new ArrayList<FichaMedica>();
    ActividadPrincipal actividadPrincipal;
    Spinner spnOpciones;
    Button Guardar;
    int idFichaMedica;
    public View onCreateView(LayoutInflater infladorDeLayout,  ViewGroup grupoDeVista, @Nullable Bundle datos) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        dialog= ProgressDialog.show(getActivity(), "Cargando",
                "Cargando. porfavor espere...", true);
        dialog.show();

        MiVista=infladorDeLayout.inflate(R.layout.activity_form_ficha_medica, grupoDeVista,false);

        Medicacion = MiVista.findViewById(R.id.edtxNombre);
        Alergias = MiVista.findViewById(R.id.edtxApellido);

        Guardar = MiVista.findViewById(R.id.btnGuardarFichaMedica);
        Guardar.setOnClickListener(this);

        actividadPrincipal=(ActividadPrincipal) getActivity();

        ArrayList<String> datosLista;
        datosLista = new ArrayList<>();
        datosLista.add("O+");
        datosLista.add("O-");
        datosLista.add("A+");
        datosLista.add("A-");
        datosLista.add("B+");
        datosLista.add("B-");
        datosLista.add("AB+");
        datosLista.add("AB-");
        datosLista.add("Por ahora no lo recuerdo");
        ArrayAdapter<String> Adaptador;
        Adaptador = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, datosLista);
        Adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spnOpciones=(Spinner)MiVista.findViewById(R.id.spinnerDirecciones);
        spnOpciones.setAdapter(Adaptador);
        Log.d("Json", "Estoy en el onPre execute de la ficha medica");
        getFichaMedicaPorID();
        return MiVista;
    }

    private void getFichaMedicaPorID()
    {
        Log.d("Json", "Estoy llenando la ficha medica");
        Call<List<FichaMedica>> call = jsonPlaceHolderApi.getFiechaMedicaporId(actividadPrincipal.getUsuario().getIdUsuario());
        call.enqueue(new Callback<List<FichaMedica>>()
        {
            @Override
            public void onResponse(Call<List<FichaMedica>> call, Response<List<FichaMedica>> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    Log.d("Json", "Codeee: " + response.code());
                    return;
                }
                miFIcha  = response.body();
                Log.d("Json", "Usuario Encontrado: " + miFIcha.get(0).getTipoDeSangre());
                Medicacion.setText(miFIcha.get(0).getMed());
                Alergias.setText(miFIcha.get(0).getAlergias());
                idFichaMedica = miFIcha.get(0).getIdFicha();
                switch (miFIcha.get(0).getTipoDeSangre()) {
                    case "O+":
                        spnOpciones.setSelection(0);
                        break;
                    case "O-":
                        spnOpciones.setSelection(1);
                        break;
                    case "A+":
                        spnOpciones.setSelection(2);
                        break;
                    case "A-":
                        spnOpciones.setSelection(3);
                        break;
                    case "B+":
                        spnOpciones.setSelection(4);
                        break;
                    case "B-":
                        spnOpciones.setSelection(5);
                        break;
                    case "AB+":
                        spnOpciones.setSelection(6);
                        break;
                    case "AB-":
                        spnOpciones.setSelection(7);
                        break;
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(Call<List<FichaMedica>> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());

            }
        });

    }
    private void updateFichaMedica()
    {
        int posicion;
        posicion = spnOpciones.getSelectedItemPosition();
        if (Medicacion.getText().toString().equals("")){
            Medicacion.setText("No");
        }
        if (Alergias.getText().toString().equals("")){
            Alergias.setText("No");
        }
        FichaMedica MiFichaMedica = new FichaMedica(actividadPrincipal.getUsuario().getIdUsuario(),spnOpciones.getItemAtPosition(posicion).toString(), Alergias.getText().toString(),Medicacion.getText().toString());
        Call<FichaMedica> call = jsonPlaceHolderApi.updateFichaMedica(idFichaMedica,MiFichaMedica);
        call.enqueue(new Callback<FichaMedica>()
        {
            @Override
            public void onResponse(Call<FichaMedica> call, Response<FichaMedica> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Codeeee: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                FichaMedica MiFicha = response.body();
                Log.d("Json", "Se ha insertado El usuario:" + MiFicha.getAlergias());
                dialog.cancel();
                getFragmentManager().popBackStackImmediate();
            }

            @Override
            public void onFailure(Call<FichaMedica> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuardarFichaMedica:
                dialog= ProgressDialog.show(getActivity(), "Cargando",
                        "Cargando. porfavor espere...", true);
                dialog.show();
                updateFichaMedica();
                break;

        }
    }
}
