package com.example.hatzalahemergencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hatzalahemergencias.R;
import com.example.hatzalahemergencias.Usuario;

import java.util.ArrayList;

public class AdaptadorFamiliares extends BaseAdapter {

    private ArrayList<Usuario> Familiares;
    private Context contexto;

    @Override
    public int getCount() {
        return Familiares.size();
    }

    @Override
    public Usuario getItem(int position) {
        return Familiares.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int posicion, View vista, ViewGroup grupoDeVista) {
        View vistaADevolver=null;

        LayoutInflater inflater=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaADevolver=inflater.inflate(R.layout.listview_familiares,grupoDeVista,false);

        TextView Nombre=vistaADevolver.findViewById(R.id.txtNombreYApellido);
        Nombre.setText(Familiares.get(posicion).getNombreUsuario()+" "+Familiares.get(posicion).getApellidoUsuario());


        return vistaADevolver;
    }

    public AdaptadorFamiliares (ArrayList<Usuario> Familiares, Context contexto){
        this.Familiares=Familiares;
        this.contexto=contexto;
    }
}
