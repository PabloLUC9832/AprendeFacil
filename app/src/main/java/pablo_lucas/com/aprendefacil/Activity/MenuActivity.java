package pablo_lucas.com.aprendefacil.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;
import pablo_lucas.com.aprendefacil.R;

public class MenuActivity extends AppCompatActivity {

    private Button btnMiPerfil;
    private Button btnCerrarSesion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnMiPerfil = findViewById(R.id.btnMiPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), PerfilActivity.class);
                startActivity(intent);
            }
        });

        //btnMiPerfil.setOnClickListener(perfil());

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

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
