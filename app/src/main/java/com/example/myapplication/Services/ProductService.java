package com.example.myapplication.Services;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.example.myapplication.DB.DBFirebase;
import com.example.myapplication.DB.DBHelper;
import com.example.myapplication.Entities.Producto;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProductService {
    public ArrayList<Producto> cursorToArray(Cursor cursor, Context context){
        ArrayList<Producto> list = new ArrayList<>();
        if(cursor.getCount()==0){
            return list;
        }else {
            while (cursor.moveToNext()) {
                if( !Boolean.valueOf(cursor.getString(5))){
                    Producto product = new Producto(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            Boolean.valueOf(cursor.getString(5)),
                            stringToDate(cursor.getString(6)),
                            stringToDate(cursor.getString(7))
                    );
                    if(Boolean.valueOf(cursor.getString(8))){
                        DBFirebase dbFirebase = new DBFirebase();
                        DBHelper dbHelper = new DBHelper(context);
                        dbFirebase.uploadData(product, dbHelper);
                    }
//for update
                    if(Boolean.valueOf(cursor.getString(9))){
                        DBFirebase dbFirebase = new DBFirebase();
                        DBHelper dbHelper = new DBHelper(context);
                        dbFirebase.forUpdate(product, dbHelper);
                    }

                    if(Boolean.valueOf(cursor.getString(10))){
                        DBFirebase dbFirebase = new DBFirebase();
                        DBHelper dbHelper = new DBHelper(context);
                        dbFirebase.forDelete(product, dbHelper);
                    }


                    list.add(product);
                }

            }
        }
        return list;
    }

    public byte[] imageviewToByte (ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public Bitmap byteToBitmap(byte[] image) {

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
        return bitmap;
    }

    public Date stringToDate (String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return  dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
