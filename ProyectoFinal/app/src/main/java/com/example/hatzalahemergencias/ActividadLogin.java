package com.example.hatzalahemergencias;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class ActividadLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private FragmentManager adminFragment;
    private FragmentTransaction transacFragment;
    private FirebaseFirestore db;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_login);
        dialog= ProgressDialog.show(this, "Cargando",
                "Cargando. porfavor espere...", true);
        dialog.show();
        mAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

       //FirebaseAuth.getInstance().signOut();
        final FirebaseUser currentUser=mAuth.getCurrentUser();


        if(currentUser==null) {
            adminFragment = getSupportFragmentManager();
            Fragment frgLogIn= new frag_LogIn();
            transacFragment = adminFragment.beginTransaction();
            transacFragment.replace(R.id.alojadorDeFragmentLogIn, frgLogIn);
            transacFragment.commit();
            dialog.cancel();
        }else{
            String UID=mAuth.getCurrentUser().getUid();
            db.collection("Usuarios")
                    .document(UID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String,Object> data=documentSnapshot.getData();
                            Intent actividad=new Intent(ActividadLogin.this, ActividadPrincipal.class);
                            actividad.putExtra("idUsuario",Integer.parseInt(data.get("IdUsuario").toString()));
                            actividad.putExtra("Usuario",currentUser);
                            startActivity(actividad);
                            finish();
                            dialog.dismiss();
                        }
                    });
        }
    }

    public void LoggedIn(FirebaseUser currentUser, int idUsuario, boolean hacer){
        Log.d("Insercion ficha", "Me voy a hacer la insercion de la ficha medica");
        if (hacer)
        {
            Bundle paqueteDeDatos;
            paqueteDeDatos = new Bundle();
            paqueteDeDatos.putInt("idUsuario",idUsuario);


            Intent actividad=new Intent(this,ActividadPrincipal.class);
            actividad.putExtra("Usuario",currentUser);
            actividad.putExtras(paqueteDeDatos);
            startActivity(actividad);
            finish();
        } else {
            //AAAACAAAAA
            Log.d("Insercion ficha", "Me voy a hacer la insercion de la ficha medica x2");
            Bundle paqueteDeDatos;
            paqueteDeDatos = new Bundle();
            paqueteDeDatos.putInt("idUsuario",idUsuario);

            Intent actividad=new Intent(this,form_ficha_medica.class);
            actividad.putExtra("Usuario",currentUser);
            actividad.putExtras(paqueteDeDatos);
            startActivity(actividad);
            finish();

        }

    }

    public void Register(){
        Fragment frgRegistro= new frag_registro();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragmentLogIn, frgRegistro);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }

    public void ContrasenaOlvidada(){
        Fragment frgRegistro= new frag_contrasena_olvidada();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragmentLogIn, frgRegistro);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }



}
