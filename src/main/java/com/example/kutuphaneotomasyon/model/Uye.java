package com.example.kutuphaneotomasyon.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Uye {
    private Long id;
    private String uyeNo;
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private LocalDate kayitTarihi;
    private List<Long> oduncAlinanKitaplar;

    public Uye() {
        this.oduncAlinanKitaplar = new ArrayList<>();
    }

    public Uye(Long id, String uyeNo, String ad, String soyad, String email, String telefon) {
        this.id = id;
        this.uyeNo = uyeNo;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
        this.kayitTarihi = LocalDate.now();
        this.oduncAlinanKitaplar = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUyeNo() {
        return uyeNo;
    }

    public void setUyeNo(String uyeNo) {
        this.uyeNo = uyeNo;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public LocalDate getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDate kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public List<Long> getOduncAlinanKitaplar() {
        return oduncAlinanKitaplar;
    }

    public void setOduncAlinanKitaplar(List<Long> oduncAlinanKitaplar) {
        this.oduncAlinanKitaplar = oduncAlinanKitaplar;
    }

    public String getAdSoyad() {
        return ad + " " + soyad;
    }

    @Override
    public String toString() {
        return "Uye{" +
                "id=" + id +
                ", uyeNo='" + uyeNo + '\'' +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", kayitTarihi=" + kayitTarihi +
                '}';
    }
}
