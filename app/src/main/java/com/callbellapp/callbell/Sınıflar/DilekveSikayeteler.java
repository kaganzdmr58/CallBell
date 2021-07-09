package com.callbellapp.callbell.Sınıflar;

import java.io.Serializable;

public class DilekveSikayeteler implements Serializable {
    private int si_id;
    private String si_masa_tarih;
    private String si_sikayet;
    private String si_email_ad;
    private String si_isletme_qr;
    private String si_gonderen_uid;
    private Float si_rating;

    public DilekveSikayeteler() {
    }

    public DilekveSikayeteler(int si_id, String si_masa_tarih, String si_sikayet, String si_email_ad, String si_isletme_qr, String si_gonderen_uid, Float si_rating) {
        this.si_id = si_id;
        this.si_masa_tarih = si_masa_tarih;
        this.si_sikayet = si_sikayet;
        this.si_email_ad = si_email_ad;
        this.si_isletme_qr = si_isletme_qr;
        this.si_gonderen_uid = si_gonderen_uid;
        this.si_rating = si_rating;
    }

    public int getSi_id() {
        return si_id;
    }

    public void setSi_id(int si_id) {
        this.si_id = si_id;
    }

    public String getSi_masa_tarih() {
        return si_masa_tarih;
    }

    public void setSi_masa_tarih(String si_masa_tarih) {
        this.si_masa_tarih = si_masa_tarih;
    }

    public String getSi_sikayet() {
        return si_sikayet;
    }

    public void setSi_sikayet(String si_sikayet) {
        this.si_sikayet = si_sikayet;
    }

    public String getSi_email_ad() {
        return si_email_ad;
    }

    public void setSi_email_ad(String si_email_ad) {
        this.si_email_ad = si_email_ad;
    }

    public String getSi_isletme_qr() {
        return si_isletme_qr;
    }

    public void setSi_isletme_qr(String si_isletme_qr) {
        this.si_isletme_qr = si_isletme_qr;
    }

    public String getSi_gonderen_uid() {
        return si_gonderen_uid;
    }

    public void setSi_gonderen_uid(String si_gonderen_uid) {
        this.si_gonderen_uid = si_gonderen_uid;
    }

    public Float getSi_rating() {
        return si_rating;
    }

    public void setSi_rating(Float si_rating) {
        this.si_rating = si_rating;
    }
}
