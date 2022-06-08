package pablo_lucas.com.aprendefacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import pablo_lucas.com.aprendefacil.EliminarActivity;
import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;
import pablo_lucas.com.aprendefacil.R;
import pablo_lucas.com.aprendefacil.SoporteActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PerfilActivity extends AppCompatActivity{

    private CircleImageView fotoPerfil;
    private TextView tvID, tvFechaCreacion,tvNombre,tvCorreo,tvGenero,tvFechaNacimiento;
    //private Button btnActualizar;
    //private EditText idCorreo;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    private String idUser;
    FirebaseUser user2 ;

    //https://firebase.google.com/docs/database/android/read-and-write?hl=es-419
    //ENVIO DE DATOS A LA ACTIVITY DE ACTUALIZAR
    String nombre,correo,genero,fotoPerfilURL,fechaDeNacimiento;
    String mId ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);



        tvID = findViewById(R.id.tvID);
        mId = FirebaseAuth.getInstance().getUid();
        tvID.setText(FirebaseAuth.getInstance().getUid());

        tvNombre = findViewById(R.id.tvNombre);
        tvFechaCreacion = findViewById(R.id.tvFechaCreacion);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvGenero = findViewById(R.id.tvGenero);
        fotoPerfil = findViewById(R.id.fotoPerfil);
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user =mAuth.getCurrentUser();
        idUser = mAuth.getCurrentUser().getUid();
        user2 = mAuth.getCurrentUser();
        //String miId = user.getUid();
        //String fechaCreacion = (String) FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
        Date date = new Date(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp());
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        //prettyTime.format(date);
        //tvFechaCreacion.setText("Fecha de creación de la cuenta:  " + FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp() );
        tvFechaCreacion.setText("" + prettyTime.format(date) );

        mDatabase = FirebaseDatabase.getInstance().getReference();
        UsuarioDAO usuarioDAO;
        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
//                    String nombre = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("nombre").getValue().toString();
//                    String correo = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("correo").getValue().toString();
//                    String genero = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("genero").getValue().toString();
//                    String fotoPerfilURL = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("fotoPerfilURL").getValue().toString();
//                    String fechaDeNacimiento =  dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("fechaDeNacimiento").getValue().toString();

                    nombre = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("nombre").getValue().toString();
                    correo = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("correo").getValue().toString();
                    genero = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("genero").getValue().toString();
                    fotoPerfilURL = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("fotoPerfilURL").getValue().toString();
                    fechaDeNacimiento =  dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("fechaDeNacimiento").getValue().toString();

                    Long fechaN =Long.parseLong(fechaDeNacimiento);

                    tvNombre.setText(nombre);
                    tvCorreo.setText(correo);
                    tvGenero.setText(genero);
                    Glide.with(PerfilActivity.this).load(fotoPerfilURL).into(fotoPerfil);

                    Date date = new Date(fechaN);
                    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                    String stringDate= DateFor.format(date);
                    tvFechaNacimiento.setText(stringDate);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SharedPreferences prefe=getSharedPreferences("correo", Context.MODE_PRIVATE);
        SharedPreferences prefe2=getSharedPreferences("password", Context.MODE_PRIVATE);
        String correoA = prefe.getString("correoA","no correo");
        String passwordA = prefe2.getString("passwordA","no password");
        Toast.makeText(this,correoA+"/"+passwordA,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actualizar) {
            Intent intent = new Intent(this,ActualizarPerfilActivity.class);
            intent.putExtra("nombre",nombre);
            intent.putExtra("genero",genero);
            intent.putExtra("correo",correo);
            intent.putExtra("fotoPerfilURL",fotoPerfilURL);
            intent.putExtra("fechaDeNacimiento",fechaDeNacimiento);
            startActivity(intent);
            return true;
        }

        if(id == R.id.eliminar){
            Bundle datos = getIntent().getExtras();
            String mIdUser = datos.getString("idUser");
            Intent intent = new Intent (getApplicationContext(), EliminarActivity.class);
            intent.putExtra("mIdUser",FirebaseAuth.getInstance().getUid());
            startActivity(intent);
            finish();
        }

        if(id == R.id.correoSoporte){
            Intent intent = new Intent (getApplicationContext(), SoporteActivity.class);
            startActivity(intent);
        }
/*            Bundle datos = getIntent().getExtras();
            String mIdUser = datos.getString("idUser");
            new AlertDialog.Builder(this).
                    setTitle("Eliminar mi cuenta").
                    setMessage("¿Estás seguro de eliminar tu cuenta").
                    setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override*/
       //                 public void onClick(DialogInterface dialogInterface, int i) {
                            //FirebaseAuth.getInstance().signOut();
                            //if(mDatabase.child("Usuarios").child(mId).removeValue().isSuccessful()){
                                //FirebaseAuth.getInstance().signOut();
/*                            if (user2.delete().isSuccessful() && mDatabase.child("Usuarios").child(mId).removeValue().isSuccessful()){
                                Toast.makeText(getApplicationContext(),"La cuenta se ha eliminado correctamente. Te echaremos de menos :(",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"ERRRORRRR",Toast.LENGTH_LONG).show();
                            }*/
                            //https://stackoverflow.com/questions/65293485/how-to-delete-user-from-realtime-database-and-firebase-authentication-android-j
/*                                user2.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //FirebaseAuth.getInstance().signOut();
                                            mDatabase.child("Usuarios").child(mIdUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(),"de realtime",Toast.LENGTH_LONG).show();

                                                    }else{
                                                        Toast.makeText(getApplicationContext(),"no se elimino de realtime: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                            Toast.makeText(getApplicationContext(),"La cuenta se ha eliminado correctamente. Te echaremos de menos :(",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);

                                        }else{
                                            Toast.makeText(getApplicationContext(),"Ha ocurrido un error... Vuelve a intentarlo, por favor."+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });*/
                                //Toast.makeText(getApplicationContext(),"La cuenta se ha eliminado correctamente. Te echaremos de menos :(",Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
                                //startActivity(intent);
                               // finish();
                            /*}else{
                                Toast.makeText(getApplicationContext(),"Ha ocurrido un error... Vuelve a intentarlo, por favor.",Toast.LENGTH_LONG).show();
                            }*/

        //                }
            //        }).setNegativeButton("No",null).show();
      //  }

        return super.onOptionsItemSelected(item);
    }



}