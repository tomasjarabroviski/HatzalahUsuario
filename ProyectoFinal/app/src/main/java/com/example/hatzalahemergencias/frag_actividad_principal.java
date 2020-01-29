package com.example.hatzalahemergencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class frag_actividad_principal extends Fragment implements View.OnClickListener {
    Button butPediremergencia;
    Button butCirculoFamiliar;
    ImageButton btnGesionarUsuario;

    ActividadPrincipal actividadPrincipal;


    @Override
    public View onCreateView(LayoutInflater infadorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        View vistaAdevolver;
        vistaAdevolver = infadorDeLayout.inflate(R.layout.act_principal, grupoDeVista, false);

        butPediremergencia = vistaAdevolver.findViewById(R.id.butSolicitarRescatista);
        butCirculoFamiliar = vistaAdevolver.findViewById(R.id.butCirculoFamiliar);
        btnGesionarUsuario = vistaAdevolver.findViewById(R.id.GestionUsario);


        butPediremergencia.setOnClickListener(this);
        butCirculoFamiliar.setOnClickListener(this);
        btnGesionarUsuario.setOnClickListener(this);

        actividadPrincipal=(ActividadPrincipal)getActivity();

        if(actividadPrincipal.getUsuario()!=null){
            HabilitarBotones();
        }

        return vistaAdevolver;

    }

    public void onClick(View vistaRecibida) {

        ActividadPrincipal actiAnfitriona;
        actiAnfitriona = (ActividadPrincipal) getActivity();

        switch (vistaRecibida.getId()) {
            case R.id.butSolicitarRescatista:
                actiAnfitriona.ProcesarEmergencia();
                break;
            case R.id.butCirculoFamiliar:
                actiAnfitriona.GestionarCirculo();
                break;
            case R.id.GestionUsario:
                actiAnfitriona.ProcesarGestionMenu();
                break;
        }
    }

    public void HabilitarBotones(){
        butCirculoFamiliar.setEnabled(true);
        butPediremergencia.setEnabled(true);
        btnGesionarUsuario.setClickable(true);
    }
}
