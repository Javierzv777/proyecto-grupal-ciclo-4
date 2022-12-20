package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FormActivity extends AppCompatActivity implements ComeBackHome{
    private ProductService productService;
    private DBFirebase dbFirebase;
    private DBHelper dbHelper;
    private ImageView formImage;
    private EditText editFormName, editFormDescription, editIdFormProduct;
    private Button btnFormProduct, btnGet, btnUpdate, btnDelete;
    private ComeBackHome comeBackHome;
    private String uuid;
    private TextView editLat, editLon;
    private Button mapForm;
    private String imagen;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static final int GALLERY_INTENT = 1;
    private ProgressDialog progressDialog;

    ActivityResultLauncher<String> content;
    @SuppressLint("MissingInflatedId")
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
        editLat = (EditText) findViewById(R.id.editLat);
        editLon = (EditText) findViewById(R.id.editLon);
        mapForm = (Button) findViewById(R.id.mapForm);
        imagen = "";
        storage = FirebaseStorage.getInstance("gs://ciclo4-3f107.appspot.com");
        storageRef = storage.getReference();
        progressDialog = new ProgressDialog(this);

        StorageReference pathReference = storageRef.child("images/stars.jpg");
        Intent intentIn = getIntent();
        editFormName.setText( intentIn.getStringExtra("nombre")) ;
        editFormDescription.setText(intentIn.getStringExtra("descripcion"));
        imagen = intentIn.getStringExtra("imagen");
        progressDialog.setTitle("Subiendo...");
        progressDialog.setMessage("Subiendo imagen a firebase");
        progressDialog.setCancelable(false);

        try{
            if(!imagen.equals("")) {
                progressDialog.show();
                Toast.makeText(getApplicationContext(),"cargando foto", Toast.LENGTH_SHORT).show();
                Picasso.with(getApplicationContext())
                        .load(imagen)
                        .resize(300, 300)
                        .into(formImage);
                progressDialog.dismiss();
            }
        }catch(Exception e) {
            Log.e("Fatal error", e.toString());
        }

        Intent intent = getIntent();
        if(intent != null) {
           String latitud = intent.getStringExtra("latitud");
           String longitud = intent.getStringExtra("longitud");
           if(latitud != "" && longitud != "") {
               editLat.setText(latitud);
               editLon.setText(longitud);
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


        formImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_PICK);
                intent.setType( "image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });
        btnFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Producto product = new Producto(
                            editFormName.getText().toString(),
                            editFormDescription.getText().toString(),
                            imagen,
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

        mapForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Maps.class);



                intent.putExtra("imagen", imagen );
                intent.putExtra("nombre", editFormName.getText().toString());
                intent.putExtra("descripcion", editFormDescription.getText().toString());
                intent.putExtra("consulta", "consulta" );
                startActivity(intent);
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0) {
                    ArrayList<Producto> list = productService.cursorToArray(dbHelper.getDataById(id), getApplicationContext());

                    //list.add(dbFirebase.getDataById(id));

                    if(list.size() != 0){
                        Producto product = list.get(0);
                        editFormDescription.setText(product.getDescription());
                        editFormName.setText(product.getName());
                        uuid = product.getId();
                        //formImage.setImageBitmap(productService.byteToBitmap(product.getImage()));
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

                if(id.compareTo("") != 0 && uuid.compareTo("") != 0) {
                    //dbHelper.deleteDataById(id);
                    dbFirebase.deleteDataById( id, uuid, dbHelper, comeBackHome);
                    //clean();
                } else {
                    Toast.makeText(getApplicationContext(),"ingrese Id a eliminar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0 && uuid.compareTo("") != 0) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            progressDialog.setTitle("Subiendo...");
            progressDialog.setMessage("Subiendo imagen a firebase");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Uri uri = data.getData();
            //formImage.setImageURI(uri);
            StorageReference filePath = storageRef.child("fotos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> subirFoto = taskSnapshot.getStorage().getDownloadUrl();

                        subirFoto.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imagen = uri.toString();
                                try{
                                    if(!imagen.equals("")) {
                                        Toast.makeText(getApplicationContext(),"cargando foto", Toast.LENGTH_SHORT).show();
                                        Picasso.with(getApplicationContext())
                                                .load(imagen)
                                                .resize(300, 300)
                                                .into(formImage);
                                        progressDialog.dismiss();
                                    }
                                }catch(Exception e) {
                                    Log.e("Fatal error", e.toString());
                                }


                            }
                        });

                    Toast.makeText(getApplicationContext(), "se subio la foto", Toast.LENGTH_SHORT).show();
                }
            });
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