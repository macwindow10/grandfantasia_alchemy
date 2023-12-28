package com.example.alchemy;

public class ImageItemModel {

    private String id;
    private String icon;
    private String name;
    private int value;
    private int quantity;

    public ImageItemModel(String icon) {
        this.id = "";
        this.icon = icon;
        this.name = "";
        this.value = 0;
        this.quantity = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}