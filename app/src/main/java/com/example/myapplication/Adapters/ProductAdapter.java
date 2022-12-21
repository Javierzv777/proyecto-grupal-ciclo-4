package com.example.myapplication.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.myapplication.DB.DBFirebase;
import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Home;
import com.example.myapplication.Maps;
import com.example.myapplication.Product;
import com.example.myapplication.Crud;
import com.example.myapplication.R;
import com.example.myapplication.Services.ComeBackHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter implements ComeBackHome {
    private Context context;
    private ArrayList<Producto> arrayProducts;
    private DBFirebase dbFirebase;
    private DBHelper dbHelper;
    ComeBackHome comeBackHome = this;
    public ProgressDialog progressDialog;
    private ProgressBar progressBar;


    public ProductAdapter(Context context,ArrayList<Producto> arrayProducts ) {
        this.arrayProducts = arrayProducts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View v = view;
        v = layoutInflater.inflate(R.layout.templete_product, null);


        Producto producto = arrayProducts.get(i);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        Button btnProductTemplate = (Button) v.findViewById(R.id.btnProductTemplate);
        Button btnEdit = (Button) v.findViewById(R.id.btnEdit);
        Button btnDelete =  (Button) v.findViewById(R.id.btnDelete);
        Button btnLocation =  (Button) v.findViewById(R.id.btnLocation);
        ImageView imgProduct = (ImageView) v.findViewById(R.id.imgProduct);
        TextView textViewNam = (TextView) v.findViewById(R.id.textProductTitle);
        TextView textViewDes = (TextView) v.findViewById(R.id.textProductDes);


        String imagen = producto.getImage();
        //progressBar.setVisibility(View.VISIBLE);


            if(!imagen.equals("")) {

                try{

                Picasso.with(viewGroup.getContext())
                        .load(imagen)
                        .resize(300, 300)
                        .into(imgProduct,  new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                textViewNam.setText(producto.getName());
                                textViewDes.setText(producto.getDescription());
                                progressBar.setVisibility(View.GONE);


                            }

                            @Override
                            public void onError() {
                                textViewNam.setText(producto.getName());
                                textViewDes.setText(producto.getDescription());
                                progressBar.setVisibility(View.GONE);
                            }
                        });





                }catch(Exception e) {
                    Log.e("Fatal error", e.toString());
                    progressBar.setVisibility(View.GONE);
                }

                try {
                    Thread.sleep(200 );
                    progressBar.setVisibility(View.GONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }



        btnProductTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), Product.class);
                intent.putExtra("id", String.valueOf(producto.getIntId()));
                viewGroup.getContext().startActivity(intent);
                Toast.makeText(viewGroup.getContext(), "Presionado"+producto.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), Crud.class);
                intent.putExtra("id", String.valueOf(producto.getIntId()));
                intent.putExtra("metodo", "actualizar");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                viewGroup.getContext().startActivity(intent);
                Toast.makeText(viewGroup.getContext(), "Presionado "+producto.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf(producto.getIntId());
                dbFirebase = new DBFirebase();
                dbHelper = dbHelper = new DBHelper(viewGroup.getContext());

                if(id.compareTo("") != 0) {
                    //dbHelper.deleteDataById(id);
                    dbFirebase.deleteDataById( id, producto.getId(), dbHelper, comeBackHome);
                    //clean();
                } else {
                    Toast.makeText(viewGroup.getContext().getApplicationContext(),"ingrese Id a eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext().getApplicationContext(), Maps.class);
                intent.putExtra("latitud", String.valueOf(producto.getLatitud()));
                intent.putExtra("longitud", String.valueOf(producto.getLongitud()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                viewGroup.getContext().startActivity(intent);
                Toast.makeText(context, "Presionado "+producto.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }


    @Override
    public void intentToHome() {
        Intent intent = new Intent(context.getApplicationContext(), Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
