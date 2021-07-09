package com.callbellapp.callbell.Sınıflar;

public class WebResimUrl {
    private int res_id;
    private int res_isletme;
    private int res_tur;
    private String res_url;

    public WebResimUrl() {
    }

    public WebResimUrl(int res_id, int res_isletme, int res_tur, String res_url) {
        this.res_id = res_id;
        this.res_isletme = res_isletme;
        this.res_tur = res_tur;
        this.res_url = res_url;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public int getRes_isletme() {
        return res_isletme;
    }

    public void setRes_isletme(int res_isletme) {
        this.res_isletme = res_isletme;
    }

    public int getRes_tur() {
        return res_tur;
    }

    public void setRes_tur(int res_tur) {
        this.res_tur = res_tur;
    }

    public String getRes_url() {
        return res_url;
    }

    public void setRes_url(String res_url) {
        this.res_url = res_url;
    }
}
