package com.callbellapp.callbell.Sınıflar;

import java.io.Serializable;

public class PersonelFirebase implements Serializable {
    private String perfKey;
    private String perfAdi;
    private String perfGorev;
    private String perfTel;
    private String perfNot;

    public PersonelFirebase() {
    }

    public PersonelFirebase(String perfKey, String perfAdi, String perfGorev, String perfTel, String perfNot) {
        this.perfKey = perfKey;
        this.perfAdi = perfAdi;
        this.perfGorev = perfGorev;
        this.perfTel = perfTel;
        this.perfNot = perfNot;
    }

    public String getPerfKey() {
        return perfKey;
    }

    public void setPerfKey(String perfKey) {
        this.perfKey = perfKey;
    }

    public String getPerfAdi() {
        return perfAdi;
    }

    public void setPerfAdi(String perfAdi) {
        this.perfAdi = perfAdi;
    }

    public String getPerfGorev() {
        return perfGorev;
    }

    public void setPerfGorev(String perfGorev) {
        this.perfGorev = perfGorev;
    }

    public String getPerfTel() {
        return perfTel;
    }

    public void setPerfTel(String perfTel) {
        this.perfTel = perfTel;
    }

    public String getPerfNot() {
        return perfNot;
    }

    public void setPerfNot(String perfNot) {
        this.perfNot = perfNot;
    }
}
