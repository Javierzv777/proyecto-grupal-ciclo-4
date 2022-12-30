package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Crud extends OptionsMenuActivity implements ComeBackHome{
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
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static final int GALLERY_INTENT = 1;
    private Toolbar ourToolbar;


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
        storage = FirebaseStorage.getInstance("gs://ciclo4-3f107.appspot.com");
        storageRef = storage.getReference();

        ourToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(ourToolbar);


        Intent intentIn = getIntent();
        editFormName.setText( intentIn.getStringExtra("nombre")) ;
        editFormDescription.setText(intentIn.getStringExtra("descripcion"));
        imagen = intentIn.getStringExtra("imagen");
        try{
            if(!imagen.equals("")) {
                Toast.makeText(getApplicationContext(),"cargando foto", Toast.LENGTH_SHORT).show();
                Picasso.with(getApplicationContext())
                        .load(imagen)
                        .resize(300, 300)
                        .into(formImage);
            }
        }catch(Exception e) {
            Log.e("Fatal error", e.toString());
        }


        Intent intent = getIntent();

        metodo = intent.getStringExtra("metodo");
        String latitud = intent.getStringExtra("latitud");
        String longitud = intent.getStringExtra("longitud");
        String id = intent.getStringExtra("id");
        uuid = intent.getStringExtra("uuid");





            if(latitud != null && longitud != null) {
                editLat.setText(latitud);
                editLon.setText(longitud);
            }
        if(id != null && uuid == null) {
            Cursor cursor = dbHelper.getDataById(id);
            if (cursor != null && metodo.equals("actualizar")) {
                ArrayList<Producto> list = productService.cursorToArray(cursor, getApplicationContext());

                if (list.size() != 0) {
                    Producto product = list.get(0);
                    editFormDescription.setText(product.getDescription());
                    editFormName.setText(product.getName());
                    uuid = product.getId();
                    if(latitud == null && longitud == null) {
                        editLat.setText(String.valueOf(product.getLatitud()));
                        editLon.setText(String.valueOf(product.getLongitud()));
                    } else {
                        editLat.setText(latitud);
                        editLon.setText(longitud);
                    }
                    imagen = product.getImage();
                    //formImage.setImageBitmap(productService.byteToBitmap(product.getImage()));
                    try {
                        if (!imagen.equals("")) {
                            Toast.makeText(getApplicationContext(), "cargando foto", Toast.LENGTH_SHORT).show();
                            Picasso.with(getApplicationContext())
                                    .load(imagen)
                                    .resize(300, 300)
                                    .into(formImage);
                        }
                    } catch (Exception e) {
                        Log.e("Fatal error", e.toString());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "no existe", Toast.LENGTH_SHORT).show();
                }
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

        formImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // content.launch("image/*");
                Intent intent = new Intent (Intent.ACTION_PICK);
                intent.setType( "image/*");
                someActivityResultLauncher.launch(intent);

            }
        });




            if(metodo != null && metodo.compareTo("agregar") == 0) {
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
            }else if(metodo != null && metodo.compareTo("actualizar") == 0) {
                btnFormProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(id.compareTo("") != 0 && uuid.compareTo("") != 0) {

                            dbFirebase.updateDataById(
                                    id,
                                    uuid,
                                    editFormName.getText().toString(),
                                    editFormDescription.getText().toString(),
                                    imagen,
                                    Double.parseDouble(editLat.getText().toString()),
                                    Double.parseDouble(editLon.getText().toString()),
                                    dbHelper,
                                    comeBackHome);

                        }

                    }

                });
            }else {
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
            }




        mapForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Maps.class);

                intent.putExtra("metodo", metodo);
                intent.putExtra("id", id);
                intent.putExtra("uuid", uuid);
                intent.putExtra("imagen", imagen );
                intent.putExtra("nombre", editFormName.getText().toString());
                intent.putExtra("descripcion", editFormDescription.getText().toString());
                intent.putExtra("consulta", "consulta" );
                startActivity(intent);
            }
        });





    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

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
                                                    //  progressDialog.dismiss();
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
            });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

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
                                  //  progressDialog.dismiss();
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