package pablo_lucas.com.aprendefacil.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pablo_lucas.com.aprendefacil.R;

public class HistoriaActivity extends AppCompatActivity {

    Button b_V1, b_V2, b_V3, b_V4;
    String l1, l2, l3, l4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        b_V1 = (Button) findViewById(R.id.b_V1);
        b_V2 = (Button) findViewById(R.id.b_V2);
        b_V3 = (Button) findViewById(R.id.b_V3);
        b_V4 = (Button) findViewById(R.id.b_V4);
        l1= "https://www.youtube.com/watch?v=W-I1W2z9ZlI";
        l2="https://www.youtube.com/watch?v=KNbPtmh18qw";
        l3="https://www.youtube.com/watch?v=gp1BawNVpZ8";
        l4="https://www.youtube.com/watch?v=ZMsrqDSfSNI";

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
