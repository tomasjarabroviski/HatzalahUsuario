package com.example.hatzalahemergencias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class form_ficha_medica extends Activity {
    int miIdUsuario;
    ProgressDialog dialog;
    Bundle Resultado;
    FirebaseUser currentUser;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ficha_medica);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        currentUser=getIntent().getParcelableExtra("Usuario");

        Resultado =this.getIntent().getExtras();
        miIdUsuario = Resultado.getInt("idUsuario");
        Log.d("Insercion ficha", "mi id es: " + miIdUsuario);
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
        Adaptador = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, datosLista);
        Adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner spnOpciones;
        spnOpciones=(Spinner)findViewById(R.id.spinnerDirecciones);
        spnOpciones.setAdapter(Adaptador);



    }

    public void guardarficha(View miVista)
    {

        dialog= ProgressDialog.show(this, "Cargando",
                "Cargando. porfavor espere...", true);
        dialog.show();
        EditText MiMedicacion;
        MiMedicacion = findViewById(R.id.edtxNombre);
        EditText MisAlergias;
        MisAlergias = findViewById(R.id.edtxApellido);
        Spinner spnOpciones;
        spnOpciones=(Spinner)findViewById(R.id.spinnerDirecciones);

        int posisicon;
        posisicon = spnOpciones.getSelectedItemPosition();
        Log.d("Insercion ficha", "me voy a guardar la ficha");

        if (MiMedicacion.getText().toString().equals("")){
            MiMedicacion.setText("No");
        }
        if (MisAlergias.getText().toString().equals("")){
            MisAlergias.setText("No");
        }
        FichaMedica MiFichaMedica1 = new FichaMedica(miIdUsuario,spnOpciones.getItemAtPosition(posisicon).toString(),MisAlergias.getText().toString(), MiMedicacion.getText().toString());
        createFicha(MiFichaMedica1);
    }

    private void createFicha(FichaMedica MiFIcha1){
        Call<FichaMedica> call = jsonPlaceHolderApi.createFicha(MiFIcha1);
        call.enqueue(new Callback<FichaMedica>() {
            @Override
            public void onResponse(Call<FichaMedica> call, Response<FichaMedica> response) {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                FichaMedica miFichaMedica = response.body();
                Log.d("Json", "Se ha insertado la ficah medica cuyas alergias son: " + miFichaMedica.getAlergias());
                Log.d("Insercion ficha", "Me voy a hacer la insercion de la ficha medica x2");
                irmedeaqui();



            }

            @Override
            public void onFailure(Call<FichaMedica> call, Throwable t) {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }

    public void irmedeaqui(){
        Log.d("Insercion ficha", "Me voy a hacer la insercion de una direccion");
        dialog.cancel();
        Bundle paqueteDeDatos;
        paqueteDeDatos = new Bundle();
        paqueteDeDatos.putInt("idUsuario",miIdUsuario);


        Intent actividad=new Intent(this,direccion_Inicio.class);
        actividad.putExtra("Usuario",currentUser);
        actividad.putExtras(paqueteDeDatos);
        startActivity(actividad);
        finish();
    }
}
