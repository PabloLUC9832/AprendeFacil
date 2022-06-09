package pablo_lucas.com.aprendefacil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SoporteActivity extends AppCompatActivity {

    private EditText To,Asunto,Msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        To = (EditText)findViewById(R.id.et_To);
        Asunto=(EditText)findViewById(R.id.et_Asun);
        Msg = (EditText)findViewById(R.id.et_Msg);

        To.setVisibility(View.INVISIBLE);

    }

    public void Enviar (View v){
        sendEmail();
    }

    private void sendEmail() {
        String recipientList = To.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject =  Asunto.getText().toString();
        String message = Msg.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Selecciona una aplicación"));

    }

}