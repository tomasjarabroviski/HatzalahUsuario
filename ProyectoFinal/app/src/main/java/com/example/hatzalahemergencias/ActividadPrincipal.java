package com.example.hatzalahemergencias;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ActividadPrincipal extends AppCompatActivity {
    int idDireccion;
    FirebaseUser currentUser;

    private String idFBF;

    public String getIdFBF() {
        return idFBF;
    }

    public void setIdFBF(String idFBF) {
        this.idFBF = idFBF;
    }

    private int idRescatista2=-1;

    public int getIdRescatista2() {
        return idRescatista2;
    }

    public void setIdRescatista2(int idRescatista2) {
        this.idRescatista2 = idRescatista2;
    }

    public void setIdRescatista1(int idRescatista1) {
        this.idRescatista1 = idRescatista1;
    }

    private int idRescatista1;

    public int getIdRescatista() {
        return idRescatista1;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    FragmentManager adminFragment;
    FragmentTransaction transacFragment;

    private Usuario usuario;
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setFamiliaresUsuario(ArrayList<Usuario> familiaresUsuario) {
        FamiliaresUsuario = familiaresUsuario;
    }

    ArrayList<Usuario> FamiliaresUsuario;
    Usuario usuarioEmergencia;
    int miIdUsuario;
    Bundle Resultado;
    ProgressDialog dialog;

    private String EstadoABM="";

    public String getEstadoABM() {
        return EstadoABM;
    }

    public void setEstadoABM(String estadoABM) {
        EstadoABM = estadoABM;
    }

    public Usuario getUsuarioEmergencia() {
        return usuarioEmergencia;
    }

    public void setUsuarioEmergencia(Usuario usuarioEmergencia) {
        this.usuarioEmergencia = usuarioEmergencia;
    }

    public ArrayList<Usuario> getFamiliaresUsuario() {
        return FamiliaresUsuario;
    }

    public void setIdDireccionPrincipal (int miId){
        idDireccion = miId;
    }

    public int getDireccionPrincipal(){
        return idDireccion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsuarioInsert miUsuario = new UsuarioInsert();
        Usuario myUsuario = new Usuario();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        adminFragment = getSupportFragmentManager();

        /*dialog= ProgressDialog.show(this, "Cargando",
                "Cargando. porfavor espere...", true);
        dialog.show();
*/
        currentUser=getIntent().getParcelableExtra("Usuario");

        Resultado =this.getIntent().getExtras();
        miIdUsuario = Resultado.getInt("idUsuario");

        Log.d("usuario", "Llego usuario " + miIdUsuario);

        if(currentUser!=null) {
            final frag_actividad_principal frgInicio = new frag_actividad_principal();
            transacFragment = adminFragment.beginTransaction();
            transacFragment.replace(R.id.alojadorDeFragment, frgInicio);
            transacFragment.commit();
            FamiliaresUsuario = new ArrayList<>();
            TareaAsincronaTraerUsuario traerUsuario = new TareaAsincronaTraerUsuario(new TareaFinalizada() {
                @Override
                public void TareaFinalizada(ArrayList<Usuario> user) {
                    if(user.size()!=0) {
                        usuario = user.get(0);
                        TareaAsincronaTraerCirculo tareaAsincronaTraerCirculo=new TareaAsincronaTraerCirculo(new TareaCirculoFinalizada() {
                            @Override
                            public void TareaCirculoFinalizada(final ArrayList<Integer> Familiares) {
                                if (!Familiares.isEmpty()) {
                                    TareaAsincronaTraerUsuario traerUsuario = new TareaAsincronaTraerUsuario(new TareaFinalizada() {
                                        @Override
                                        public void TareaFinalizada(ArrayList<Usuario> user) {
                                            if (!user.isEmpty()) {
                                                FamiliaresUsuario = user;
                                            } else {
                                                FamiliaresUsuario = new ArrayList<>();
                                            }
                                            frgInicio.HabilitarBotones();
                                        }
                                    });
                                    Integer[] prueba = new Integer[Familiares.size()];
                                    for (int x = 0; x < Familiares.size(); x++) {
                                        prueba[x] = Familiares.get(x);
                                    }
                                    traerUsuario.execute(prueba);
                                }else{
                                    frgInicio.HabilitarBotones();
                                    FamiliaresUsuario=new ArrayList<>();;
                                }
                            }
                        });
                        tareaAsincronaTraerCirculo.execute(usuario.getIdUsuario());

                        //                        dialog.cancel();
                        Log.d("usuario", "Llego usuario " + usuario.getNombreUsuario());
                    }else{
                    AlertDialog.Builder compiladorAlerta = new AlertDialog.Builder(ActividadPrincipal.this);
                    compiladorAlerta.setMessage("Error de conexion con el servidor");
                    compiladorAlerta.setCancelable(true);
                    compiladorAlerta.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    AlertDialog Alerta = compiladorAlerta.create();
                    Alerta.show();
                    }
                }
            });
            traerUsuario.execute(miIdUsuario);
        }
    }

    public void GestionarDirecciones() {
        Log.d("Gestion Usuario", "Llegue Aca");
        Fragment frGestionABMDirecciones;
        frGestionABMDirecciones = new frag_abm_direcciones();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frGestionABMDirecciones);
        transacFragment.addToBackStack(null);
        transacFragment.commit();

    }

    public void DatosemergenciaRescatista(){
        Log.d("FragEmergencia", "Llegue Aca");
        Fragment frgEmergencai;
        frgEmergencai = new frag_Emergencia_Pedida();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frgEmergencai);
        transacFragment.addToBackStack(null);
        transacFragment.commitAllowingStateLoss();

    }


    public void GestionarFichaMedeica() {
        //ACAAA
        Log.d("Gestion Usuario", "Llegue Aca");
        Fragment frGestionFichaMedica;
        frGestionFichaMedica = new frag_abm_fichamedica();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frGestionFichaMedica);
        transacFragment.addToBackStack(null);
        transacFragment.commit();

    }

    public void GestionarCirculo() {
        Log.d("Gestion Usuario", "Me voy a gestionar el circulo");
        Fragment frGestionCirculoFamiliares;
        frGestionCirculoFamiliares = new frag_abm_circulo();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frGestionCirculoFamiliares);
        transacFragment.addToBackStack(null);
        transacFragment.commit();

    }

    public void ProcesarGestionMenu() {
        Fragment frGestion;
        frGestion = new frag_gestion_usuario();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frGestion);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }

    public void IrAlFormDeDirecciones() {
        Fragment frFromDirecciones;
        frFromDirecciones = new frag_abm_direcciones_form();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frFromDirecciones);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }

    public void GestionarUsuario(){
        Log.d("Gestion Usuario", "Me voy a gestionar el usuario");
        Fragment frgGestionarDatosUsuarios;
        frgGestionarDatosUsuarios = new frag_gestion_usuario_datos_usuario();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frgGestionarDatosUsuarios);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }

    public void ProcesarEmergencia(){
        if(!FamiliaresUsuario.isEmpty()) {
            Fragment frgRedCOntacto;
            frgRedCOntacto = new frag_red_contacto();
            transacFragment = adminFragment.beginTransaction();
            transacFragment.replace(R.id.alojadorDeFragment, frgRedCOntacto);
            transacFragment.addToBackStack(null);
            transacFragment.commit();
        }else{
            usuarioEmergencia = usuario;
            Fragment frgSolicitarRescatista;
            frgSolicitarRescatista = new frag_solicitar_rescatista();
            transacFragment = adminFragment.beginTransaction();
            transacFragment.replace(R.id.alojadorDeFragment, frgSolicitarRescatista);
            transacFragment.addToBackStack(null);
            transacFragment.commit();
        }
    }

    public void CirculoDeEmergenciaFormulario(){
        Fragment fragmentABMCirculoForm=new frag_abm_circulo_form();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, fragmentABMCirculoForm);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }

    public void RedContactos(int idContacto){
        Log.d("FragMenu" , "Llegue a red Contactos con el id: "+idContacto);
        // vincular con solicitar paramedico
        if(idContacto==0) {
            usuarioEmergencia=usuario;
        }else
        {
            usuarioEmergencia=FamiliaresUsuario.get(idContacto-1);
        }
        Fragment frgInicio = new frag_solicitar_rescatista();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frgInicio);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }

    public void CambiarContrasena(){
        Fragment frgInicio = new frag_gestion_cambio_contrasena();
        transacFragment = adminFragment.beginTransaction();
        transacFragment.replace(R.id.alojadorDeFragment, frgInicio);
        transacFragment.addToBackStack(null);
        transacFragment.commit();
    }


}
