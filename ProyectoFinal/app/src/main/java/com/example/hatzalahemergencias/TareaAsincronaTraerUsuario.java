package com.example.hatzalahemergencias;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;



import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TareaAsincronaTraerUsuario extends AsyncTask<Integer,Void,ArrayList<Usuario>> {
    private TareaFinalizada finalizada;
    public TareaAsincronaTraerUsuario(TareaFinalizada finalizada){
        this.finalizada=finalizada;
    }
    private ArrayList<Usuario> usuarios=new ArrayList<>();
    private ArrayList<Direccion> ListaDireccionesUsuario=new ArrayList<>();
    @Override
    protected ArrayList<Usuario> doInBackground(Integer... idUsuario) {
        int x = 0;
        do {
            try {
                URL rutaDeAcceso = new URL("http://hatzalah.hostingerapp.com/api/usuario/?idUsuario=" +idUsuario[x]);
                HttpURLConnection conexion = (HttpURLConnection) rutaDeAcceso.openConnection();
                Log.d("Acceso API", "Se inicio la conexion");
                if (conexion.getResponseCode() == 200) {
                    Log.d("Acceso API", "ResponseCode 200");
                    InputStreamReader lectorRespuesta = new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8);
                    LectorJSON(lectorRespuesta);
                } else {
                    Log.d("Acceso API", "Error, ResponseCode= " + conexion.getResponseCode());
                }
                conexion.disconnect();
            }catch(Exception error){
                Log.d("Error", "Error: " + error.getMessage());
            }
            x++;
        }while(idUsuario.length>x);
        return usuarios;
    }
    private void LectorJSON(InputStreamReader cuerpo) {
        JsonReader Lector = new JsonReader(cuerpo);
        Usuario usuario=new Usuario();
        try {
            Lector.beginObject();
            while (Lector.hasNext()) {
                String siguienteNombre = Lector.nextName();
                if (siguienteNombre.equals("Usuario")) {
                    Lector.beginObject();
                    usuario=new Usuario();
                    while (Lector.hasNext()) {
                        siguienteNombre = Lector.nextName();
                        switch (siguienteNombre) {
                            case "nombreUsuario":
                                usuario.setNombreUsuario(Lector.nextString());
                                break;
                            case "apellidoUsuario":
                                usuario.setApellidoUsuario(Lector.nextString());
                                break;
                            case "idUsuario":
                                usuario.setIdUsuario(Lector.nextInt());
                                break;
                            case "telefonoUsuario":
                                usuario.setTelefonoUsuario(Lector.nextLong());
                                break;
                            case "dniusuario":
                                usuario.setDniUsuario(Lector.nextInt());
                                break;
                            case "mailUsuario":
                                usuario.setMailUsuario(Lector.nextString());
                                break;
                            case "fechaNacimientoUsuario":
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                                Date fechaNacimiento=df.parse(Lector.nextString());
                                Log.d("Usuario",""+fechaNacimiento.toString());
                                usuario.setFechaNacimiento(fechaNacimiento);
                                break;
                                default:
                                Lector.skipValue();
                                break;
                        }
                    }
                    usuarios.add(usuario);
                    Lector.endObject();
                } else {
                    if (siguienteNombre.equals("Direcciones")) {
                        Lector.beginArray();
                        while (Lector.hasNext()) {
                            Lector.beginObject();
                            Direccion direccion=new Direccion();
                            while (Lector.hasNext()) {
                                switch (Lector.nextName()) {
                                    case "direccion":
                                        direccion.setDireccion(Lector.nextString());
                                        break;
                                    case "entre1":
                                        direccion.setEntre1(Lector.nextString());
                                        break;
                                    case "entre2":
                                        direccion.setEntre2(Lector.nextString());
                                        break;
                                    case "etiqueta":
                                        direccion.setEtiqueta(Lector.nextString());
                                        break;
                                    case "lat":
                                        direccion.setLat(Lector.nextDouble());
                                        break;
                                    case "lon":
                                        direccion.setLon(Lector.nextDouble());
                                        break;
                                    default:
                                        Lector.skipValue();
                                        break;
                                }
                            }
                            ListaDireccionesUsuario.add(direccion);
                            Lector.endObject();
                        }
                        Lector.endArray();
                        usuario.setDireccionesUsuario(ListaDireccionesUsuario);
                        ListaDireccionesUsuario=new ArrayList<>();
                    }else{
                        Lector.skipValue();
                    }
                }
            }
            Lector.endObject();
        } catch (Exception error) {
            Log.d("Error", "Error: " + error.getMessage());
        }
    }
    @Override
    protected void onPostExecute(ArrayList<Usuario> usuarios) {
        finalizada.TareaFinalizada(usuarios);
    }
}
