package com.example.kutuphaneotomasyon.repository;

import com.example.kutuphaneotomasyon.model.Kitap;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class KitapRepository {

    private final Map<Long, Kitap> kitaplar = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public KitapRepository() {
        // Örnek kitaplar ekleniyor
        ekle(new Kitap(null, "978-3-16-148410-0", "Sefiller", "Victor Hugo", "İş Bankası", 1862));
        ekle(new Kitap(null, "978-0-06-112008-4", "1984", "George Orwell", "Can Yayınları", 1949));
        ekle(new Kitap(null, "978-0-7432-7356-5", "Suç ve Ceza", "Fyodor Dostoyevski", "İletişim", 1866));
        ekle(new Kitap(null, "978-605-07-0323-3", "Kürk Mantolu Madonna", "Sabahattin Ali", "Yapı Kredi", 1943));
        ekle(new Kitap(null, "978-975-07-0616-0", "İnce Memed", "Yaşar Kemal", "YKY", 1955));
    }

    public Kitap ekle(Kitap kitap) {
        Long id = idCounter.getAndIncrement();
        kitap.setId(id);
        kitaplar.put(id, kitap);
        return kitap;
    }

    public Optional<Kitap> idIleBul(Long id) {
        return Optional.ofNullable(kitaplar.get(id));
    }

    public List<Kitap> tumunuGetir() {
        return new ArrayList<>(kitaplar.values());
    }

    public Kitap guncelle(Kitap kitap) {
        if (kitaplar.containsKey(kitap.getId())) {
            kitaplar.put(kitap.getId(), kitap);
            return kitap;
        }
        return null;
    }

    public boolean sil(Long id) {
        return kitaplar.remove(id) != null;
    }

    public List<Kitap> basligaGoreAra(String baslik) {
        return kitaplar.values().stream()
                .filter(k -> k.getBaslik().toLowerCase().contains(baslik.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Kitap> yazaraGoreAra(String yazar) {
        return kitaplar.values().stream()
                .filter(k -> k.getYazar().toLowerCase().contains(yazar.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Kitap> isbnIleBul(String isbn) {
        return kitaplar.values().stream()
                .filter(k -> k.getIsbn().equals(isbn))
                .findFirst();
    }

    public List<Kitap> musaitKitaplariGetir() {
        return kitaplar.values().stream()
                .filter(k -> !k.isOduncAlindi())
                .collect(Collectors.toList());
    }

    public List<Kitap> oduncAlinanKitaplariGetir() {
        return kitaplar.values().stream()
                .filter(Kitap::isOduncAlindi)
                .collect(Collectors.toList());
    }
}
