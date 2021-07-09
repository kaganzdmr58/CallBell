package com.callbellapp.callbell.Sınıflar;


// tablo 3 gelen çağrıların tutulduğu tablo


import java.io.Serializable;

public class Cagrilar implements Serializable {
    private int caId;
    private String caIsletmeID;
    private String caMasaID;
    private String caMusteriID;
    private String caMesaj;
    private String caZaman;

    public Cagrilar() {
    }

    public Cagrilar(int caId, String caIsletmeID, String caMasaID, String caMusteriID, String caMesaj, String caZaman) {
        this.caId = caId;
        this.caIsletmeID = caIsletmeID;
        this.caMasaID = caMasaID;
        this.caMusteriID = caMusteriID;
        this.caMesaj = caMesaj;
        this.caZaman = caZaman;
    }

    public int getCaId() {
        return caId;
    }

    public void setCaId(int caId) {
        this.caId = caId;
    }

    public String getCaIsletmeID() {
        return caIsletmeID;
    }

    public void setCaIsletmeID(String caIsletmeID) {
        this.caIsletmeID = caIsletmeID;
    }

    public String getCaMasaID() {
        return caMasaID;
    }

    public void setCaMasaID(String caMasaID) {
        this.caMasaID = caMasaID;
    }

    public String getCaMusteriID() {
        return caMusteriID;
    }

    public void setCaMusteriID(String caMusteriID) {
        this.caMusteriID = caMusteriID;
    }

    public String getCaMesaj() {
        return caMesaj;
    }

    public void setCaMesaj(String caMesaj) {
        this.caMesaj = caMesaj;
    }

    public String getCaZaman() {
        return caZaman;
    }

    public void setCaZaman(String caZaman) {
        this.caZaman = caZaman;
    }
}
