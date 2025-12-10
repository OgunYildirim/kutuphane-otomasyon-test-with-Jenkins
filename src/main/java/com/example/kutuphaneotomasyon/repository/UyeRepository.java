package com.example.kutuphaneotomasyon.repository;

import com.example.kutuphaneotomasyon.model.Uye;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class UyeRepository {

    private final Map<Long, Uye> uyeler = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public UyeRepository() {
        // Örnek üyeler ekleniyor
        ekle(new Uye(null, "UYE001", "Ahmet", "Yılmaz", "ahmet@email.com", "555-111-2233"));
        ekle(new Uye(null, "UYE002", "Fatma", "Demir", "fatma@email.com", "555-222-3344"));
        ekle(new Uye(null, "UYE003", "Mehmet", "Kaya", "mehmet@email.com", "555-333-4455"));
    }

    public Uye ekle(Uye uye) {
        Long id = idCounter.getAndIncrement();
        uye.setId(id);
        uyeler.put(id, uye);
        return uye;
    }

    public Optional<Uye> idIleBul(Long id) {
        return Optional.ofNullable(uyeler.get(id));
    }

    public List<Uye> tumunuGetir() {
        return new ArrayList<>(uyeler.values());
    }

    public Uye guncelle(Uye uye) {
        if (uyeler.containsKey(uye.getId())) {
            uyeler.put(uye.getId(), uye);
            return uye;
        }
        return null;
    }

    public boolean sil(Long id) {
        return uyeler.remove(id) != null;
    }

    public List<Uye> adaGoreAra(String ad) {
        return uyeler.values().stream()
                .filter(u -> u.getAd().toLowerCase().contains(ad.toLowerCase()) ||
                        u.getSoyad().toLowerCase().contains(ad.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Uye> uyeNoIleBul(String uyeNo) {
        return uyeler.values().stream()
                .filter(u -> u.getUyeNo().equals(uyeNo))
                .findFirst();
    }
}
