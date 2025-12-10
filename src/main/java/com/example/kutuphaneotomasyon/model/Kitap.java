package com.example.kutuphaneotomasyon.model;

import java.time.LocalDate;

public class Kitap {
    private Long id;
    private String isbn;
    private String baslik;
    private String yazar;
    private String yayinevi;
    private int yayinYili;
    private boolean oduncAlindi;
    private String oduncAlanUye;
    private LocalDate oduncAlmaTarihi;
    private LocalDate iadeTarihi;

    public Kitap() {
    }

    public Kitap(Long id, String isbn, String baslik, String yazar, String yayinevi, int yayinYili) {
        this.id = id;
        this.isbn = isbn;
        this.baslik = baslik;
        this.yazar = yazar;
        this.yayinevi = yayinevi;
        this.yayinYili = yayinYili;
        this.oduncAlindi = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public String getYayinevi() {
        return yayinevi;
    }

    public void setYayinevi(String yayinevi) {
        this.yayinevi = yayinevi;
    }

    public int getYayinYili() {
        return yayinYili;
    }

    public void setYayinYili(int yayinYili) {
        this.yayinYili = yayinYili;
    }

    public boolean isOduncAlindi() {
        return oduncAlindi;
    }

    public void setOduncAlindi(boolean oduncAlindi) {
        this.oduncAlindi = oduncAlindi;
    }

    public String getOduncAlanUye() {
        return oduncAlanUye;
    }

    public void setOduncAlanUye(String oduncAlanUye) {
        this.oduncAlanUye = oduncAlanUye;
    }

    public LocalDate getOduncAlmaTarihi() {
        return oduncAlmaTarihi;
    }

    public void setOduncAlmaTarihi(LocalDate oduncAlmaTarihi) {
        this.oduncAlmaTarihi = oduncAlmaTarihi;
    }

    public LocalDate getIadeTarihi() {
        return iadeTarihi;
    }

    public void setIadeTarihi(LocalDate iadeTarihi) {
        this.iadeTarihi = iadeTarihi;
    }

    @Override
    public String toString() {
        return "Kitap{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", baslik='" + baslik + '\'' +
                ", yazar='" + yazar + '\'' +
                ", yayinevi='" + yayinevi + '\'' +
                ", yayinYili=" + yayinYili +
                ", oduncAlindi=" + oduncAlindi +
                ", oduncAlanUye='" + oduncAlanUye + '\'' +
                '}';
    }
}
