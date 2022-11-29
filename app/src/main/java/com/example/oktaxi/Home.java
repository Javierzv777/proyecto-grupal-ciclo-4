package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    private Button btnProduct1, btnProduct2, btnProduct3;
    private TextView textProduct1, textProduct2, textProduct3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnProduct1 = (Button) findViewById(R.id.btnProduct1);
        btnProduct2 = (Button) findViewById(R.id.btnProduct2);
        btnProduct3 = (Button) findViewById(R.id.btnProduct3);
        textProduct1 = (TextView) findViewById(R.id.textProduct1);
        textProduct2 = (TextView) findViewById(R.id.textProduct2);
        textProduct3 = (TextView) findViewById(R.id.textProduct3);

        btnProduct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Product.class);
                intent.putExtra("title", textProduct1.getText().toString());
                intent.putExtra("imageCode", R.drawable.bog);
                intent.putExtra("description", "description product 1");
                startActivity(intent);
            }
        });
        btnProduct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Product.class);
                intent.putExtra("title", textProduct2.getText().toString());
                intent.putExtra("imageCode", R.drawable.med);
                intent.putExtra("description", "description product 2");
                startActivity(intent);
            }
        });
        btnProduct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Product.class);
                intent.putExtra("title", textProduct3.getText().toString());
                intent.putExtra("imageCode", R.drawable.cali);
                intent.putExtra("description", "description product 3");
                startActivity(intent);
            }
        });
    }
}