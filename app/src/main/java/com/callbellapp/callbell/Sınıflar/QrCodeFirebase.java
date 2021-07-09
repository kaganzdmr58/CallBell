package com.callbellapp.callbell.Sınıflar;

import java.io.Serializable;

public class QrCodeFirebase implements Serializable {
    private String qrfKey;
    private String qrfMesaj;
    private String qrfZaman;
    private String qrfAdi;

    public QrCodeFirebase() {
    }

    public QrCodeFirebase(String qrfKey, String qrfMesaj, String qrfZaman, String qrfAdi) {
        this.qrfKey = qrfKey;
        this.qrfMesaj = qrfMesaj;
        this.qrfZaman = qrfZaman;
        this.qrfAdi = qrfAdi;
    }

    public String getQrfKey() {
        return qrfKey;
    }

    public void setQrfKey(String qrfKey) {
        this.qrfKey = qrfKey;
    }

    public String getQrfMesaj() {
        return qrfMesaj;
    }

    public void setQrfMesaj(String qrfMesaj) {
        this.qrfMesaj = qrfMesaj;
    }

    public String getQrfZaman() {
        return qrfZaman;
    }

    public void setQrfZaman(String qrfZaman) {
        this.qrfZaman = qrfZaman;
    }

    public String getQrfAdi() {
        return qrfAdi;
    }

    public void setQrfAdi(String qrfAdi) {
        this.qrfAdi = qrfAdi;
    }
}
