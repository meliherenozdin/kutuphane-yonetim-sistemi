package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.service.KategoriService;

@Component
public class KategoriController {

    private final KategoriService kategoriService;
    private final Scanner scanner;

    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Kategori İşlemleri ===");
            System.out.println("1. Tüm Kategorileri Listele");
            System.out.println("2. Kategori Ara");
            System.out.println("3. Yeni Kategori Ekle");
            System.out.println("4. Kategori Güncelle");
            System.out.println("5. Kategori Sil");
            System.out.println("6. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumKategorileriListele();
                case "2" -> kategoriAra();
                case "3" -> yeniKategoriEkle();
                case "4" -> kategoriGuncelle();
                case "5" -> kategoriSil();
                case "6" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumKategorileriListele() {
        System.out.println("\n=== Tüm Kategoriler ===");
        kategoriService.tumKategorileriGetir().forEach(this::kategoriYazdir);
    }

    private void kategoriAra() {
        System.out.print("Kategori Adı: ");
        String turu = scanner.nextLine();
        List<Kategori> kategoriler = kategoriService.kategoriAra(turu);
        if (kategoriler.isEmpty()) {
            System.out.println("Kategori bulunamadı!");
        } else {
            kategoriler.forEach(this::kategoriYazdir);
        }
    }

    private void yeniKategoriEkle() {
        Kategori kategori = new Kategori();
        
        System.out.print("Kategori Adı: ");
        kategori.setTuru(scanner.nextLine());
        
        System.out.print("Açıklama: ");
        kategori.setAciklama(scanner.nextLine());
        
        kategori.setAktif(true);
        
        kategoriService.kategoriKaydet(kategori);
        System.out.println("Kategori başarıyla eklendi.");
    }

    private void kategoriGuncelle() {
        System.out.print("Güncellenecek Kategori ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Kategori kategori = new Kategori();
        
        System.out.print("Yeni Kategori Adı: ");
        kategori.setTuru(scanner.nextLine());
        
        System.out.print("Yeni Açıklama: ");
        kategori.setAciklama(scanner.nextLine());
        
        kategori.setAktif(true);
        
        try {
            kategoriService.kategoriGuncelle(id, kategori);
            System.out.println("Kategori başarıyla güncellendi.");
        } catch (RuntimeException e) {
            System.out.println("Kategori güncellenirken hata oluştu!");
        }
    }

    private void kategoriSil() {
        System.out.print("Silinecek Kategori ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            kategoriService.kategoriSil(id);
            System.out.println("Kategori başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Kategori silinirken hata oluştu!");
        }
    }

    private void kategoriYazdir(Kategori kategori) {
        System.out.printf("ID: %d - Ad: %s - Açıklama: %s%n",
            kategori.getKategoriID(),
            kategori.getTuru(),
            kategori.getAciklama());
    }
} 