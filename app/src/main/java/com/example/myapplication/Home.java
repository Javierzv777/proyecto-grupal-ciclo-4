package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;


import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.Entities.Producto;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private ListView listViewProducts;
    private ArrayList<Producto> arrayProducts;
    private ProductAdapter productAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Producto producto1 = new Producto("fdfdf","fdfdfd");
        Producto producto2 = new Producto("fffdfdf","fdfwedfd");
        Producto producto3 = new Producto("fdfefdf","fdfdfeed");
        arrayProductos.add(producto1);
        arrayProductos.add(producto2);
        arrayProductos.add(producto3);*/

        arrayProducts =  new ArrayList<>();
        Producto bogota = new Producto("Bogotá", "capital de Colombia", R.drawable.bog );
        Producto medellin = new Producto("Medellín", "ciudad de montañas", R.drawable.med );
        Producto cali = new Producto("Cali", "bellas mujeres", R.drawable.cali );
        arrayProducts.add(bogota);
        arrayProducts.add(medellin);
        arrayProducts.add(cali);

        productAdapter = new ProductAdapter(   getApplicationContext(), arrayProducts);
        listViewProducts = (ListView) findViewById(R.id.listViewProduct);
        listViewProducts.setAdapter(productAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}