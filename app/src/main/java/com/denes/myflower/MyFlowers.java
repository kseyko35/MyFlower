package com.denes.myflower;

/**
 * Created by seyfi on 11.3.2018.
 */

public class MyFlowers {
    String name;
    String tarih;
    String cesit;
    String imageUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getCesit() {
        return cesit;
    }

    public void setCesit(String cesit) {
        this.cesit = cesit;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public MyFlowers(String name, String tarih, String cesit, String imageUri) {
        this.name = name;
        this.tarih = tarih;
        this.cesit = cesit;
        this.imageUri = imageUri;

    }
}
