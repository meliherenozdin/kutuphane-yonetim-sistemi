package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.service.CezaService;

@Component
public class CezaController {

    private final CezaService cezaService;
    private final Scanner scanner;

    public CezaController(CezaService cezaService) {
        this.cezaService = cezaService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Ceza İşlemleri ===");
            System.out.println("1. Tüm Cezaları Listele");
            System.out.println("2. Ceza Detayı Görüntüle");
            System.out.println("3. Yeni Ceza Ekle");
            System.out.println("4. Ceza Güncelle");
            System.out.println("5. Ceza Sil");
            System.out.println("6. Üye Cezalarını Görüntüle");
            System.out.println("7. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            
            String secim = scanner.nextLine();
            
            switch (secim) {
                case "1" -> tumCezalariListele();
                case "2" -> cezaDetayGoruntule();
                case "3" -> yeniCezaEkle();
                case "4" -> cezaGuncelle();
                case "5" -> cezaSil();
                case "6" -> uyeCezalariGoruntule();
                case "7" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumCezalariListele() {
        System.out.println("\n=== Tüm Cezalar ===");
        cezaService.tumCezalariGetir().forEach(this::cezaYazdir);
    }

    private void cezaDetayGoruntule() {
        System.out.print("Ceza ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        cezaService.cezaGetir(id).ifPresentOrElse(
            this::cezaYazdir,
            () -> System.out.println("Ceza bulunamadı!")
        );
    }

    private void yeniCezaEkle() {
        Ceza ceza = new Ceza();
        System.out.print("Ceza Miktarı: ");
        ceza.setCezaMiktar(new BigDecimal(scanner.nextLine()));
        cezaService.cezaKaydet(ceza);
        System.out.println("Ceza başarıyla eklendi.");
    }

    private void cezaGuncelle() {
        System.out.print("Güncellenecek Ceza ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Yeni Ceza Miktarı: ");
        BigDecimal yeniMiktar = new BigDecimal(scanner.nextLine());
        
        Ceza ceza = new Ceza();
        ceza.setCezaMiktar(yeniMiktar);
        
        try {
            cezaService.cezaGuncelle(id, ceza);
            System.out.println("Ceza başarıyla güncellendi.");
        } catch (RuntimeException e) {
            System.out.println("Ceza güncellenirken hata oluştu!");
        }
    }

    private void cezaSil() {
        System.out.print("Silinecek Ceza ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            cezaService.cezaSil(id);
            System.out.println("Ceza başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Ceza silinirken hata oluştu!");
        }
    }

    private void uyeCezalariGoruntule() {
        System.out.print("Üye ID: ");
        int uyeId = Integer.parseInt(scanner.nextLine());
        System.out.println("\n=== Üye Cezaları ===");
        cezaService.uyeCezalariGetir(uyeId).forEach(this::cezaYazdir);
    }

    private void cezaYazdir(Ceza ceza) {
        System.out.printf("ID: %d - Miktar: %.2f ₺ - Tarih: %s%n",
            ceza.getCezaID(),
            ceza.getCezaMiktar(),
            ceza.getCezaTarih());
    }
} 