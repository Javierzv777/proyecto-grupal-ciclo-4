package com.example.myapplication.Services;

import android.database.Cursor;

import com.example.myapplication.Entities.Producto;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ProductService {
    public ArrayList<Producto> cursorToArray(Cursor cursor){
        ArrayList<Producto> list = new ArrayList<>();
        if(cursor.getCount()==0){
            return list;
        }else {
            while (cursor.moveToNext()) {
            Producto product = new Producto(cursor.getString(1),
                    cursor.getString(2),
                    R.drawable.bog);
                list.add(product);
            }


        }
        return list;
    }

}
