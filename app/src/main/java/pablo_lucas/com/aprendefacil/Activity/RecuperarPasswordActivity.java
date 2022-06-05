package pablo_lucas.com.aprendefacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pablo_lucas.com.aprendefacil.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//https://firebase.google.com/docs/auth/android/manage-users#send_a_password_reset_email
public class RecuperarPasswordActivity extends AppCompatActivity {

    Button btnRecuperar;
    EditText etEmail;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        etEmail = findViewById(R.id.etEmail);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        auth = FirebaseAuth.getInstance();

    }

    public void recuperarPassword(View v){
        String correo = etEmail.getText().toString();


        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){

            auth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(v.getContext(),"El correo ha sido enviado, revisa tu bandeja",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(v.getContext(),"Ha ocurrido un error al enviar el correo, intentalo nuevamente",Toast.LENGTH_LONG).show();
                    }
                }
            });
            
        }else{
            Toast.makeText(v.getContext(),"Sin conexión a internet",Toast.LENGTH_LONG).show();
        }


    }

}