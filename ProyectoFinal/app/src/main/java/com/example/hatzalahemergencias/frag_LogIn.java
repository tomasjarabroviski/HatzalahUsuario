package com.example.hatzalahemergencias;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;


public class frag_LogIn extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    EditText edtxEmail;
    EditText edtxContrasena;
    Button botonIniciarSesion;
    //Button botonIniciarSesionGoogle;
    Button botonRegistrarse;
    TextView txtOlvidoContrasena;
    ActividadLogin actividadLogin;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        View vistaADevolver;
        vistaADevolver = infladorDeLayout.inflate(R.layout.act_login, grupoDeVista, false);

        botonIniciarSesion=vistaADevolver.findViewById(R.id.botonInicioSesion);
        botonIniciarSesion.setOnClickListener(this);

        botonRegistrarse=vistaADevolver.findViewById(R.id.botonRegistroLogIn);
        botonRegistrarse.setOnClickListener(this);

        txtOlvidoContrasena=vistaADevolver.findViewById(R.id.contrasenaOlvidada);
        txtOlvidoContrasena.setOnClickListener(this);

        edtxEmail=vistaADevolver.findViewById(R.id.edtxInicioSesionEmail);
        edtxContrasena=vistaADevolver.findViewById(R.id.edtxInicioSesionContrasena);

        actividadLogin=(ActividadLogin) getActivity();

        mAuth=actividadLogin.getmAuth();

        db= FirebaseFirestore.getInstance();

        return vistaADevolver;
    }



    @Override
    public void onClick(View vista) {
        switch (vista.getId()){
            case R.id.botonInicioSesion:
                LogIn();
                break;
            case R.id.botonRegistroLogIn:
                actividadLogin.Register();
                break;
            case R.id.contrasenaOlvidada:
                actividadLogin.ContrasenaOlvidada();
                break;
        }
    }

    private void LogIn() {
        DeshabilitarTodosElementos();
        final Toast toast=new Toast(getActivity());
        toast.makeText(getActivity(),"Iniciando sesion...",Toast.LENGTH_LONG).show();
        mAuth.signInWithEmailAndPassword(edtxEmail.getText().toString(),edtxContrasena.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toast.cancel();
                            Log.d("LogIn", "signInWithEmail:success");
                            // ACA DEBERIA PASARLE EL ID DEL USUARIO DE MI BDD SQL
                            String UID=mAuth.getCurrentUser().getUid();
                            db.collection("Usuarios")
                                    .document(UID)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Map<String,Object> data=documentSnapshot.getData();
                                            actividadLogin.LoggedIn(mAuth.getCurrentUser(), Integer.parseInt(data.get("IdUsuario").toString()), true);
                                        }
                                    });

                        } else {
                            Log.d("LogIn", "signInWithEmail:failure "+ task.getException().getMessage());
                            AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(getActivity());
                            compiladorAlerta.setMessage("Email o contraseña invalidos");
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
                            HabilitarTodosElementos();
                        }
                    }
                });
    }

    private void DeshabilitarTodosElementos(){
        edtxEmail.setEnabled(false);
        edtxContrasena.setEnabled(false);
        botonIniciarSesion.setEnabled(false);
        botonRegistrarse.setEnabled(false);
        txtOlvidoContrasena.setEnabled(false);
    }

    private void HabilitarTodosElementos(){
        edtxEmail.setEnabled(true);
        edtxContrasena.setEnabled(true);
        botonIniciarSesion.setEnabled(true);
        botonRegistrarse.setEnabled(true);
        txtOlvidoContrasena.setEnabled(true);
    }


}
