package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Product;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Producto> arrayProducts;


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

        Button btnProductTemplate = (Button) v.findViewById(R.id.btnProductTemplate);
        ImageView imgProduct = (ImageView) v.findViewById(R.id.imgProduct);
        TextView textViewNam = (TextView) v.findViewById(R.id.textProductTitle);
        TextView textViewDes = (TextView) v.findViewById(R.id.textProductDes);

        byte[] image = producto.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
        imgProduct.setImageBitmap(bitmap);
        textViewNam.setText(producto.getName());
        textViewDes.setText(producto.getDescription());

        btnProductTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Product.class);
                intent.putExtra("id", String.valueOf(producto.getId()));
                Toast.makeText(context, "Presionado"+producto.getName(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        return v;
    }
}
