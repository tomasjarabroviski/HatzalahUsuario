package com.example.hatzalahemergencias;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class frag_gestion_usuario extends Fragment implements View.OnClickListener {
    Button butGestionDirecciones;
    ImageButton btnBotonAtras;
    Button btnGestionarUsuario;


    private ActividadPrincipal actiAnfitriona;


    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        Log.d("FragGestion" , "Comenzo el onCreateView");

        //Asigno y creo vista inflada
        View vistaAdevolver=infladorDeLayout.inflate(R.layout.act_gestion_usuario, grupoDeVista,false);

        //Referencio elementos
        Button butGestionDirecciones = vistaAdevolver.findViewById(R.id.butGestorDirecciones);
        butGestionDirecciones.setOnClickListener(this);

        ImageButton btnBotonAtras=vistaAdevolver.findViewById(R.id.btnAtrasGestionUsuario);
        btnBotonAtras.setOnClickListener(this);

        Button btnGestionarUsuario = vistaAdevolver.findViewById(R.id.butGestionarUsuario);
        btnGestionarUsuario.setOnClickListener(this);

        Button btnCambioContrasena = vistaAdevolver.findViewById(R.id.btnCambiarContrasena);
        btnCambioContrasena.setOnClickListener(this);

        Button btnGestionarFichaMedica = vistaAdevolver.findViewById(R.id.butGestorFichaMedica);
        btnGestionarFichaMedica.setOnClickListener(this);

        Button btnLogout= vistaAdevolver.findViewById(R.id.btnCerrarSesion);
        btnLogout.setOnClickListener(this);

        actiAnfitriona = (ActividadPrincipal) getActivity();

        return  vistaAdevolver;
    }

    public void onClick(View vistaRecibida)
    {
        switch (vistaRecibida.getId()){
            case R.id.btnAtrasGestionUsuario:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.butGestorDirecciones:
                actiAnfitriona.GestionarDirecciones();
                break;
            case R.id.btnCambiarContrasena:
                actiAnfitriona.CambiarContrasena();
                break;
            case R.id.butGestionarUsuario:
                actiAnfitriona.GestionarUsuario();
                break;
            case R.id.butGestorFichaMedica:
                actiAnfitriona.GestionarFichaMedeica();
                break;
            case R.id.btnCerrarSesion:
                Log.d("logout","Entra");
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getContext(),ActividadLogin.class);
                startActivity(intent);
                getActivity().finish();
                break;


        }
    }
}
