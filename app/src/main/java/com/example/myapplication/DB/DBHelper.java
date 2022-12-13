package com.example.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.myapplication.Entities.Producto;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;
    public DBHelper(Context context){
        super(context, "G104.db", null, 1 );
        sqLiteDatabase = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PRODUCTS("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "uuid VARCHAR(200), "+
                "name VARCHAR, " +
                "description VARCHAR," +
                "img TEXT," +
                "deleted BOOLEAN, " +
                "createdAt DATETIME," +
                "updatedAt DATETIME," +
                "forUpload BOOLEAN," +
                "forUpdate BOOLEAN," +
                "forDelete BOOLEAN" +
                ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
    }

    //metodos crud

    public void insertData(Producto product, Boolean forUpload, Boolean forUpdate) {
        String sql = "INSERT INTO PRODUCTS VALUES (NULL, ?,?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.bindString(1,product.getId());
        statement.bindString(2,product.getName());
        statement.bindString(3,product.getDescription());
        statement.bindString(4, product.getImage());
        statement.bindString(5,String.valueOf(product.isDelete()));
        statement.bindString(6,String.valueOf(product.getCreatedAt()));
        statement.bindString(7, String.valueOf(product.getUpdatedAt()));
        statement.bindString(8,String.valueOf(forUpload));
        statement.bindString(9,String.valueOf(forUpdate));

        statement.executeInsert();
    }

    public Cursor getData(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PRODUCTS", null);
        return cursor;
    }

    public Cursor getDataById(String id){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PRODUCTS WHERE id =" + id, null);
        return cursor;
    }

    public void deleteAllRecords(){
        sqLiteDatabase.execSQL("DELETE FROM PRODUCTS");
    }

    public void deleteDataById(String id, Boolean forDelete){
        ContentValues contentValues = new ContentValues();
        contentValues.put("deleted", true);
        contentValues.put("forDelete", forDelete);
        sqLiteDatabase.update("PRODUCTS", contentValues, "id = ?", new String[]{id});
        //sqLiteDatabase.execSQL("DELETE FROM PRODUCTS WHERE id =" + id);
    }

    public void updateDataById(String id, String name, String description, byte[] image, Boolean forUpdate ){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("img", image);
        contentValues.put("forUpdate", forUpdate);
        sqLiteDatabase.update("PRODUCTS", contentValues, "id = ?", new String[]{id});
    }

    public void updateUpload(String id, Boolean forUpload){
        ContentValues contentValues = new ContentValues();
        contentValues.put("forUpload", forUpload);
        sqLiteDatabase.update("PRODUCTS", contentValues, "id = ?", new String[]{id});
    }

    public void isUpdated(String id, Boolean forUpdate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("forUpdate", forUpdate);
        sqLiteDatabase.update("PRODUCTS", contentValues, "id = ?", new String[]{id});
    }
    public void isDeleted(String id, Boolean isDeleted){
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDeleted", isDeleted);
        sqLiteDatabase.update("PRODUCTS", contentValues, "id = ?", new String[]{id});
    }
}
