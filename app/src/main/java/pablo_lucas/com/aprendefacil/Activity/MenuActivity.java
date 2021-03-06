package pablo_lucas.com.aprendefacil.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;
import pablo_lucas.com.aprendefacil.R;
import pablo_lucas.com.aprendefacil.SoporteActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnMiPerfil;
    private Button btnCerrarSesion;
    private Button btnAprender;
    Button b_dark;
    Button b_light;
    String idUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        b_dark = findViewById(R.id.b_dark);
        b_light = findViewById(R.id.b_light);

        idUser = FirebaseAuth.getInstance().getUid();

        btnMiPerfil = findViewById(R.id.btnMiPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnAprender = findViewById(R.id.btnAprender);

        b_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                finish();
            }
        });

        b_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                finish();
            }
        });

        btnAprender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentA = new Intent (MenuActivity.this, ElegirActivity.class);
                startActivity(intentA);
            }
        });

        btnMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected){
                    Intent intent = new Intent (view.getContext(), PerfilActivity.class);
                    intent.putExtra("idUser",idUser);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(view.getContext(),"Sin conexi??n a internet",Toast.LENGTH_LONG).show();
                }

            }
        });

        //btnMiPerfil.setOnClickListener(perfil());

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected){
                    //Toast.makeText(view.getContext(),"Conectado a internet",Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    returnLogin();
                }else{
                    Toast.makeText(view.getContext(),"Sin conexi??n a internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        Toast.makeText(this,"uid: "+idUser,Toast.LENGTH_LONG).show();

    }


    private void returnLogin(){
        startActivity(new Intent(MenuActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UsuarioDAO.getInstancia().siUsurioLogeado()) {
            //el usuario esta logeado
        }else {
            returnLogin();
        }
    }

//    public void perfil(){
//        Intent i = new Intent(this,PerfilActivity.class);
//        startActivity(i);
//    }

}
