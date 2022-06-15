package pablo_lucas.com.aprendefacil.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pablo_lucas.com.aprendefacil.R;

public class InglesActivity extends AppCompatActivity {

    Button b_V1, b_V2, b_V3, b_V4;
    String l1, l2, l3, l4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingles);

        b_V1 = (Button) findViewById(R.id.b_V1);
        b_V2 = (Button) findViewById(R.id.b_V2);
        b_V3 = (Button) findViewById(R.id.b_V3);
        b_V4 = (Button) findViewById(R.id.b_V4);
        l1= "https://www.youtube.com/watch?v=W_LhyqgaRJI";
        l2="https://www.youtube.com/watch?v=jlC2l0kqocE";
        l3="https://www.youtube.com/watch?v=2n7CXoRMsG4";
        l4="https://www.youtube.com/watch?v=AF7_-GxeJeo";

        b_V1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(l1);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        b_V2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(l2);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        b_V3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(l3);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        b_V4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(l4);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }
}
