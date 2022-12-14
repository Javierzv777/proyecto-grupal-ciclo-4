package com.example.myapplication.Entities;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class Producto {
    private String id;
    private String name;
    private String description;
    private String image;
    private boolean delete;
    private Date createdAt;
    private Date updatedAt;
    private int intId = 0;
    private Double latitud;
    private Double longitud;



    public int getIntId() {
        return intId;
    }

    public Producto(String name, String descripcion, String image, Double latitud, Double longitud) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = descripcion;
        this.image = image;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.delete = false;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Producto(String id, String name, String descripcion, String image) {
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.delete = false;
    }

    public Producto(int intId, String id, String name, String descripcion, String image, Boolean delete, Date createdAt, Date updatedAt) {
        this.intId = intId;
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.delete = delete;
    }

    public Producto(
            int intId,
            String id,
            String name,
            String descripcion,
            String image,
            Boolean delete,
            Date createdAt,
            Date updatedAt,
            Double latitud,
            Double longitud
    ) {
        this.intId = intId;
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.delete = delete;
        this.latitud= latitud;
        this.longitud = longitud;
    }

    public Producto(

        String id,
        String name,
        String description,
        String image,
        Boolean delete,
        Date createdAt,
        Date updatedAt,
        Double latitud,
        Double longitud
    ) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.intId = intId;
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.delete = delete;
    }

    public Producto(String id, String name, String descripcion, String image, Boolean delete, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.delete = delete;
    }

    public Producto(String id, String name, String descripcion, String image, Boolean delete, Date createdAt, Date updatedAt, Boolean forUpload) {
        this.id = id;
        this.name = name;
        this.description = descripcion;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.delete = delete;
    }




    public Producto (String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.image = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.delete = false;
    }




    public void setIntId(int intId) {
        this.intId = intId;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}