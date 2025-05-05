package com.ekip.yakupmelih.library_manage_system.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RaporlamaFacadeIntegrationTest {

    @Autowired
    private RaporlamaFacade raporlamaFacade;

    @Test
    void testGenelDurumRaporu() {
        // Gerçek veritabanı üzerinde test
        Map<String, Object> rapor = raporlamaFacade.genelDurumRaporu();

        // Sonucun boş olmamasını doğrulama
        assertNotNull(rapor);
        assertNotNull(rapor.get("toplamKitapSayisi"));
        assertNotNull(rapor.get("toplamUyeSayisi"));
        assertNotNull(rapor.get("aktifUyeSayisi"));
        assertNotNull(rapor.get("aktifOduncSayisi"));
    }

    @Test
    void testTarihAraligiRaporu() {
        // Son 3 ayın raporu
        LocalDate baslangic = LocalDate.now().minusMonths(3);
        LocalDate bitis = LocalDate.now();

        Map<String, Object> rapor = raporlamaFacade.tarihAraligiRaporu(baslangic, bitis);

        assertNotNull(rapor);
        assertNotNull(rapor.get("oduncler"));
    }

    // Varolan bir üye ID ile test yalnızca veritabanında
    // kayıt varsa çalışacaktır, aksi halde bu test atlanabilir
    /*
     * @Test
     * void testUyeDetayRaporu() {
     * // Veritabanında var olan bir üye ID kullanın
     * int uyeId = 1;
     * 
     * Map<String, Object> rapor = raporlamaFacade.uyeDetayRaporu(uyeId);
     * 
     * assertNotNull(rapor);
     * // Üye bulunamadı mesajı içermemeli
     * assertFalse(rapor.get("uyeBilgileri").toString().contains("Üye bulunamadı"));
     * }
     */

    // Varolan bir kitap ID ile test yalnızca veritabanında
    // kayıt varsa çalışacaktır, aksi halde bu test atlanabilir
    /*
     * @Test
     * void testKitapDetayRaporu() {
     * // Veritabanında var olan bir kitap ID kullanın
     * int kitapId = 1;
     * 
     * Map<String, Object> rapor = raporlamaFacade.kitapDetayRaporu(kitapId);
     * 
     * assertNotNull(rapor);
     * // Kitap bulunamadı mesajı içermemeli
     * assertFalse(rapor.get("kitapBilgileri").toString().
     * contains("Kitap bulunamadı"));
     * }
     */
}