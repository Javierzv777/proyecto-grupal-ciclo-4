package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Services.ProductService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Product extends AppCompatActivity {
    private DBHelper dbHelper;
    private ProductService productService;
    private Button btnProductInfo;
    private TextView textProductTitle, textProductDes;
    private ImageView imgProduct;
    private ProgressDialog progressDialog;



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
        Cursor cursor = dbHelper.getDataById(id);



        if(cursor != null){
            ArrayList<Producto> list = productService.cursorToArray(cursor,Product.this);
            Producto product = list.get(0);

            textProductTitle.setText(product.getName());
            textProductDes.setText(product.getDescription());

            if(!product.getImage().equals("")) {
                try{
                    progressDialog = new ProgressDialog(Product.this);
                    progressDialog.setTitle("Subiendo...");
                    progressDialog.setMessage("Subiendo imagen a firebase");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if(!product.getImage().equals("")) {
                        Picasso.with(Product.this)
                                .load(product.getImage())
                                .resize(300, 300)
                                .into(imgProduct);
                        progressDialog.dismiss();
                    }
                }catch(Exception e) {
                    Log.e("Fatal error", e.toString());
                }
            }

        }



        //imgProduct.setImageBitmap(productService.byteToBitmap(product.getImage()));

        btnProductInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}