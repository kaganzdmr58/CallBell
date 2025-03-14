package com.callbellapp.callbell.Sınıflar;

public class CagrilarInt {
    private int caId;
    private int caIsletmeID;
    private int caMasaID;
    private int caMusteriID;
    private String caMesaj;
    private String caZaman;

    public CagrilarInt() {
    }

    public CagrilarInt(int caId, int caIsletmeID, int caMasaID, int caMusteriID, String caMesaj, String caZaman) {
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

    public int getCaIsletmeID() {
        return caIsletmeID;
    }

    public void setCaIsletmeID(int caIsletmeID) {
        this.caIsletmeID = caIsletmeID;
    }

    public int getCaMasaID() {
        return caMasaID;
    }

    public void setCaMasaID(int caMasaID) {
        this.caMasaID = caMasaID;
    }

    public int getCaMusteriID() {
        return caMusteriID;
    }

    public void setCaMusteriID(int caMusteriID) {
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
