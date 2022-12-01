package com.example.myapplication.Entities;

public class Producto {

    String name;
    String description;
    int image;

    public Producto(String name, String descripcion, int image) {
        this.name = name;
        this.description = descripcion;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}