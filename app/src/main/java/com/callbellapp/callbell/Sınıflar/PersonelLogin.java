package com.callbellapp.callbell.Sınıflar;

import java.io.Serializable;

public class PersonelLogin implements Serializable {
    private int perId;
    private String perAdi;
    private String perUID;
    private int perIsletmeId;

    public PersonelLogin() {
    }

    public PersonelLogin(int perId, String perAdi, String perUID, int perIsletmeId) {
        this.perId = perId;
        this.perAdi = perAdi;
        this.perUID = perUID;
        this.perIsletmeId = perIsletmeId;
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

    public int getPerIsletmeId() {
        return perIsletmeId;
    }

    public void setPerIsletmeId(int perIsletmeId) {
        this.perIsletmeId = perIsletmeId;
    }
}
