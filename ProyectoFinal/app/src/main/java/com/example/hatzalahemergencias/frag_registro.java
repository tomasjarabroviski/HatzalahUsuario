package com.example.hatzalahemergencias;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class frag_registro extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private ActividadLogin actividadLogin;

    private EditText edtxNombre;
    private EditText edtxApellido;
    private EditText edtxDNI;
    private EditText edtxNumeroTelefono;
    private EditText edtxContrasena;
    private EditText edtxReingresoContrasena;
    private EditText edtxEmail;
    private EditText edtxFechaNacimiento;
    private String year1;
    private String month1;
    private String day1;
    private FirebaseUser user;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ProgressDialog dialog;
    private Bitmap imagenUsuario;

    private FirebaseFirestore db;

    private static final int PICK_IMAGE=100;

    private FirebaseStorage storage;


    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        View vistaADevolver;
        vistaADevolver = infladorDeLayout.inflate(R.layout.act_registro, grupoDeVista, false);
            //EN EL CREATE USUARIO TENGO EL ID
        edtxNombre=vistaADevolver.findViewById(R.id.edtxRegistroNombre);
        edtxApellido=vistaADevolver.findViewById(R.id.edtxRegistroApellido);
        edtxContrasena=vistaADevolver.findViewById(R.id.edtxRegistroContrasena);
        edtxReingresoContrasena=vistaADevolver.findViewById(R.id.edtxRegistroContrasena2);
        edtxNumeroTelefono=vistaADevolver.findViewById(R.id.edtxRegistroNumeroTelefono);
        edtxDNI=vistaADevolver.findViewById(R.id.edtxRegistroDNI);
        edtxEmail=vistaADevolver.findViewById(R.id.edtxRegistroEmail);

        edtxFechaNacimiento=vistaADevolver.findViewById(R.id.edtxRegistroFechaNacimiento);
        edtxFechaNacimiento.setOnClickListener(this);

        Button botonRegistro=vistaADevolver.findViewById(R.id.botonRegistroRegistro);
        botonRegistro.setOnClickListener(this);

        Button botonFoto=vistaADevolver.findViewById(R.id.botonRegistroElegirFoto);
        botonFoto.setOnClickListener(this);

        ImageButton imgbAtrasRegistro=vistaADevolver.findViewById(R.id.imgbAtrasRegistro);
        imgbAtrasRegistro.setOnClickListener(this);

        actividadLogin=(ActividadLogin) getActivity();

        mAuth=actividadLogin.getmAuth();

        storage = FirebaseStorage.getInstance();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        db= FirebaseFirestore.getInstance();


        return vistaADevolver;
    }

    private void createUsuario(UsuarioInsert MiUsuario3){
        Call<UsuarioInsert> call = jsonPlaceHolderApi.createUsuario(MiUsuario3);
        Log.d("json",""+call.request());
        call.enqueue(new Callback<UsuarioInsert>() {
            @Override
            public void onResponse(Call<UsuarioInsert> call, Response<UsuarioInsert> response) {
                if (!response.isSuccessful())
                {
                    Log.d("Json", "Code: " + response.code());
                    return;
                }
                Log.d("Json", "Code: " + response.code());
                final UsuarioInsert MiUsuario = response.body();
                Log.d("Json", "Se ha insertado el usuario con DNI:" + MiUsuario.getDniusuario() + "y id con: " + MiUsuario.getIdUsuario());
                //ACA TENGO EL ID  MiUsuario.getIdUsuario().

                Map<String,Object> data=new HashMap<>();
                data.put("IdUsuario",MiUsuario.getIdUsuario());
                data.put("Rescatista",0);

                db.collection("Usuarios").document(mAuth.getCurrentUser().getUid())
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                actividadLogin.LoggedIn(user, MiUsuario.getIdUsuario(), false);
                                dialog.cancel();
                                Log.d("Registro", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.cancel();
                                Log.d("Registro", "Error writing document"+ e.getMessage());
                            }
                        });

            }

            @Override
            public void onFailure(Call<UsuarioInsert> call, Throwable t) {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View vista) {
        switch (vista.getId()){
            case R.id.botonRegistroRegistro:
                if(edtxContrasena.getText().toString().equals(edtxReingresoContrasena.getText().toString())&&imagenUsuario!=null){
                    dialog= ProgressDialog.show(getActivity(), "Cargando",
                            "Cargando. porfavor espere...", true);
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(edtxEmail.getText().toString(),edtxContrasena.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Register", "createUserWithEmail:success");
                                        user = mAuth.getCurrentUser();

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        imagenUsuario.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                                        byte[] data = baos.toByteArray();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference imagesRef = storageRef.child("ImagenesPerfil/"+user.getUid());

                                        UploadTask uploadTask = imagesRef.putBytes(data);

                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Log.d("Registro","Error de subida de imagen"+exception.getMessage());
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Log.d("Register", "Ano: " + year1);
                                                Log.d("Register", "mes: " + month1);
                                                Log.d("Register", "dia: " + day1);
                                                String nacimiento= year1 + "-" + month1 + "-" + day1;
                                                UsuarioInsert MiUsuario2 = new UsuarioInsert(edtxDNI.getText().toString(),edtxNombre.getText().toString(),edtxApellido.getText().toString(), edtxNumeroTelefono.getText().toString(), edtxEmail.getText().toString(), edtxContrasena.getText().toString(), 1, "ImagenesPerfil/"+user.getUid(), nacimiento);
                                                createUsuario(MiUsuario2);
                                            }
                                        });

                                    } else {
                                        dialog.cancel();
                                        Log.d("Register", "createUserWithEmail:failure " + task.getException());
                                        Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    if(imagenUsuario==null){
                        Toast.makeText(getActivity(),"Debe seleccionar una foto",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.imgbAtrasRegistro:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.edtxRegistroFechaNacimiento:
                edtxFechaNacimiento.setEnabled(false);
                showDatePickerDialog();
                break;
            case R.id.botonRegistroElegirFoto:
                if(hasStoragePermission(1)){
                 AbrirGaleria();
                }
                break;
        }
    }



    private void showDatePickerDialog() {
        Calendar cal= Calendar.getInstance();
        int dia=cal.get(Calendar.DAY_OF_MONTH);
        int mes=cal.get(Calendar.MONTH);
        int ano=cal.get(Calendar.YEAR);

        DatePickerDialog dialog=new DatePickerDialog(
                getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                month1 = Integer.toString(month);
                day1 = Integer.toString(day);
                year1 = Integer.toString(year);
                edtxFechaNacimiento.setText(selectedDate);
            }
        }, ano, mes, dia);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        edtxFechaNacimiento.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            try{
                imagenUsuario = MediaStore.Images.Media.getBitmap(actividadLogin.getContentResolver(), pickedImage);
                Toast.makeText(getActivity(),"La imagen se cargo",Toast.LENGTH_LONG).show();
            }catch(Exception error){
                Log.d("Registro","Error de imagenUsuario: "+error.getMessage());
            }
        }else{
            Log.d("Registro","Error alguna cosa llego mal al IF de actvityResult");
        }
    }

    private boolean hasStoragePermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1){
                AbrirGaleria();
            }

        }
    }

    private void AbrirGaleria(){
        Intent galeria=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria,100);
    }

}



