package com.callbellapp.callbell.Sınıflar;


// Tablo 4 Personel (tum herkesin bilgilerinin) tutulduğu tablo.


import java.io.Serializable;

public class Personel implements Serializable {
    private int perId;
    private String perAdi;
    private String perUID;


    public Personel() {
    }

    public Personel(int perId, String perAdi, String perUID) {
        this.perId = perId;
        this.perAdi = perAdi;
        this.perUID = perUID;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public String getPerAdi() {
        return perAdi;
    }

    public void setPerAdi(String perAdi) {
        this.perAdi = perAdi;
    }

    public String getPerUID() {
        return perUID;
    }

    public void setPerUID(String perUID) {
        this.perUID = perUID;
    }
}
