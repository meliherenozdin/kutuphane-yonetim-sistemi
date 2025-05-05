package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Raf;
import com.ekip.yakupmelih.library_manage_system.service.RafService;

@Component
public class RafController {

    private final RafService rafService;
    private final Scanner scanner;

    public RafController(RafService rafService) {
        this.rafService = rafService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Raf İşlemleri ===");
            System.out.println("1. Tüm Rafları Listele");
            System.out.println("2. Raf Ara");
            System.out.println("3. Yeni Raf Ekle");
            System.out.println("4. Raf Güncelle");
            System.out.println("5. Raf Sil");
            System.out.println("6. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumRaflariListele();
                case "2" -> rafAra();
                case "3" -> yeniRafEkle();
                case "4" -> rafGuncelle();
                case "5" -> rafSil();
                case "6" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumRaflariListele() {
        System.out.println("\n=== Tüm Raflar ===");
        rafService.tumRaflariGetir().forEach(this::rafYazdir);
    }

    private void rafAra() {
        System.out.print("Raf No: ");
        String rafNo = scanner.nextLine();
        List<Raf> raflar = rafService.rafAra(rafNo);
        if (raflar.isEmpty()) {
            System.out.println("Raf bulunamadı!");
        } else {
            raflar.forEach(this::rafYazdir);
        }
    }

    private void yeniRafEkle() {
        Raf raf = new Raf();
        
        System.out.print("Raf No: ");
        raf.setRafNo(scanner.nextLine());
        
        System.out.print("Açıklama: ");
        raf.setAciklama(scanner.nextLine());
        
        raf.setAktif(true);
        
        rafService.rafKaydet(raf);
        System.out.println("Raf başarıyla eklendi.");
    }

    private void rafGuncelle() {
        System.out.print("Güncellenecek Raf No: ");
        String rafNo = scanner.nextLine();
        
        Raf raf = new Raf();
        raf.setRafNo(rafNo);
        
        System.out.print("Yeni Açıklama: ");
        raf.setAciklama(scanner.nextLine());
        
        raf.setAktif(true);
        
        try {
            rafService.rafGuncelle(rafNo, raf);
            System.out.println("Raf başarıyla güncellendi.");
        } catch (RuntimeException e) {
            System.out.println("Raf güncellenirken hata oluştu!");
        }
    }

    private void rafSil() {
        System.out.print("Silinecek Raf No: ");
        String rafNo = scanner.nextLine();
        try {
            rafService.rafSil(rafNo);
            System.out.println("Raf başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Raf silinirken hata oluştu!");
        }
    }

    private void rafYazdir(Raf raf) {
        System.out.printf("No: %s - Açıklama: %s%n",
            raf.getRafNo(),
            raf.getAciklama());
    }
} 