package com.example.kutuphaneotomasyon.controller;

import com.example.kutuphaneotomasyon.model.Kitap;
import com.example.kutuphaneotomasyon.service.KitapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kitaplar")
public class KitapController {

    private final KitapService kitapService;

    public KitapController(KitapService kitapService) {
        this.kitapService = kitapService;
    }

    // Tüm kitapları listele
    @GetMapping
    public ResponseEntity<List<Kitap>> tumKitaplariGetir() {
        return ResponseEntity.ok(kitapService.tumKitaplariGetir());
    }

    // ID ile kitap getir
    @GetMapping("/{id}")
    public ResponseEntity<Kitap> kitapGetir(@PathVariable Long id) {
        return kitapService.kitapBul(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Yeni kitap ekle
    @PostMapping
    public ResponseEntity<Kitap> kitapEkle(@RequestBody Kitap kitap) {
        Kitap yeniKitap = kitapService.kitapEkle(kitap);
        return ResponseEntity.ok(yeniKitap);
    }

    // Kitap güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Kitap> kitapGuncelle(@PathVariable Long id, @RequestBody Kitap kitap) {
        kitap.setId(id);
        Kitap guncellenenKitap = kitapService.kitapGuncelle(kitap);
        if (guncellenenKitap != null) {
            return ResponseEntity.ok(guncellenenKitap);
        }
        return ResponseEntity.notFound().build();
    }

    // Kitap sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> kitapSil(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        if (kitapService.kitapSil(id)) {
            response.put("mesaj", "Kitap başarıyla silindi!");
            return ResponseEntity.ok(response);
        }
        response.put("mesaj", "Kitap silinemedi! Kitap bulunamadı veya ödünç alınmış.");
        return ResponseEntity.badRequest().body(response);
    }

    // Başlığa göre ara
    @GetMapping("/ara/baslik")
    public ResponseEntity<List<Kitap>> basligaGoreAra(@RequestParam String q) {
        return ResponseEntity.ok(kitapService.basligaGoreAra(q));
    }

    // Yazara göre ara
    @GetMapping("/ara/yazar")
    public ResponseEntity<List<Kitap>> yazaraGoreAra(@RequestParam String q) {
        return ResponseEntity.ok(kitapService.yazaraGoreAra(q));
    }

    // Müsait kitapları listele
    @GetMapping("/musait")
    public ResponseEntity<List<Kitap>> musaitKitaplariGetir() {
        return ResponseEntity.ok(kitapService.musaitKitaplariGetir());
    }

    // Ödünç alınan kitapları listele
    @GetMapping("/odunc-alinan")
    public ResponseEntity<List<Kitap>> oduncAlinanKitaplariGetir() {
        return ResponseEntity.ok(kitapService.oduncAlinanKitaplariGetir());
    }

    // Kitap ödünç ver
    @PostMapping("/{kitapId}/odunc-ver/{uyeId}")
    public ResponseEntity<Map<String, String>> kitapOduncVer(
            @PathVariable Long kitapId,
            @PathVariable Long uyeId) {
        Map<String, String> response = new HashMap<>();
        String sonuc = kitapService.kitapOduncVer(kitapId, uyeId);
        response.put("mesaj", sonuc);
        if (sonuc.contains("başarıyla")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // Kitap iade al
    @PostMapping("/{kitapId}/iade")
    public ResponseEntity<Map<String, String>> kitapIadeAl(@PathVariable Long kitapId) {
        Map<String, String> response = new HashMap<>();
        String sonuc = kitapService.kitapIadeAl(kitapId);
        response.put("mesaj", sonuc);
        if (sonuc.contains("başarıyla")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
