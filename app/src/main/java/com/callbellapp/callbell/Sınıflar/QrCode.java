package com.callbellapp.callbell.Sınıflar;


// Tablo 1 qr codların tutulduğu tablo


import java.io.Serializable;

public class QrCode implements Serializable {
    private int qrId;
    private String qrName;
    private String qrIsletmeID;


    public QrCode() {
    }

    public QrCode(int qrId, String qrName, String qrIsletmeID) {
        this.qrId = qrId;
        this.qrName = qrName;
        this.qrIsletmeID = qrIsletmeID;
    }

    public int getQrId() {
        return qrId;
    }

    public void setQrId(int qrId) {
        this.qrId = qrId;
    }

    public String getQrName() {
        return qrName;
    }

    public void setQrName(String qrName) {
        this.qrName = qrName;
    }

    public String getQrIsletmeID() {
        return qrIsletmeID;
    }

    public void setQrIsletmeID(String qrIsletmeID) {
        this.qrIsletmeID = qrIsletmeID;
    }
}
