package pablo_lucas.com.aprendefacil.Persistencia;

import android.net.Uri;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import pablo_lucas.com.aprendefacil.Entidades.FireBase.Usuario;
import pablo_lucas.com.aprendefacil.Entidades.Logica.LUsuario;
import pablo_lucas.com.aprendefacil.Utilidades.Constantes;

//PERSISTENCIA se encarga para guardar y cargar datos
//DAO == Data Access Object / Objeto de Acceso a Datos
public class UsuarioDAO {

    public  interface IDevolverUsuario{
        public void devolverUsuario(LUsuario lUsuario);
        public void devolverError(String error);
    }

    public interface IDevolverUrlFoto{
        public void devolverUrlString(String url);
    }

    private static UsuarioDAO usuarioDAO;
    private FirebaseDatabase database;
    private DatabaseReference referenciaUsuarios;
    private FirebaseStorage storage;
    private StorageReference referenceFotoDePerfil;

    public static UsuarioDAO getInstancia(){
        if (usuarioDAO==null){
        usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    private UsuarioDAO(){
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        referenciaUsuarios = database.getReference(Constantes.NODO_USUARIOS);
        referenceFotoDePerfil = storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }

    public  static String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }

    public boolean siUsurioLogeado(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /*if(firebaseUser!=null){
            return true;
        }return false;*/
        return firebaseUser!=null;
    }

    public long fechaDeCreacion(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }

    public long fechaDeUltimaVezQueSeLogeoLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }

    public void obtenerInformacionDeUsuarioPorLlave(final String key, final IDevolverUsuario iDevolverUsuario){
        referenciaUsuarios.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                LUsuario lUsuario = new LUsuario(key,usuario);
                iDevolverUsuario.devolverUsuario(lUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iDevolverUsuario.devolverError(databaseError.getMessage());
            }
        });
    }

    public void añadirFotoDePerfilALosUsuariosQueNoTienenFoto(){
        referenciaUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LUsuario> lUsuariosLista = new ArrayList<>();
                for (DataSnapshot childDataSnapShot : dataSnapshot.getChildren()){
                    Usuario usuario = childDataSnapShot.getValue(Usuario.class);
                    LUsuario lUsuario = new LUsuario(childDataSnapShot.getKey(),usuario);
                    lUsuariosLista.add(lUsuario);
                }

                for(LUsuario lUsuario : lUsuariosLista){
                    if(lUsuario.getUsuario().getFotoPerfilURL()==null){
                       referenciaUsuarios.child(lUsuario.getKey()).child("fotoPerfilURL").setValue(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void subirFotoUri(Uri uri, final IDevolverUrlFoto iDevolverUrlFoto){

       //Uri u = data.getData();
        // storageReference = storage.getReference("imagenes_chat");
        String nombreFoto="";
        Date date = new Date();
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("SSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombreFoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = referenceFotoDePerfil.child(nombreFoto);

        fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                   throw task.getException();
               }
               return fotoReferencia.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri = task.getResult();
                    iDevolverUrlFoto.devolverUrlString(uri.toString());
                }
            }
        });



    }


}
