package pablo_lucas.com.aprendefacil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EncuestasActivity extends AppCompatActivity {

    private EditText Para, Encabezado, Contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas);

        Para = (EditText) findViewById(R.id.et_Para);
        Encabezado = (EditText) findViewById(R.id.et_Encabezado);
        Contenido = (EditText) findViewById(R.id.et_Contenido);

        Para.setVisibility(View.INVISIBLE);
    }

    public void Send (View s) {
        sendEmail();
    }

    private void sendEmail() {
        String recipeientList = Para.getText().toString();
        String[] recipients = recipeientList.split(",");

        String subject = Encabezado.getText().toString();
        String message = Contenido.getText().toString();

        Intent intentEncuestas = new Intent(Intent.ACTION_SEND);
        intentEncuestas.putExtra(Intent.EXTRA_EMAIL, recipients);
        intentEncuestas.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentEncuestas.putExtra(Intent.EXTRA_TEXT, message);

        intentEncuestas.setType("message/rfc822");
        startActivity(Intent.createChooser(intentEncuestas, "Selecciona una aplicación para enviar"));
    }
}
