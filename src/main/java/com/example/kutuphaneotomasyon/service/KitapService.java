package com.example.kutuphaneotomasyon.service;

import com.example.kutuphaneotomasyon.model.Kitap;
import com.example.kutuphaneotomasyon.model.Uye;
import com.example.kutuphaneotomasyon.repository.KitapRepository;
import com.example.kutuphaneotomasyon.repository.UyeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class KitapService {

    private final KitapRepository kitapRepository;
    private final UyeRepository uyeRepository;

    public KitapService(KitapRepository kitapRepository, UyeRepository uyeRepository) {
        this.kitapRepository = kitapRepository;
        this.uyeRepository = uyeRepository;
    }

    public Kitap kitapEkle(Kitap kitap) {
        return kitapRepository.ekle(kitap);
    }

    public List<Kitap> tumKitaplariGetir() {
        return kitapRepository.tumunuGetir();
    }

    public Optional<Kitap> kitapBul(Long id) {
        return kitapRepository.idIleBul(id);
    }

    public Kitap kitapGuncelle(Kitap kitap) {
        return kitapRepository.guncelle(kitap);
    }

    public boolean kitapSil(Long id) {
        Optional<Kitap> kitap = kitapRepository.idIleBul(id);
        if (kitap.isPresent() && kitap.get().isOduncAlindi()) {
            return false; // Ödünç alınmış kitap silinemez
        }
        return kitapRepository.sil(id);
    }

    public List<Kitap> basligaGoreAra(String baslik) {
        return kitapRepository.basligaGoreAra(baslik);
    }

    public List<Kitap> yazaraGoreAra(String yazar) {
        return kitapRepository.yazaraGoreAra(yazar);
    }

    public List<Kitap> musaitKitaplariGetir() {
        return kitapRepository.musaitKitaplariGetir();
    }

    public List<Kitap> oduncAlinanKitaplariGetir() {
        return kitapRepository.oduncAlinanKitaplariGetir();
    }

    public String kitapOduncVer(Long kitapId, Long uyeId) {
        Optional<Kitap> kitapOpt = kitapRepository.idIleBul(kitapId);
        Optional<Uye> uyeOpt = uyeRepository.idIleBul(uyeId);

        if (kitapOpt.isEmpty()) {
            return "Kitap bulunamadı!";
        }
        if (uyeOpt.isEmpty()) {
            return "Üye bulunamadı!";
        }

        Kitap kitap = kitapOpt.get();
        Uye uye = uyeOpt.get();

        if (kitap.isOduncAlindi()) {
            return "Bu kitap zaten ödünç alınmış!";
        }

        if (uye.getOduncAlinanKitaplar().size() >= 3) {
            return "Üye en fazla 3 kitap ödünç alabilir!";
        }

        // Kitabı ödünç ver
        kitap.setOduncAlindi(true);
        kitap.setOduncAlanUye(uye.getAdSoyad());
        kitap.setOduncAlmaTarihi(LocalDate.now());
        kitap.setIadeTarihi(LocalDate.now().plusDays(15)); // 15 gün süre
        kitapRepository.guncelle(kitap);

        // Üyenin listesine ekle
        uye.getOduncAlinanKitaplar().add(kitapId);
        uyeRepository.guncelle(uye);

        return "Kitap başarıyla ödünç verildi. İade tarihi: " + kitap.getIadeTarihi();
    }

    public String kitapIadeAl(Long kitapId) {
        Optional<Kitap> kitapOpt = kitapRepository.idIleBul(kitapId);

        if (kitapOpt.isEmpty()) {
            return "Kitap bulunamadı!";
        }

        Kitap kitap = kitapOpt.get();

        if (!kitap.isOduncAlindi()) {
            return "Bu kitap zaten kütüphanede!";
        }

        // Üyenin listesinden çıkar
        for (Uye uye : uyeRepository.tumunuGetir()) {
            if (uye.getOduncAlinanKitaplar().contains(kitapId)) {
                uye.getOduncAlinanKitaplar().remove(kitapId);
                uyeRepository.guncelle(uye);
                break;
            }
        }

        // Kitabı iade al
        kitap.setOduncAlindi(false);
        kitap.setOduncAlanUye(null);
        kitap.setOduncAlmaTarihi(null);
        kitap.setIadeTarihi(null);
        kitapRepository.guncelle(kitap);

        return "Kitap başarıyla iade alındı!";
    }
}
