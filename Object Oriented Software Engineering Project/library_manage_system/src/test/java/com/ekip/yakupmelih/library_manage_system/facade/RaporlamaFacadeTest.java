package com.ekip.yakupmelih.library_manage_system.facade;

import com.ekip.yakupmelih.library_manage_system.model.*;
import com.ekip.yakupmelih.library_manage_system.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RaporlamaFacadeTest {

    @Mock
    private KitapService kitapService;

    @Mock
    private OduncService oduncService;

    @Mock
    private UyeService uyeService;

    @Mock
    private CezaService cezaService;

    @Mock
    private RezervasyonService rezervasyonService;

    @Mock
    private BagisService bagisService;

    @InjectMocks
    private RaporlamaFacade raporlamaFacade;

    private Uye testUye;
    private Kitap testKitap;
    private List<Odunc> testOduncler;
    private List<Ceza> testCezalar;
    private List<Rezervasyon> testRezervasyonlar;
    private List<Bagis> testBagislar;

    @BeforeEach
    void setUp() {
        // Test verileri hazırlama
        testUye = new Uye();
        testUye.setUyeID(1);
        testUye.setUyeAdi("Test");
        testUye.setUyeSoyad("Kullanıcı");
        testUye.setEmail("test@test.com");
        testUye.setTelNo("5551234567");
        testUye.setCreatedAt(LocalDateTime.now());
        testUye.setAktif(true);

        testKitap = new Kitap();
        testKitap.setKitapID(1);
        testKitap.setKitapAdi("Test Kitap");
        testKitap.setIsbn("1234567890");
        testKitap.setBasimYili(LocalDate.of(2020, 1, 1));
        testKitap.setYayinevi("Test Yayınevi");
        testKitap.setAdet(5);
        testKitap.setDurum(Kitap.KitapDurum.AKTIF);

        // Odunc test listesi
        testOduncler = new ArrayList<>();
        Odunc odunc = new Odunc();
        odunc.setOduncID(1);
        odunc.setUye(testUye);
        odunc.setKitap(testKitap);
        odunc.setOduncAlmaTarih(LocalDate.now().minusDays(5));
        odunc.setSonTeslimTarihi(LocalDate.now().plusDays(10));
        testOduncler.add(odunc);

        // Ceza test listesi
        testCezalar = new ArrayList<>();
        Ceza ceza = new Ceza();
        ceza.setCezaID(1);
        ceza.setUye(testUye);
        ceza.setCezaMiktar(new BigDecimal("10.0"));
        ceza.setAciklama("Geç teslim");
        ceza.setCezaTarih(LocalDate.now().minusDays(2));
        testCezalar.add(ceza);

        // Rezervasyon test listesi
        testRezervasyonlar = new ArrayList<>();
        Rezervasyon rezervasyon = new Rezervasyon();
        rezervasyon.setRezervasyonID(1);
        rezervasyon.setUye(testUye);
        rezervasyon.setKitap(testKitap);
        rezervasyon.setRezervasyonTarih(LocalDate.now());
        testRezervasyonlar.add(rezervasyon);

        // Bağış test listesi
        testBagislar = new ArrayList<>();
        Bagis bagis = new Bagis();
        bagis.setBagisID(1);
        bagis.setUye(testUye);
        bagis.setKitap(testKitap);
        bagis.setAdet(2);
        bagis.setBagisTarih(LocalDate.now().minusDays(10));
        testBagislar.add(bagis);
    }

    @Test
    void testGenelDurumRaporu() {
        // Mock service methodları için davranışları tanımlama
        when(kitapService.tumKitaplariGetir()).thenReturn(List.of(testKitap));
        when(kitapService.stoktaOlanKitaplariGetir()).thenReturn(List.of(testKitap));
        when(uyeService.tumUyeleriGetir()).thenReturn(List.of(testUye));
        when(uyeService.aktifUyeleriGetir()).thenReturn(List.of(testUye));
        when(oduncService.aktifOdunclariGetir()).thenReturn(testOduncler);
        when(oduncService.gecikmisOdunclariGetir(any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(cezaService.aktifCezalariGetir()).thenReturn(testCezalar);
        when(rezervasyonService.aktifRezervasyonlariGetir()).thenReturn(testRezervasyonlar);

        // Test edilecek metod çağrısı
        Map<String, Object> rapor = raporlamaFacade.genelDurumRaporu();

        // Sonuçları doğrulama (assertions)
        assertNotNull(rapor);
        assertEquals(1, rapor.get("toplamKitapSayisi"));
        assertEquals(1, rapor.get("stoktaOlanKitapSayisi"));
        assertEquals(1, rapor.get("toplamUyeSayisi"));
        assertEquals(1, rapor.get("aktifUyeSayisi"));
        assertEquals(1, rapor.get("aktifOduncSayisi"));
        assertEquals(0, rapor.get("gecikmisOduncSayisi"));
        assertEquals(1, rapor.get("aktifCezaSayisi"));
        assertEquals(1, rapor.get("aktifRezervasyonSayisi"));

        // Service metotlarının çağrıldığını doğrulama
        verify(kitapService).tumKitaplariGetir();
        verify(kitapService).stoktaOlanKitaplariGetir();
        verify(uyeService).tumUyeleriGetir();
        verify(uyeService).aktifUyeleriGetir();
        verify(oduncService).aktifOdunclariGetir();
        verify(oduncService).gecikmisOdunclariGetir(any(LocalDate.class));
        verify(cezaService).aktifCezalariGetir();
        verify(rezervasyonService).aktifRezervasyonlariGetir();
    }

    @Test
    void testUyeDetayRaporu() {
        // Mock service methodları için davranışları tanımlama
        when(uyeService.uyeBulById(1)).thenReturn(Optional.of(testUye));
        when(oduncService.findByUyeId(1)).thenReturn(testOduncler);
        when(cezaService.uyeCezalariGetir(1)).thenReturn(testCezalar);
        when(rezervasyonService.uyeRezervasyonlariGetir(1)).thenReturn(testRezervasyonlar);
        when(bagisService.bagisciBagislariGetir(1)).thenReturn(testBagislar);

        // Test edilecek metod çağrısı
        Map<String, Object> rapor = raporlamaFacade.uyeDetayRaporu(1);

        // Sonuçları doğrulama
        assertNotNull(rapor);
        assertTrue(rapor.get("uyeBilgileri").toString().contains("Test Kullanıcı"));
        assertTrue(rapor.get("aktifOduncler").toString().contains("Test Kitap"));
        assertTrue(rapor.get("aktifCezalar").toString().contains("Geç teslim"));
        assertTrue(rapor.get("aktifRezervasyonlar").toString().contains("Test Kitap"));
        assertTrue(rapor.get("bagislar").toString().contains("Test Kitap"));

        // Service metotlarının çağrıldığını doğrulama
        verify(uyeService).uyeBulById(1);
        verify(oduncService).findByUyeId(1);
        verify(cezaService).uyeCezalariGetir(1);
        verify(rezervasyonService).uyeRezervasyonlariGetir(1);
        verify(bagisService).bagisciBagislariGetir(1);
    }

    @Test
    void testKitapDetayRaporu() {
        // Mock service methodları için davranışları tanımlama
        when(kitapService.kitapGetir(1)).thenReturn(Optional.of(testKitap));
        when(oduncService.kitapBazliOdunclariGetir(testKitap)).thenReturn(testOduncler);
        when(rezervasyonService.kitapBazliRezervasyonlariGetir(testKitap)).thenReturn(testRezervasyonlar);

        // Test edilecek metod çağrısı
        Map<String, Object> rapor = raporlamaFacade.kitapDetayRaporu(1);

        // Sonuçları doğrulama
        assertNotNull(rapor);
        assertTrue(rapor.get("kitapBilgileri").toString().contains("Test Kitap"));
        assertTrue(rapor.get("aktifOduncler").toString().contains("Test Kullanıcı"));
        assertTrue(rapor.get("aktifRezervasyonlar").toString().contains("Test Kullanıcı"));

        // Service metotlarının çağrıldığını doğrulama
        verify(kitapService).kitapGetir(1);
        verify(oduncService).kitapBazliOdunclariGetir(testKitap);
        verify(rezervasyonService).kitapBazliRezervasyonlariGetir(testKitap);
    }

    @Test
    void testTarihAraligiRaporu() {
        LocalDate baslangic = LocalDate.now().minusDays(30);
        LocalDate bitis = LocalDate.now();

        // Mock service methodları için davranışları tanımlama
        when(oduncService.tumOduncleriGetir()).thenReturn(testOduncler);

        // Test edilecek metod çağrısı
        Map<String, Object> rapor = raporlamaFacade.tarihAraligiRaporu(baslangic, bitis);

        // Sonuçları doğrulama
        assertNotNull(rapor);
        assertTrue(rapor.get("oduncler").toString().contains("Test Kitap"));

        // Service metotlarının çağrıldığını doğrulama
        verify(oduncService).tumOduncleriGetir();
    }
}