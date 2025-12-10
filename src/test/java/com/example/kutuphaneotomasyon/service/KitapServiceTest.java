package com.example.kutuphaneotomasyon.service;

import com.example.kutuphaneotomasyon.model.Kitap;
import com.example.kutuphaneotomasyon.repository.KitapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * KitapService sınıfı için basit birim testleri
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Kitap Service Testleri")
class KitapServiceTest {

    @Mock
    private KitapRepository kitapRepository;


    @InjectMocks
    private KitapService kitapService;

    private Kitap testKitap;

    @BeforeEach
    void setUp() {
        // Her testten önce örnek kitap oluştur
        testKitap = new Kitap(1L, "978-3-16-148410-0", "Test Kitap", "Test Yazar", "Test Yayınevi", 2023);
    }

    @Test
    @DisplayName("Test 1: Kitap başarıyla eklenmeli")
    void testKitapEkle() {
        // Given - Mock davranışını ayarla
        when(kitapRepository.ekle(any(Kitap.class))).thenReturn(testKitap);

        // When - Metodu çağır
        Kitap result = kitapService.kitapEkle(testKitap);

        // Then - Sonucu doğrula
        assertNotNull(result);
        assertEquals("Test Kitap ", result.getBaslik());
        assertEquals("Test Yazar", result.getYazar());
        verify(kitapRepository, times(1)).ekle(testKitap);
    }

    @Test
    @DisplayName("Test 2: Tüm kitaplar listelenebilmeli")
    void testTumKitaplariGetir() {
        // Given
        List<Kitap> kitaplar = Arrays.asList(
                testKitap,
                new Kitap(2L, "978-0-06-112008-4", "1984", "George Orwell", "Can Yayınları", 1949)
        );
        when(kitapRepository.tumunuGetir()).thenReturn(kitaplar);

        // When
        List<Kitap> result = kitapService.tumKitaplariGetir();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Kitap", result.get(0).getBaslik());
        verify(kitapRepository, times(1)).tumunuGetir();
    }

    @Test
    @DisplayName("Test 3: Kitap ID ile bulunabilmeli")
    void testKitapBul() {
        // Given
        when(kitapRepository.idIleBul(1L)).thenReturn(Optional.of(testKitap));

        // When
        Optional<Kitap> result = kitapService.kitapBul(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Kitap", result.get().getBaslik());
        verify(kitapRepository, times(1)).idIleBul(1L);
    }
}

