package com.example.barcodescannerusinggooglesheet;

public class ListItem {

    private String id;
    private String barcode;

    public ListItem(String id, String barcode)
    {
        this.id = id;
        this.barcode = barcode;
    }

    public String getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }
}
