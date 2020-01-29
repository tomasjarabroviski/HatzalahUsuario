package com.example.hatzalahemergencias;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class frag_solicitar_rescatista extends Fragment implements View.OnClickListener {
    Usuario usuario;
    ActividadPrincipal actividadPrincipal;
    Button botonSolicitarParamedico;
    ImageButton botonAtras;
    Spinner direcciones;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater infladorDeLayout, ViewGroup grupoDeVista, Bundle datos) {
        View vistaADevolver;
        vistaADevolver = infladorDeLayout.inflate(R.layout.act_solicitar_rescatista, grupoDeVista, false);

        db= FirebaseFirestore.getInstance();

        actividadPrincipal=(ActividadPrincipal)getActivity();
        usuario=actividadPrincipal.getUsuarioEmergencia();
        botonSolicitarParamedico=vistaADevolver.findViewById(R.id.btnSolicitarRescatista);
        botonAtras=vistaADevolver.findViewById(R.id.imgbAtrasSoliciarRescatista);
        botonAtras.setOnClickListener(this);
        botonSolicitarParamedico.setOnClickListener(this);
        TextView txtParaEmergencia=vistaADevolver.findViewById(R.id.txtParaSolicitarRescatista);
        EditText edtxNombre=vistaADevolver.findViewById(R.id.edtxNombre);
        EditText edtxApellido=vistaADevolver.findViewById(R.id.edtxApellido);
        EditText edtxDNI=vistaADevolver.findViewById(R.id.edtxDNI);
        EditText edtxNumeroTelefono=vistaADevolver.findViewById(R.id.edtxNumeroDeTelefono);


        edtxNombre.setText(usuario.getNombreUsuario());
        edtxApellido.setText(usuario.getApellidoUsuario());
        edtxDNI.setText(""+usuario.getDniUsuario());
        edtxNumeroTelefono.setText(""+usuario.getTelefonoUsuario());
        direcciones=vistaADevolver.findViewById(R.id.spinnerDirecciones);
        ArrayList<String> etiquetasSpinner=new ArrayList<>();
        int x=0;
        do{
            etiquetasSpinner.add(actividadPrincipal.getUsuarioEmergencia().getDireccionesUsuario().get(x).getEtiqueta());
            x++;
        }while(x<actividadPrincipal.getUsuarioEmergencia().getDireccionesUsuario().size());
        ArrayAdapter adaptadorSpinner=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,etiquetasSpinner);
        direcciones.setAdapter(adaptadorSpinner);

        txtParaEmergencia.setText(txtParaEmergencia.getText().toString()+usuario.getNombreUsuario());

        Log.d("direcciones",""+actividadPrincipal.getUsuarioEmergencia().getDireccionesUsuario().get(0).getLat());

        return vistaADevolver;
    }

    public void onClick(View vistaRecibida) {

        switch (vistaRecibida.getId()){
            case R.id.imgbAtrasSoliciarRescatista:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.btnSolicitarRescatista:
                final ProgressDialog progressDialog=ProgressDialog.show(getContext(),"Buscando rescatista","Por favor aguarde",true,false);
                Map<String,Object> data=new HashMap<>();
                data.put("NombreUsuario",usuario.getNombreUsuario());
                data.put("ApellidoUsuario",usuario.getApellidoUsuario());
                data.put("DNIUsuario",usuario.getDniUsuario());
                data.put("MailUsuario",usuario.getMailUsuario());
                data.put("IdUsuario",usuario.getIdUsuario());
                data.put("TelefonoUsuario",usuario.getTelefonoUsuario());
                data.put("DireccionEmergencia",actividadPrincipal.getUsuarioEmergencia().getDireccionesUsuario().get(direcciones.getSelectedItemPosition()));
                db.collection("llamadosEmergencia")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(final DocumentReference documentReference) {
                                actividadPrincipal.setIdFBF(documentReference.getId());
                                Log.d("Firestore", "DocumentSnapshot successfully written!");
                                Task<String> result=DistanceMatrix(documentReference.getId());
                                actividadPrincipal.DatosemergenciaRescatista();
                                progressDialog.cancel();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Firestore", "Error writing document: "+ e.getMessage());
                            }
                        });

                break;
        }
    }

    private Task<String> DistanceMatrix(String idFBF){
        Map<String, Object> datosDistanceMatrix=new HashMap<>();
        datosDistanceMatrix.put("lat",actividadPrincipal.getUsuarioEmergencia().getDireccionesUsuario().get(direcciones.getSelectedItemPosition()).getLat());
        datosDistanceMatrix.put("long",actividadPrincipal.getUsuarioEmergencia().getDireccionesUsuario().get(direcciones.getSelectedItemPosition()).getLon());
        datosDistanceMatrix.put("id",idFBF);
        Log.d("distance","Lat: "+datosDistanceMatrix.get("lat")+" lon: "+datosDistanceMatrix.get("long")+" id: "+datosDistanceMatrix.get("id"));
        return FirebaseFunctions.getInstance()
                .getHttpsCallable("distanceMatrix")
                .call(datosDistanceMatrix).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                Log.d("distance",""+task.getResult().getData());
                return (String) task.getResult().getData();
            }
        });
    }

}
