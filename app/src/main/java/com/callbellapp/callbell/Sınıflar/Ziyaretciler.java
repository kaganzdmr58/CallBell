package com.callbellapp.callbell.Sınıflar;

import java.io.Serializable;

public class Ziyaretciler implements Serializable {
    private int zi_id;
    private String zi_uid;
    private IsletmeBilgi zi_isletme;

    public Ziyaretciler() {
    }

    public Ziyaretciler(int zi_id, String zi_uid, IsletmeBilgi zi_isletme) {
        this.zi_id = zi_id;
        this.zi_uid = zi_uid;
        this.zi_isletme = zi_isletme;
    }

    public int getZi_id() {
        return zi_id;
    }

    public void setZi_id(int zi_id) {
        this.zi_id = zi_id;
    }

    public String getZi_uid() {
        return zi_uid;
    }

    public void setZi_uid(String zi_uid) {
        this.zi_uid = zi_uid;
    }

    public IsletmeBilgi getZi_isletme() {
        return zi_isletme;
    }

    public void setZi_isletme(IsletmeBilgi zi_isletme) {
        this.zi_isletme = zi_isletme;
    }
}
