package com.example.myapplication.Entities;

public class Producto {

    String name;
    String description;
    byte[] image;

    public Producto(String name, String descripcion, byte[] image) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}