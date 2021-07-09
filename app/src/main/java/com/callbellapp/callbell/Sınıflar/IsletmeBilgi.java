package com.callbellapp.callbell.Sınıflar;


// Tablo 2 İşletme bilgilerinin tutulduğu tablo


import java.io.Serializable;

public class IsletmeBilgi implements Serializable {
    private int isId;
    private String isName;
    private String isYonetciID;
    private String isSlogan;
    private String isTel;
    private String isEmail;
    private String isWeb;
    private String isAdres;
    private String isIconUrl;

    public IsletmeBilgi() {
    }

    public IsletmeBilgi(int isId, String isName, String isYonetciID, String isSlogan, String isTel, String isEmail, String isWeb, String isAdres, String isIconUrl) {
        this.isId = isId;
        this.isName = isName;
        this.isYonetciID = isYonetciID;
        this.isSlogan = isSlogan;
        this.isTel = isTel;
        this.isEmail = isEmail;
        this.isWeb = isWeb;
        this.isAdres = isAdres;
        this.isIconUrl = isIconUrl;
    }

    public int getIsId() {
        return isId;
    }

    public void setIsId(int isId) {
        this.isId = isId;
    }

    public String getIsName() {
        return isName;
    }

    public void setIsName(String isName) {
        this.isName = isName;
    }

    public String getIsYonetciID() {
        return isYonetciID;
    }

    public void setIsYonetciID(String isYonetciID) {
        this.isYonetciID = isYonetciID;
    }

    public String getIsSlogan() {
        return isSlogan;
    }

    public void setIsSlogan(String isSlogan) {
        this.isSlogan = isSlogan;
    }

    public String getIsTel() {
        return isTel;
    }

    public void setIsTel(String isTel) {
        this.isTel = isTel;
    }

    public String getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(String isEmail) {
        this.isEmail = isEmail;
    }

    public String getIsWeb() {
        return isWeb;
    }

    public void setIsWeb(String isWeb) {
        this.isWeb = isWeb;
    }

    public String getIsAdres() {
        return isAdres;
    }

    public void setIsAdres(String isAdres) {
        this.isAdres = isAdres;
    }

    public String getIsIconUrl() {
        return isIconUrl;
    }

    public void setIsIconUrl(String isIconUrl) {
        this.isIconUrl = isIconUrl;
    }
}
