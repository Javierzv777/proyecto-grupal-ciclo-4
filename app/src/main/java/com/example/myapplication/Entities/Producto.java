package com.example.myapplication.Entities;

public class Producto {
    private int id;
    private String name;
    private String description;
    private byte[] image;

    public Producto(int id,String name, String descripcion, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
    }

    public int getId() {
        return id;
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