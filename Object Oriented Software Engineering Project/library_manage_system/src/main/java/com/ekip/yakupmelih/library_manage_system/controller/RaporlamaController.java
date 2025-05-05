package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.facade.RaporlamaFacade;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

@Component
public class RaporlamaController {
    private final RaporlamaFacade raporlamaFacade;
    private final Scanner scanner = new Scanner(System.in);

    public RaporlamaController(RaporlamaFacade raporlamaFacade) {
        this.raporlamaFacade = raporlamaFacade;
    }

    // Kullanıcı tipi parametreli menü
    public void menu(String kullaniciTipi) {
        if (!"personel".equalsIgnoreCase(kullaniciTipi)) {
            return;
        }
        while (true) {
            System.out.println("\n=== Raporlama Menüsü ===");
            System.out.println("1. Genel Durum Raporu");
            System.out.println("2. Üye Detay Raporu");
            System.out.println("3. Kitap Detay Raporu");
            System.out.println("4. Tarih Aralığı Raporu");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> genelDurumRaporu();
                case "2" -> uyeDetayRaporu();
                case "3" -> kitapDetayRaporu();
                case "4" -> tarihAraligiRaporu();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void genelDurumRaporu() {
        try {
            Map<String, Object> rapor = raporlamaFacade.genelDurumRaporu();
            System.out.println("\n--- GENEL DURUM RAPORU ---");
            rapor.forEach((k, v) -> {
                String baslik = switch (k) {
                    case "toplamKitapSayisi" -> "Toplam Kitap Sayısı";
                    case "stoktaOlanKitapSayisi" -> "Stokta Olan Kitap Sayısı";
                    case "toplamUyeSayisi" -> "Toplam Üye Sayısı";
                    case "aktifUyeSayisi" -> "Aktif Üye Sayısı";
                    case "aktifOduncSayisi" -> "Aktif Ödünç Sayısı";
                    case "gecikmisOduncSayisi" -> "Gecikmiş Ödünç Sayısı";
                    case "aktifCezaSayisi" -> "Aktif Ceza Sayısı";
                    case "aktifRezervasyonSayisi" -> "Aktif Rezervasyon Sayısı";
                    default -> k;
                };
                System.out.println(baslik + ": " + v);
            });
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void uyeDetayRaporu() {
        try {
            System.out.print("Üye ID: ");
            int uyeId = Integer.parseInt(scanner.nextLine());
            Map<String, Object> rapor = raporlamaFacade.uyeDetayRaporu(uyeId);
            System.out.println("\n--- ÜYE DETAY RAPORU ---");
            for (Object value : rapor.values()) {
                System.out.println(value);
            }
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı. Lütfen sayısal bir değer giriniz.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void kitapDetayRaporu() {
        try {
            System.out.print("Kitap ID: ");
            int kitapId = Integer.parseInt(scanner.nextLine());
            Map<String, Object> rapor = raporlamaFacade.kitapDetayRaporu(kitapId);
            System.out.println("\n--- KİTAP DETAY RAPORU ---");
            for (Object value : rapor.values()) {
                System.out.println(value);
            }
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı. Lütfen sayısal bir değer giriniz.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void tarihAraligiRaporu() {
        try {
            System.out.print("Başlangıç tarihi (YYYY-MM-DD): ");
            LocalDate baslangic = LocalDate.parse(scanner.nextLine());
            System.out.print("Bitiş tarihi (YYYY-MM-DD): ");
            LocalDate bitis = LocalDate.parse(scanner.nextLine());
            Map<String, Object> rapor = raporlamaFacade.tarihAraligiRaporu(baslangic, bitis);
            System.out.println("\n--- TARİH ARALIĞI RAPORU (" + baslangic + " - " + bitis + ") ---");
            for (Object value : rapor.values()) {
                System.out.println(value);
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}