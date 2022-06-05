package pablo_lucas.com.aprendefacil.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;
import pablo_lucas.com.aprendefacil.R;

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreo,txtContraseña;
    private Button btnLogin,btnRegistro;
    private FirebaseAuth mAuth;// ...

    Button idRecupera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = (EditText)findViewById(R.id.idCorreoLogin);
        txtContraseña = (EditText)findViewById(R.id.idContraseñaLogin);
        btnLogin = (Button) findViewById(R.id.idLoginLogin);
        btnRegistro = (Button) findViewById(R.id.idRegistroLogin);

        idRecupera = findViewById(R.id.idRecupera);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtCorreo.getText().toString();
                if (isValidEmail(correo) && validadContraseña()){
                String contraseña = txtContraseña.getText().toString();
                    mAuth.signInWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this,"Ingreso exitoso",Toast.LENGTH_SHORT).show();
                                    nextActivity();
                                    } else {
                                    Toast.makeText(LoginActivity.this,"Error, contraseña incorrecta.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }else {
                    Toast.makeText(LoginActivity.this,"Correo o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistroActivity.class));            }
        });

        //UsuarioDAO.getInstancia().añadirFotoDePerfilALosUsuariosQueNoTienenFoto();

        idRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),RecuperarPasswordActivity.class);
                startActivity(intent);
            }
        });
    //COMPROBANDO CONEXIÓN A INTERNET
        //https://developer.android.com/training/monitoring-device-state/connectivity-status-type?hl=es-419#java
/*        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            Toast.makeText(this,"Conectado a internet",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Sin conexión a internet",Toast.LENGTH_LONG).show();
        }*/
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validadContraseña(){
        String contraseña;
        contraseña = txtContraseña.getText().toString();
            if (contraseña.length()>=6 && contraseña.length()<=16){
                return true;
            }else return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Toast.makeText(this,"Usuario Logueado",Toast.LENGTH_SHORT).show();
            nextActivity();
        }

    }

    private void nextActivity(){
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
    //    startActivity(new Intent(LoginActivity.this, MensajeriaActivity.class));
        finish();
    }
}
