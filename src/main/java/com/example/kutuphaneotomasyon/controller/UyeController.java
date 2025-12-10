package com.example.kutuphaneotomasyon.controller;

import com.example.kutuphaneotomasyon.model.Uye;
import com.example.kutuphaneotomasyon.service.UyeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uyeler")
public class UyeController {

    private final UyeService uyeService;

    public UyeController(UyeService uyeService) {
        this.uyeService = uyeService;
    }

    // Tüm üyeleri listele
    @GetMapping
    public ResponseEntity<List<Uye>> tumUyeleriGetir() {
        return ResponseEntity.ok(uyeService.tumUyeleriGetir());
    }

    // ID ile üye getir
    @GetMapping("/{id}")
    public ResponseEntity<Uye> uyeGetir(@PathVariable Long id) {
        return uyeService.uyeBul(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Yeni üye ekle
    @PostMapping
    public ResponseEntity<Uye> uyeEkle(@RequestBody Uye uye) {
        Uye yeniUye = uyeService.uyeEkle(uye);
        return ResponseEntity.ok(yeniUye);
    }

    // Üye güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Uye> uyeGuncelle(@PathVariable Long id, @RequestBody Uye uye) {
        uye.setId(id);
        Uye guncellenenUye = uyeService.uyeGuncelle(uye);
        if (guncellenenUye != null) {
            return ResponseEntity.ok(guncellenenUye);
        }
        return ResponseEntity.notFound().build();
    }

    // Üye sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> uyeSil(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        String sonuc = uyeService.uyeSil(id);
        response.put("mesaj", sonuc);
        if (sonuc.contains("başarıyla")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // Ada göre ara
    @GetMapping("/ara")
    public ResponseEntity<List<Uye>> adaGoreAra(@RequestParam String q) {
        return ResponseEntity.ok(uyeService.adaGoreAra(q));
    }
}

