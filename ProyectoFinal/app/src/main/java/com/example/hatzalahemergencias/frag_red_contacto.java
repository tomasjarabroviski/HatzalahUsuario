package com.example.hatzalahemergencias;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class frag_red_contacto extends Fragment implements View.OnClickListener {
    Button[] ArrayBotones=new Button[8];
    Button botonAtras;
    ActividadPrincipal actiAnfitriona;
    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        View vistaADevolver;
        vistaADevolver = infladorDeLayout.inflate(R.layout.act_red_contactos, grupoDeVista, false);
        actiAnfitriona = (ActividadPrincipal) getActivity();
        botonAtras=vistaADevolver.findViewById(R.id.btnAtrasRedContactos);
        ArrayBotones[0]= vistaADevolver.findViewById(R.id.BotonParaMi);
        ArrayBotones[1]= vistaADevolver.findViewById(R.id.BotonContacto1);
        ArrayBotones[2] = vistaADevolver.findViewById(R.id.BotonContacto2);
        ArrayBotones[3] = vistaADevolver.findViewById(R.id.BotonContacto3);
        ArrayBotones[4] = vistaADevolver.findViewById(R.id.BotonContacto4);
        ArrayBotones[5] = vistaADevolver.findViewById(R.id.BotonContacto5);
        ArrayBotones[6] = vistaADevolver.findViewById(R.id.BotonContacto6);
        ArrayBotones[7] = vistaADevolver.findViewById(R.id.BotonContacto7);

        ArrayBotones[0].setOnClickListener(this);
        ArrayBotones[1].setOnClickListener(this);
        ArrayBotones[2].setOnClickListener(this);
        ArrayBotones[3].setOnClickListener(this);
        ArrayBotones[4].setOnClickListener(this);
        ArrayBotones[5].setOnClickListener(this);
        ArrayBotones[6].setOnClickListener(this);
        ArrayBotones[7].setOnClickListener(this);
        botonAtras.setOnClickListener(this);


        for (int x = 0; x <actiAnfitriona.getFamiliaresUsuario().size(); x++) {
            Log.d("user", "Lista de familiares " + actiAnfitriona.getFamiliaresUsuario().get(x).getNombreUsuario());
            ArrayBotones[x+1].setText(actiAnfitriona.getFamiliaresUsuario().get(x).getNombreUsuario());
            ArrayBotones[x+1].setVisibility(View.VISIBLE);
        }

        return vistaADevolver;

    }
    public void onClick(View vistaRecibida) {
        Button botonPrecionado=(Button) vistaRecibida;
        if(botonPrecionado.getId()!=botonAtras.getId()){
            boolean Bandera=false;
            int posicion=0;
            do{
                if(botonPrecionado.getId()==ArrayBotones[posicion].getId()){
                    Bandera=true;
                }
                posicion++;
            }while(posicion<ArrayBotones.length&&!Bandera);
            actiAnfitriona.RedContactos(posicion-1);
        }else{
            getFragmentManager().popBackStackImmediate();
        }

    }
}
