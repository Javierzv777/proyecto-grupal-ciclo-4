package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Services.ProductService;

import java.util.ArrayList;

public class Product extends AppCompatActivity {
    private DBHelper dbHelper;
    private ProductService productService;
    private Button btnProductInfo;
    private TextView textProductTitle, textProductDes;
    private ImageView imgProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        btnProductInfo = (Button) findViewById(R.id.btnProductInfo);
        textProductTitle = (TextView) findViewById(R.id.textProductTitle);
        textProductDes = (TextView) findViewById(R.id.textProductDes);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        dbHelper = new DBHelper(this);
        productService = new ProductService();

        Intent intentIn = getIntent();
        String id = intentIn.getStringExtra("id");
        ArrayList<Producto> list = productService.cursorToArray(dbHelper.getDataById(id));
        Producto product = list.get(0);

        textProductTitle.setText(product.getName());
        textProductDes.setText(product.getDescription()); 
        imgProduct.setImageBitmap(productService.byteToBitmap(product.getImage()));

        btnProductInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}