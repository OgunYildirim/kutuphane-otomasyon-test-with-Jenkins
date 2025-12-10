package com.example.kutuphaneotomasyon.service;

import com.example.kutuphaneotomasyon.model.Uye;
import com.example.kutuphaneotomasyon.repository.UyeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UyeService {

    private final UyeRepository uyeRepository;

    public UyeService(UyeRepository uyeRepository) {
        this.uyeRepository = uyeRepository;
    }

    public Uye uyeEkle(Uye uye) {
        // Otomatik üye numarası oluştur
        String uyeNo = "UYE" + String.format("%03d", uyeRepository.tumunuGetir().size() + 1);
        uye.setUyeNo(uyeNo);
        return uyeRepository.ekle(uye);
    }

    public List<Uye> tumUyeleriGetir() {
        return uyeRepository.tumunuGetir();
    }

    public Optional<Uye> uyeBul(Long id) {
        return uyeRepository.idIleBul(id);
    }

    public Uye uyeGuncelle(Uye uye) {
        return uyeRepository.guncelle(uye);
    }

    public String uyeSil(Long id) {
        Optional<Uye> uyeOpt = uyeRepository.idIleBul(id);
        if (uyeOpt.isEmpty()) {
            return "Üye bulunamadı!";
        }

        Uye uye = uyeOpt.get();
        if (!uye.getOduncAlinanKitaplar().isEmpty()) {
            return "Üyenin iade etmediği kitaplar var! Önce kitaplar iade edilmeli.";
        }

        uyeRepository.sil(id);
        return "Üye başarıyla silindi!";
    }

    public List<Uye> adaGoreAra(String ad) {
        return uyeRepository.adaGoreAra(ad);
    }
}

