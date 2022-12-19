package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DB.DBFirebase;
import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Services.ComeBackHome;
import com.example.myapplication.Services.ProductService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Crud extends AppCompatActivity implements ComeBackHome{
    private ProductService productService;
    private DBFirebase dbFirebase;
    private DBHelper dbHelper;
    private ImageView formImage;
    private EditText editFormName, editFormDescription, editIdFormProduct;
    private Button btnFormProduct;
    private ComeBackHome comeBackHome;
    private String uuid;
    private TextView editLat, editLon;
    private Button mapForm;
    private String imagen;
    private String metodo;


    ActivityResultLauncher<String> content;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);
        editFormName = (EditText) findViewById(R.id.editFormName);
        editIdFormProduct = (EditText) findViewById(R.id.editIdFormProduct);
        editFormDescription = (EditText) findViewById(R.id.editFormDescription);
        formImage = (ImageView) findViewById(R.id.formImage);
        editLat = (EditText) findViewById(R.id.editLat);
        editLon = (EditText) findViewById(R.id.editLon);
        mapForm = (Button) findViewById(R.id.mapForm);
        imagen = "";
        productService = new ProductService();
        dbHelper = new DBHelper(this);


        Intent intentIn = getIntent();
        editFormName.setText( intentIn.getStringExtra("nombre")) ;
        editFormDescription.setText(intentIn.getStringExtra("descripcion"));
        imagen = intentIn.getStringExtra("imagen");

        Intent intent = getIntent();

        metodo = intent.getStringExtra("metodo");
        String latitud = intent.getStringExtra("latitud");
        String longitud = intent.getStringExtra("longitud");
        String id = intent.getStringExtra("id");



            if(latitud != null && longitud != null) {
                editLat.setText(latitud);
                editLon.setText(longitud);
            }

        Cursor cursor = dbHelper.getDataById(id);
        if(cursor != null){
                ArrayList<Producto> list = productService.cursorToArray(cursor, getApplicationContext());

                if(list.size() != 0){
                    Producto product = list.get(0);
                    editFormDescription.setText(product.getDescription());
                    editFormName.setText(product.getName());
                    uuid = product.getId();
                    editLat.setText(String.valueOf(product.getLatitud()));
                    editLon.setText(String.valueOf(product.getLongitud()));
                    //formImage.setImageBitmap(productService.byteToBitmap(product.getImage()));
                }else{
                    Toast.makeText(getApplicationContext(),"no existe", Toast.LENGTH_SHORT).show();
                }
            }
            if(metodo != null) {
                if(metodo.equals("agregar")) {
                    btnFormProduct.setText("Agregar");
                }
                if(metodo.equals("actualizar")) {
                    btnFormProduct.setText("Actualizar");
                }
            }


        comeBackHome = this;
        try {
            productService = new ProductService();
            dbHelper = new DBHelper(this);
            dbFirebase = new DBFirebase();
        }catch (Exception e){
            Log.e("error db", e.toString());
        }
        byte[] img = "".getBytes(StandardCharsets.UTF_8);

        content = registerForActivityResult(
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

        if(metodo != null) {
            if(metodo.compareTo("agregar") == 0) {
                btnFormProduct.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        try {
                            Producto product = new Producto(
                                    editFormName.getText().toString(),
                                    editFormDescription.getText().toString(),
                                    "",
                                    Double.parseDouble(editLat.getText().toString().trim()),
                                    Double.parseDouble(editLon.getText().toString().trim())
                                    //    productService.imageviewToByte(formImage)
                            );

                            dbFirebase.insertData(product, dbHelper, comeBackHome );


                            //
                        }catch (Exception e){
                            Log.e("DB Insert", e.toString());
                        }
                    }
                });
            }
        }



        mapForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Maps.class);


                intent.putExtra("id", id);
                intent.putExtra("imagen", imagen );
                intent.putExtra("nombre", editFormName.getText().toString());
                intent.putExtra("descripcion", editFormDescription.getText().toString());
                intent.putExtra("consulta", "consulta" );
                startActivity(intent);
            }
        });


        if(metodo != null) {
            if(metodo.compareTo("actualizar") == 0) {
                btnFormProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(id.compareTo("") != 0 && uuid.compareTo("") != 0) {
                  /*  dbHelper.updateDataById(
                            id,
                            editFormName.getText().toString(),
                            editFormDescription.getText().toString(),
                            productService.imageviewToByte(formImage));
                            */
                            dbFirebase.updateDataById(
                                    id,
                                    uuid,
                                    editFormName.getText().toString(),
                                    editFormDescription.getText().toString(),
                                    //productService.imageviewToByte(formImage),
                                    dbHelper,
                                    comeBackHome);

                        }
                        //clean();

                    }

                });
            }
        }





    }
    public void clean (){
        editFormDescription.setText("");
        editFormName.setText("");
        formImage.setImageResource(R.drawable.empty);
    }


    @Override
    public void intentToHome() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }
}