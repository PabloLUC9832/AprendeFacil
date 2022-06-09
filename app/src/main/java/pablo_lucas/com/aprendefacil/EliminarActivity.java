package pablo_lucas.com.aprendefacil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pablo_lucas.com.aprendefacil.Activity.LoginActivity;
import pablo_lucas.com.aprendefacil.Activity.MenuActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EliminarActivity extends AppCompatActivity {

    Button btnEliminar;

    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnEliminar = findViewById(R.id.btnEliminarCuenta);

    }

    public void eliminar(View v){
        Bundle datos = getIntent().getExtras();
        String mIdUser = datos.getString("mIdUser");

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                        mDatabase.child("Usuarios").child(mIdUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"de realtime",Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                    //finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),"no se elimino de realtime: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Toast.makeText(getApplicationContext(),"La cuenta se ha eliminado correctamente. Te echaremos de menos :(",Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                        //finish();

                    }else{
                        Toast.makeText(v.getContext(),"Ha ocurrido un error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });
        }else{
            Toast.makeText(v.getContext(),"Sin conexión a internet",Toast.LENGTH_LONG).show();
        }

    }

    public void inicio(View v){
        startActivity(new Intent(EliminarActivity.this,LoginActivity.class));
        finish();
    }


}