package com.example.myapplication.Entities;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Producto {
    private String id;
    private String name;
    private String description;
    private byte[] image;

    public Producto(String name, String descripcion, byte[] image) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = descripcion;
        this.image = image;
    }

    public Producto(String id, String name, String descripcion, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
    }




    public Producto (String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.image = "".getBytes();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}