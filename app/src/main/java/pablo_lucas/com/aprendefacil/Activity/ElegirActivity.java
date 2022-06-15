package pablo_lucas.com.aprendefacil.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pablo_lucas.com.aprendefacil.R;
import pablo_lucas.com.aprendefacil.SoporteActivity;

public class ElegirActivity extends AppCompatActivity {

    Button espanol;
    Button matematicas;
    Button ciencias;
    Button historia;
    Button geografia;
    Button ingles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir);

        espanol = (Button) findViewById(R.id.b_Espanol);
        matematicas = (Button) findViewById(R.id.b_Matematicas);
        ciencias = (Button) findViewById(R.id.b_Ciencias);
        historia = (Button) findViewById(R.id.b_Historia);
        geografia = (Button) findViewById(R.id.b_Geografia);
        ingles = (Button) findViewById(R.id.b_Ingles);

        espanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EspanolActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        matematicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MatematicasActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        ciencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CienciasActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HistoriaActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        geografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GeografiaActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        ingles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InglesActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

}
