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
                case "0" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void genelDurumRaporu() {
        Map<String, Object> rapor = raporlamaFacade.genelDurumRaporu();
        System.out.println("\n--- Genel Durum Raporu ---");
        rapor.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void uyeDetayRaporu() {
        System.out.print("Üye ID: ");
        int uyeId = Integer.parseInt(scanner.nextLine());
        Map<String, Object> rapor = raporlamaFacade.uyeDetayRaporu(uyeId);
        System.out.println("\n--- Üye Detay Raporu ---");
        rapor.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void kitapDetayRaporu() {
        System.out.print("Kitap ID: ");
        int kitapId = Integer.parseInt(scanner.nextLine());
        Map<String, Object> rapor = raporlamaFacade.kitapDetayRaporu(kitapId);
        System.out.println("\n--- Kitap Detay Raporu ---");
        rapor.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void tarihAraligiRaporu() {
        System.out.print("Başlangıç tarihi (YYYY-MM-DD): ");
        LocalDate baslangic = LocalDate.parse(scanner.nextLine());
        System.out.print("Bitiş tarihi (YYYY-MM-DD): ");
        LocalDate bitis = LocalDate.parse(scanner.nextLine());
        Map<String, Object> rapor = raporlamaFacade.tarihAraligiRaporu(baslangic, bitis);
        System.out.println("\n--- Tarih Aralığı Raporu ---");
        rapor.forEach((k, v) -> System.out.println(k + ": " + v));
    }
} 