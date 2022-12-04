package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Services.ProductService;

import java.nio.charset.StandardCharsets;

public class FormActivity extends AppCompatActivity {
    private ProductService productService;
    private DBHelper dbHelper;
    private ImageView formImage;
    private EditText editFormName, editFormDescription;
    private Button btnFormProduct;
    ActivityResultLauncher<String> content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);
        editFormName = (EditText) findViewById(R.id.editFormName);
        editFormDescription = (EditText) findViewById(R.id.editFormDescription);
        formImage = (ImageView) findViewById(R.id.formImage);
        try {
            productService = new ProductService();
            dbHelper = new DBHelper(this);
        }catch (Exception e){
            Log.e("error db", e.toString());
        }
        byte[] img = "".getBytes(StandardCharsets.UTF_8);

        content =registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        formImage.setImageURI(result);
                    }
                }
        );
        formImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.launch("image/*");
            }
        });
        btnFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insertData(
                        editFormName.getText().toString(),
                        editFormDescription.getText().toString(),
                        productService.imageviewToByte(formImage)
                );
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
    }
}