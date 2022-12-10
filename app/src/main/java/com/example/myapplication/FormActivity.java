package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.DB.DBFirebase;
import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Services.ProductService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {
    private ProductService productService;
    private DBFirebase dbFirebase;
    private DBHelper dbHelper;
    private ImageView formImage;
    private EditText editFormName, editFormDescription, editIdFormProduct;
    private Button btnFormProduct, btnGet, btnUpdate, btnDelete;
    ActivityResultLauncher<String> content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editFormName = (EditText) findViewById(R.id.editFormName);
        editIdFormProduct = (EditText) findViewById(R.id.editIdFormProduct);
        editFormDescription = (EditText) findViewById(R.id.editFormDescription);
        formImage = (ImageView) findViewById(R.id.formImage);
        try {
            productService = new ProductService();
            dbHelper = new DBHelper(this);
            dbFirebase = new DBFirebase();
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

                try {
                    Producto product = new Producto(
                            editFormName.getText().toString(),
                            editFormDescription.getText().toString(),
                            productService.imageviewToByte(formImage));
                    dbFirebase.insertData(product);
                     dbHelper.insertData(product);

                    //
                }catch (Exception e){
                    Log.e("DB Insert", e.toString());
                }


                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0) {
                    ArrayList<Producto> list = productService.cursorToArray(dbHelper.getDataById(id));

                    list.add(dbFirebase.getDataById(id));

                    if(list.size() != 0){
                        Producto product = list.get(0);
                        editFormDescription.setText(product.getDescription());
                        editFormName.setText(product.getName());
                        formImage.setImageBitmap(productService.byteToBitmap(product.getImage()));
                    }else{
                        Toast.makeText(getApplicationContext(),"no existe", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"ingrese Id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0) {
                    dbHelper.deleteDataById(id);
                    clean();
                } else {
                    Toast.makeText(getApplicationContext(),"ingrese Id a eliminar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0) {
                    dbHelper.updateDataById(
                            id,
                            editFormName.getText().toString(),
                            editFormDescription.getText().toString(),
                            productService.imageviewToByte(formImage));
                }
                clean();

            }
        });


    }
    public void clean (){
        editFormDescription.setText("");
        editFormName.setText("");
        formImage.setImageResource(R.drawable.empty);
    }
}