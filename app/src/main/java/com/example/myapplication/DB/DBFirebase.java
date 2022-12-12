package com.example.myapplication.DB;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.Entities.Producto;
import com.example.myapplication.Home;
import com.example.myapplication.Services.ComeBackHome;
import com.example.myapplication.Services.ProductService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.ls.LSParser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBFirebase {
    private FirebaseFirestore db;
    private ProductService productService;

    public DBFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.productService = new ProductService();
    }
    public void insertData(Producto producto, DBHelper dbHelper, Context context, ComeBackHome comeBackHome){
        // Create a new user with a first and last name
        Map<String, Object> product = new HashMap<>();
        product.put("id", producto.getId());
        product.put("name", producto.getName());
        product.put("description", producto.getDescription());
        product.put("image", producto.getImage());
        product.put("deleted", producto.isDelete());
        product.put("createdAt", producto.getCreatedAt());
        product.put("updatedAt", producto.getUpdatedAt());
        // Add a new document with a generated ID
        db.collection("products")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        AppCompatActivity appCompatActivity = new AppCompatActivity();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        dbHelper.insertData(producto, false);
                        comeBackHome.intentToHome();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        dbHelper.insertData(producto, true);
                        comeBackHome.intentToHome();
                    }
                });
    }

    public void uploadData(Producto producto, DBHelper dbHelper){
        // Create a new user with a first and last name
        Map<String, Object> product = new HashMap<>();
        product.put("id", producto.getId());
        product.put("name", producto.getName());
        product.put("description", producto.getDescription());
        product.put("image", producto.getImage());
        product.put("deleted", producto.isDelete());
        product.put("createdAt", producto.getCreatedAt());
        product.put("updatedAt", producto.getUpdatedAt());
        // Add a new document with a generated ID
        db.collection("products")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        dbHelper.updateUpload(producto.getId(), false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        dbHelper.updateUpload(producto.getId(), true);
                    }
                });
    }



    public void syncData(ProductAdapter productAdapter, ArrayList<Producto> list, DBHelper dbHelper){

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!Boolean.valueOf(document.getData().get("deleted").toString())){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Producto product = null;
                                    product = new Producto(
                                            document.getData().get("id").toString(),
                                            document.getData().get("name").toString(),
                                            document.getData().get("description").toString(),
                                            document.getData().get("image").toString(),
                                            Boolean.valueOf(document.getData().get("deleted").toString()),
                                            productService.stringToDate(document.getData().get("createdAt").toString()),
                                            productService.stringToDate(document.getData().get("updatedAt").toString())

                                    );
                                    dbHelper.insertData(product, false);
                                    list.add(product);
                                }

                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getData(ProductAdapter productAdapter, ArrayList<Producto> list){
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!Boolean.valueOf(document.getData().get("deleted").toString())){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Producto product;
                                    product = new Producto(
                                            document.getData().get("id").toString(),
                                            document.getData().get("name").toString(),
                                            document.getData().get("description").toString(),
                                            document.getData().get("image").toString(),
                                            Boolean.valueOf(document.getData().get("deleted").toString()),
                                            productService.stringToDate(document.getData().get("createdAt").toString()),
                                            productService.stringToDate(document.getData().get("updatedAt").toString())

                                    );
                                    list.add(product);
                                }

                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public Producto getDataById(String id){
        final Producto[] product = {null};
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                if(document.getData().get("id").toString().compareTo(id) == 0) {
                                    product[0] = new Producto(
                                            document.getData().get("id").toString(),
                                            document.getData().get("name").toString(),
                                            document.getData().get("description").toString(),
                                            document.getData().get("image").toString(),
                                            Boolean.valueOf(document.getData().get("deleted").toString()),
                                            productService.stringToDate(document.getData().get("createdAt").toString()),
                                            productService.stringToDate(document.getData().get("updatedAt").toString())
                                            );
                                }
                            }
                        }
                    }
                });
        return product[0];
    }

    public void deleteDataById(String id) {
        db.collection("products")
                .document(id)
                .update("deleted",true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
      /*  db.collection("products")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });*/
    }

    public void updateDataById(String id, String name, String description, byte[] image) {
        db.collection("products")
                .document(id)
                .update("name",name, "description", description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
