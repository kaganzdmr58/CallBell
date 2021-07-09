package com.callbellapp.callbell.Sınıflar;

import java.io.Serializable;

public class Mesajlar implements Serializable {
    private int mes_id;
    private IsletmeBilgi mes_isletme_id;
    private String mes_mesaj;
    private String mes_tarih;
    private String mes_url;

    public Mesajlar() {
    }

    public Mesajlar(int mes_id, IsletmeBilgi mes_isletme_id, String mes_mesaj, String mes_tarih, String mes_url) {
        this.mes_id = mes_id;
        this.mes_isletme_id = mes_isletme_id;
        this.mes_mesaj = mes_mesaj;
        this.mes_tarih = mes_tarih;
        this.mes_url = mes_url;
    }

    public int getMes_id() {
        return mes_id;
    }

    public void setMes_id(int mes_id) {
        this.mes_id = mes_id;
    }

    public IsletmeBilgi getMes_isletme_id() {
        return mes_isletme_id;
    }

    public void setMes_isletme_id(IsletmeBilgi mes_isletme_id) {
        this.mes_isletme_id = mes_isletme_id;
    }

    public String getMes_mesaj() {
        return mes_mesaj;
    }

    public void setMes_mesaj(String mes_mesaj) {
        this.mes_mesaj = mes_mesaj;
    }

    public String getMes_tarih() {
        return mes_tarih;
    }

    public void setMes_tarih(String mes_tarih) {
        this.mes_tarih = mes_tarih;
    }

    public String getMes_url() {
        return mes_url;
    }

    public void setMes_url(String mes_url) {
        this.mes_url = mes_url;
    }
}
