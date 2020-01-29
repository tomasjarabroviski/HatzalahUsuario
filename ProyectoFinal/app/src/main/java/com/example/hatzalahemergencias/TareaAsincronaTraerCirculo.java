package com.example.hatzalahemergencias;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TareaAsincronaTraerCirculo extends AsyncTask<Integer,Void,ArrayList<Integer>> {
    ArrayList<Integer> ListaFamiliares;
    private TareaCirculoFinalizada finalizada;
    public TareaAsincronaTraerCirculo(TareaCirculoFinalizada tareaCirculoFinalizada){
        this.finalizada=tareaCirculoFinalizada;
    }

    @Override
    protected void onPreExecute() {
        ListaFamiliares=new ArrayList<>();
    }
    @Override
    protected void onPostExecute(ArrayList<Integer> ListaFamiliares) {
        finalizada.TareaCirculoFinalizada(ListaFamiliares);
    }
    @Override
    protected ArrayList<Integer> doInBackground(Integer... idUsuario) {
        try{
            URL rutaDeAcceso=new URL("http://hatzalah.hostingerapp.com/api/circulo/?idUsuario="+idUsuario[0]);
            HttpURLConnection conexion=(HttpURLConnection) rutaDeAcceso.openConnection();
            Log.d("Acceso API","Se inicio la conexion con el id: "+idUsuario[0]);
            if(conexion.getResponseCode()==200){
                Log.d("Acceso API","ResponseCode 200");
                InputStreamReader lectorRespuesta=new InputStreamReader(conexion.getInputStream(),"UTF-8");
                LectorJSON(lectorRespuesta);
            }else{
                Log.d("Acceso API","Error, ResponseCode= "+conexion.getResponseCode());
            }
            conexion.disconnect();
        }catch(Exception error){
            Log.d("Error","idUsuario: "+idUsuario+" Error: "+error.getMessage());
        }
        return ListaFamiliares;
    }
    private void LectorJSON(InputStreamReader cuerpo){
        JsonReader Lector=new JsonReader(cuerpo);
        try{
            Lector.beginArray();
            while (Lector.hasNext()){
                Lector.beginObject();
                while(Lector.hasNext()){
                    if(Lector.nextName().equals("idFamiliar")){
                        ListaFamiliares.add(Lector.nextInt());
                    }else{
                        Lector.skipValue();
                    }
                }
                Lector.endObject();
            }
            Lector.endArray();

        }catch (Exception error){
            Log.d("Error", "Error: "+error.getMessage());
        }
    }
}
