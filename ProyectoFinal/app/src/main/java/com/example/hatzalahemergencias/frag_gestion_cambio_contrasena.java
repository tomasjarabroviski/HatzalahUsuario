package com.example.hatzalahemergencias;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class frag_gestion_cambio_contrasena extends Fragment implements View.OnClickListener {

    private EditText edtxContrasenaAnterior;
    private EditText edtxContrasenaNueva;
    private EditText edtxContrasenaReNueva;
    private ActividadPrincipal actividadPrincipal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater infladorDeLayout, @Nullable ViewGroup grupoDeVista, @Nullable Bundle datos) {
        View vistaAdevolver=infladorDeLayout.inflate(R.layout.act_gestion_cambio_contrasena, grupoDeVista,false);

        ImageButton imgBotonAtras=vistaAdevolver.findViewById(R.id.imgbAtrasGestionCambioContrasena);
        imgBotonAtras.setOnClickListener(this);

        Button botonGuardar=vistaAdevolver.findViewById(R.id.btnGuardarGestionContrasena);
        botonGuardar.setOnClickListener(this);

        actividadPrincipal=(ActividadPrincipal) getActivity();

        edtxContrasenaAnterior=vistaAdevolver.findViewById(R.id.edtxGestionCambioContrasenaAntigua);
        edtxContrasenaNueva=vistaAdevolver.findViewById(R.id.edtxGestionCambioContrasenaNueva1);
        edtxContrasenaReNueva=vistaAdevolver.findViewById(R.id.edtxGestionCambioContrasenaNueva2);

        return vistaAdevolver;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuardarGestionContrasena:
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email, edtxContrasenaAnterior.getText().toString());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (edtxContrasenaNueva.getText().toString().equals(edtxContrasenaReNueva.getText().toString())) {
                                user.updatePassword(edtxContrasenaNueva.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(getActivity());
                                            compiladorAlerta.setMessage("No se logro cambiar la contraseña en este momento, por favor intentelo mas tarde");
                                            compiladorAlerta.setCancelable(true);
                                            compiladorAlerta.setPositiveButton(
                                                    "Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            getFragmentManager().popBackStackImmediate();
                                                        }
                                                    });
                                            AlertDialog Alerta = compiladorAlerta.create();
                                            Alerta.show();
                                        } else {
                                            AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(getActivity());
                                            compiladorAlerta.setMessage("Se cambio la contraseña con exito");
                                            compiladorAlerta.setCancelable(true);
                                            compiladorAlerta.setPositiveButton(
                                                    "Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            getFragmentManager().popBackStackImmediate();
                                                        }
                                                    });
                                            AlertDialog Alerta = compiladorAlerta.create();
                                            Alerta.show();
                                        }
                                    }
                                });
                            } else {
                                AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(getActivity());
                                compiladorAlerta.setMessage("Las contraseñas no coinciden");
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
                            }
                        }else{
                            AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(getActivity());
                            compiladorAlerta.setMessage("Las contraseña anterior es incorrecta");
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
                        }

                    }
                });
                break;
            case R.id.imgbAtrasGestionCambioContrasena:
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }
}

