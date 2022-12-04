package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Services.ProductService;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private DBHelper dbHelper;
    private ProductService productService;
    private ListView listViewProducts;
    private ArrayList<Producto> arrayProducts;
    private ProductAdapter productAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayProducts =  new ArrayList<>();

        try {
            dbHelper = new DBHelper(this);
            //byte[] img = "".getBytes();
            //dbHelper.insertData("bogotá", "ciudad bonita", img);
            //dbHelper.insertData("Medellín", "ciudad montañosa pero cool", img);
            //dbHelper.insertData("Cali", "ciudad parcera", img);


            productService = new ProductService();
            Cursor cursor = dbHelper.getData();
            arrayProducts = productService.cursorToArray(cursor);
            Toast.makeText(this, "insert OK", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error lectura DB", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_home);


        //Producto bogota = new Producto("Bogotá", "capital de Colombia", R.drawable.bog );
        //Producto medellin = new Producto("Medellín", "ciudad de montañas", R.drawable.med );
        //Producto cali = new Producto("Cali", "bellas mujeres", R.drawable.cali );
        //arrayProducts.add(bogota);
        //arrayProducts.add(medellin);
        //arrayProducts.add(cali);

        productAdapter = new ProductAdapter(   getApplicationContext(), arrayProducts);
        listViewProducts = (ListView) findViewById(R.id.listViewProduct);
        listViewProducts.setAdapter(productAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionAdd:
                Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}