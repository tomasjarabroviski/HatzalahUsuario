package com.example.hatzalahemergencias;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_Emergencia_Pedida extends Fragment implements View.OnClickListener {
    private Button butTerminarEmergeqncai;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ImageView imgImagenRescatista1;
    private ImageButton imgbutTelefono;
    private ImageView imgImagenRescatista2;
    private TextView txtNombreRescatista1;
    private TextView txtNombreRescatista2;
    private TextView txtTiempoRescatista;
    private Rescatista  miRescatista;

    private ActividadPrincipal actividadPrincipal;

    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        actividadPrincipal = (ActividadPrincipal) getActivity();

        final ProgressDialog progressDialog=ProgressDialog.show(getContext(),"Buscando rescatista","Por favor aguarde",true,false);
        FirebaseFirestore.getInstance().collection("llamadosEmergencia").document(actividadPrincipal.getIdFBF())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.d("Rescatista","Fallo el snapshotListner");
                            return;
                        }
                        if(documentSnapshot != null && documentSnapshot.exists()){
                            if(documentSnapshot.contains("Rescatista1")){
                                int id= Integer.parseInt(documentSnapshot.get("Rescatista1").toString());
                                actividadPrincipal.setIdRescatista1(id);
                                getRescatistaUno(id);
                                progressDialog.cancel();
                            }
                            if(documentSnapshot.contains("Rescatista2")){
                                int id2=Integer.parseInt(documentSnapshot.get("Rescatista2").toString());
                                actividadPrincipal.setIdRescatista2(id2);
                                getRescatistaDos(id2);
                            }
                        }
                    }
                });


        Log.d("FragEmergencia", "Comenzo el onCreateView");

        Log.d("FragEmergencia", "Declaro las variables que va a tenber la vista inflada ");
        View vistaAdevolver;

        Log.d("FragEmergencia", "Le asigno la vista inflada");
        vistaAdevolver = infladorDeLayout.inflate(R.layout.act_emergencia_recibida, grupoDeVista, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Log.d("FragEmergencia", "Referencio los elementos");
        butTerminarEmergeqncai = vistaAdevolver.findViewById(R.id.btnFinalizarEmergencai);
        imgImagenRescatista1 = vistaAdevolver.findViewById(R.id.imgFotoRescatista1);
        imgImagenRescatista2 = vistaAdevolver.findViewById(R.id.imgFotoRescatista2);
        imgbutTelefono = vistaAdevolver.findViewById(R.id.imagetelefono);
        txtNombreRescatista1 = vistaAdevolver.findViewById(R.id.txtNombreRescatista1);
        txtNombreRescatista2 = vistaAdevolver.findViewById(R.id.txtNombreRescatista2);
        txtTiempoRescatista = vistaAdevolver.findViewById(R.id.txTiempoRescatista);

        actividadPrincipal=(ActividadPrincipal)getActivity();

        butTerminarEmergeqncai.setOnClickListener(this);
        imgbutTelefono.setOnClickListener(this);

        //aca llega el id de gonzi
        Log.d("gonzii", "id: " + actividadPrincipal.getIdRescatista());

        return vistaAdevolver;


    }

    private void getRescatistaUno(int id) {
        Call<Rescatista> call = jsonPlaceHolderApi.getRescaistaUno(id);
        call.enqueue(new Callback<Rescatista>()
        {
            @Override
            public void onResponse(Call<Rescatista> call, Response<Rescatista> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                miRescatista = response.body();
                Log.d("Json", "Rescatista Encontrado: " + miRescatista.getFotoRescatista());

                StorageReference reference=FirebaseStorage.getInstance().getReference().child(miRescatista.getFotoRescatista());
                final long ONE_MEGABYTE=1024*1024*5;
                reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d("Json","Imagen descargada");
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imgImagenRescatista1.setImageBitmap(bitmap);
                        txtNombreRescatista1.setText(miRescatista.getNombreRescatista()+" "+miRescatista.getApellidoRescatista());
                    }
                });
            }

            @Override
            public void onFailure(Call<Rescatista> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());

            }
        });

    }

    private void getRescatistaDos(int id){
        Call<Rescatista> call = jsonPlaceHolderApi.getRescaistaUno(id);
        call.enqueue(new Callback<Rescatista>()
        {
            @Override
            public void onResponse(Call<Rescatista> call, Response<Rescatista> response)
            {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                final Rescatista miRescatista = response.body();
                Log.d("Json", "Rescatista Encontrado: " + miRescatista.getFotoRescatista());

                StorageReference reference=FirebaseStorage.getInstance().getReference().child(miRescatista.getFotoRescatista());
                final long ONE_MEGABYTE=1024*1024*10;
                reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d("Json","Imagen descargada");
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imgImagenRescatista2.setImageBitmap(bitmap);
                        txtNombreRescatista2.setText(miRescatista.getNombreRescatista()+" "+miRescatista.getApellidoRescatista());
                    }
                });
            }

            @Override
            public void onFailure(Call<Rescatista> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());

            }
        });

    }

    @Override
    public void onClick(View vistaRecibida) {
        switch (vistaRecibida.getId()) {
            case R.id.btnFinalizarEmergencai:
                actividadPrincipal.setIdRescatista1(-1);
                actividadPrincipal.setIdRescatista2(-1);
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.imagetelefono:
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:"+miRescatista.getTelefonoRescatista()));
                startActivity(intent);
                break;



        }
    }
}
