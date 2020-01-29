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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class frag_contrasena_olvidada extends Fragment implements View.OnClickListener {

    Dialog dialog;
    Button botonEnviarMail;
    EditText edtxEmail;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater infladorDeLayout, @Nullable ViewGroup grupoDeVista, @Nullable Bundle datos) {
        View vista;
        vista=infladorDeLayout.inflate(R.layout.act_contrasena_olvidada, grupoDeVista, false);

        botonEnviarMail=vista.findViewById(R.id.btnContrasenaOlvidadaEnviarMail);
        botonEnviarMail.setOnClickListener(this);

        ImageButton imgbAtras=vista.findViewById(R.id.imgbatrasContrasenaOlvidada);
        imgbAtras.setOnClickListener(this);

        edtxEmail=vista.findViewById(R.id.edtxEmailContrasenaOlvidada);

        return vista;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnContrasenaOlvidadaEnviarMail:
                if(edtxEmail.getText().toString().length()>0) {
                    dialog= ProgressDialog.show(getActivity(),"Guardando",
                            "Guardando. porfavor espere...", true);
                    dialog.show();
                    botonEnviarMail.setEnabled(false);
                    ContrasenaOlvidada();
                }else{
                        Toast.makeText(getActivity(),"Porfavor ingrese un email",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imgbatrasContrasenaOlvidada:
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }

    private void ContrasenaOlvidada(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(edtxEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Resetcontrasena", "Email sent.");
                            dialog.cancel();
                            Toast.makeText(getActivity(),"Email Enviado",Toast.LENGTH_LONG).show();
                        }else{
                            dialog.cancel();
                            Toast.makeText(getActivity(),"Email no enviado, porfavor intentelo nuevamente",Toast.LENGTH_LONG).show();
                            botonEnviarMail.setEnabled(true);
                        }
                    }
                });
    }

}
