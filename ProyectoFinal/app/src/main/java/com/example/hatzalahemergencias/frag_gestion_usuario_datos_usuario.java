package com.example.hatzalahemergencias;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class frag_gestion_usuario_datos_usuario extends Fragment implements View.OnClickListener {
    private Button butAgregar;
    private ImageButton butaTras;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ActividadPrincipal actividadPrincipal;
    private EditText edtxNombre;
    private EditText edtxApellido;
    private EditText edtxDNI;
    private EditText edtxNumeroTelefono;
    private EditText edtxEmail;
    private EditText edtxFechaNacimiento;
    private ProgressDialog dialog;

    private Bitmap imagenUsuario=null;

    private static final int PICK_IMAGE=100;

    private String year1;
    private String month1;
    private String day1;

    @Override
    public View onCreateView(LayoutInflater infladorDeLAyout,  ViewGroup grupoDeVista,  Bundle datos) {
        View miVista;
        actividadPrincipal=(ActividadPrincipal) getActivity();
        miVista = infladorDeLAyout.inflate(R.layout.act_gestion_usuario_datos_primarios, grupoDeVista, false);
        edtxNombre=miVista.findViewById(R.id.edtxModificacionNombreUsuario);
        edtxApellido=miVista.findViewById(R.id.edtxModificacionApellidoUsuario);
        edtxNumeroTelefono=miVista.findViewById(R.id.edtxModificacionTelefonoUsuario);
        edtxDNI=miVista.findViewById(R.id.edtxModificarDNIUsuario);
        edtxEmail=miVista.findViewById(R.id.edtxModificacionEmailUsuario);
        edtxFechaNacimiento= miVista.findViewById(R.id.edtxModificacionFechaNacimientoUsuario);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hatzalah.hostingerapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        butAgregar = miVista.findViewById(R.id.btnModificacionGuardarUsuario);
        butaTras = miVista.findViewById(R.id.imgbAtrasGestionDatosUsuario);

        butAgregar.setOnClickListener(this);
        butaTras.setOnClickListener(this);

        Button botonElegirFoto=miVista.findViewById(R.id.btnModificacionFotoUsuario);
        botonElegirFoto.setOnClickListener(this);


        edtxNombre.setText(actividadPrincipal.getUsuario().getNombreUsuario());
        edtxApellido.setText(actividadPrincipal.getUsuario().getApellidoUsuario());
        edtxNumeroTelefono.setText(""+actividadPrincipal.getUsuario().getTelefonoUsuario());
        edtxEmail.setText(actividadPrincipal.getUsuario().getMailUsuario());

        edtxDNI.setText(""+actividadPrincipal.getUsuario().getDniUsuario());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        edtxFechaNacimiento.setText(df.format(actividadPrincipal.getUsuario().getFechaNacimiento()));

        edtxFechaNacimiento.setOnClickListener(this);

         return miVista;
    }

    public void onClick(View vistaRecibida) {
        Log.d("Json", "Mi ID que llego es: " + vistaRecibida.getId());
        // getFragmentManager().popBackStackImmediate();
        switch (vistaRecibida.getId()){
            case R.id.btnModificacionGuardarUsuario:
               if(edtxEmail.getText().length() == 0 || edtxNumeroTelefono.getText().length() == 0) {
                   Toast toast = Toast.makeText(getActivity(), "Debe completar todos los campos", Toast.LENGTH_SHORT);
                   toast.setMargin(50, 50);
                   toast.show();
               }else {
                  /* dialog = ProgressDialog.show(getActivity(), "Guardando",
                           "Guardando. porfavor espere...", true);
                   dialog.show();*/
                   if(imagenUsuario!=null) {
                       FirebaseStorage storage=FirebaseStorage.getInstance();
                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                       imagenUsuario.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                       byte[] data = baos.toByteArray();
                       StorageReference storageRef = storage.getReference();
                       StorageReference imagesRef = storageRef.child("ImagenesPerfil/" + actividadPrincipal.getCurrentUser().getUid());

                       UploadTask uploadTask = imagesRef.putBytes(data);

                       uploadTask.addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception exception) {
                               Log.d("Registro", "Error de subida de imagen" + exception.getMessage());
                           }
                       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               Log.d("Register", "Ano: " + year1);
                               Log.d("Register", "mes: " + month1);
                               Log.d("Register", "dia: " + day1);
                               Toast.makeText(getActivity(),"La imagen se subio",Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
                   dialog= ProgressDialog.show(getActivity(),"Guardando",
                           "Guardando. porfavor espere...", true);
                   dialog.show();
                   Log.d("Register", "Ano: " + year1);
                   Log.d("Register", "mes: " + month1);
                   Log.d("Register", "dia: " + day1);
                   UsuarioInsert MiUsuario = new UsuarioInsert(edtxDNI.getText().toString(),edtxNombre.getText().toString(), edtxApellido.getText().toString(),edtxNumeroTelefono.getText().toString(), edtxEmail.getText().toString(), "asda", 1,"",edtxFechaNacimiento.getText().toString());
                   //UsuarioInsert MiUsuario = new UsuarioInsert(edtxDNI.getText().toString(),edtxNombre.getText().toString(), edtxApellido.getText().toString(),edtxNumeroTelefono.getText().toString(), edtxEmail.getText().toString(), edtxContrasena.getText().toString(), 1,"asdas","asd");
                   //UsuarioInsert MiUsuario = new UsuarioInsert(edtxDNI.getText().toString(), edtxNombre.getText().toString(), edtxApellido.getText().toString(), edtxNumeroTelefono.getText().toString(), edtxEmail.getText().toString(), edtxContrasena.getText().toString(), 1, "asd", nacimiento);
                   updateUsuario(MiUsuario);
               }

                Log.d("Json", "ME LO VOY A GUARDAR");

                //ACA GUARDAR
                break;
            case R.id.imgbAtrasGestionDatosUsuario:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.btnModificacionFotoUsuario:
                if(hasStoragePermission(1)){
                    AbrirGaleria();
                }
                break;
            case R.id.edtxModificacionFechaNacimientoUsuario:
                edtxFechaNacimiento.setEnabled(false);
                showDatePickerDialog();
                break;
        }
    }

    private void updateUsuario(UsuarioInsert MiUsuarioaCambiar) {
        Call<Usuario> call = jsonPlaceHolderApi.updateUsuario(actividadPrincipal.getUsuario().getIdUsuario(), MiUsuarioaCambiar);
        call.enqueue(new Callback<Usuario>()
        {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response)
            {
                if (!response.isSuccessful())
                {
                    AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(getActivity());
                    compiladorAlerta.setMessage("Error de conexion con el servidor");
                    compiladorAlerta.setCancelable(true);
                    compiladorAlerta.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog Alerta = compiladorAlerta.create();
                    Alerta.show();
                    Log.d("Json", "Codeeee: " + response.code());
                }else{
                    Usuario MiUsuario2 = response.body();
                    Log.d("Json", "Se ha insertado El usuario:" + MiUsuario2.getNombreUsuario());
                    //actividadPrincipal.setUsuario(MiUsuario2);
                    actividadPrincipal.getUsuario().setTelefonoUsuario(MiUsuario2.getTelefonoUsuario());
                    actividadPrincipal.getUsuario().setMailUsuario(MiUsuario2.getMailUsuario());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                    try {
                        actividadPrincipal.getUsuario().setFechaNacimiento(df.parse(edtxFechaNacimiento.getText().toString()));
                    }catch (Exception error){
                        Log.d("ErrorEnDayFormat",error.getMessage());
                    }

                dialog.cancel();
                    getFragmentManager().popBackStackImmediate();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t)
            {
                Log.d("Json", "Error: " + t.getMessage());
            }
        });

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
                final String selectedDate = day + "-" + (month+1) + "-" + year;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            try{
                imagenUsuario = MediaStore.Images.Media.getBitmap(actividadPrincipal.getContentResolver(), pickedImage);
                Toast.makeText(getActivity(),"La imagen se cargo",Toast.LENGTH_LONG).show();
            }catch(Exception error){
                Log.d("Registro","Error de imagenUsuario: "+error.getMessage());
            }
        }else{
            Log.d("Registro","Error alguna cosa llego mal al IF de actvityResult");
        }
    }

}
