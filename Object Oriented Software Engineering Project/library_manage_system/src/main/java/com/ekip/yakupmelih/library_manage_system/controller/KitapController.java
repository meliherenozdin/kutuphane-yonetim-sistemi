package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.KategoriService;

@Component
public class KitapController {

    private final KitapService kitapService;
    private final KategoriService kategoriService;
    private final Scanner scanner;

    public KitapController(KitapService kitapService, KategoriService kategoriService) {
        this.kitapService = kitapService;
        this.kategoriService = kategoriService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Kitap İşlemleri ===");
            System.out.println("1. Tüm Kitapları Listele");
            System.out.println("2. Kitap Ara");
            System.out.println("3. Yeni Kitap Ekle");
            System.out.println("4. Kitap Güncelle");
            System.out.println("5. Kitap Sil");
            System.out.println("6. Kategoriye Göre Kitapları Listele");
            System.out.println("7. Stokta Olan Kitapları Listele");
            System.out.println("8. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumKitaplariListele();
                case "2" -> kitapAra();
                case "3" -> yeniKitapEkle();
                case "4" -> kitapGuncelle();
                case "5" -> kitapSil();
                case "6" -> kategoriyeGoreListele();
                case "7" -> stoktaOlanKitaplariListele();
                case "8" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumKitaplariListele() {
        System.out.println("\n=== Tüm Kitaplar ===");
        kitapService.tumKitaplariGetir().forEach(this::kitapYazdir);
    }

    private void kitapAra() {
        System.out.println("\n=== Kitap Arama ===");
        System.out.println("1. ISBN ile Ara");
        System.out.println("2. İsim ile Ara");
        System.out.print("Seçiminiz: ");
        
        String secim = scanner.nextLine();
        
        switch (secim) {
            case "1" -> {
                System.out.print("ISBN: ");
                String isbn = scanner.nextLine();
                kitapService.kitapBulByIsbn(isbn).ifPresentOrElse(
                    this::kitapYazdir,
                    () -> System.out.println("Kitap bulunamadı!")
                );
            }
            case "2" -> {
                System.out.print("Kitap Adı: ");
                String kitapAdi = scanner.nextLine();
                List<Kitap> kitaplar = kitapService.kitapAra(kitapAdi);
                if (kitaplar.isEmpty()) {
                    System.out.println("Kitap bulunamadı!");
                } else {
                    kitaplar.forEach(this::kitapYazdir);
                }
            }
            default -> System.out.println("Geçersiz seçim!");
        }
    }

    private void yeniKitapEkle() {
        Kitap kitap = new Kitap();
        
        System.out.print("Kitap Adı: ");
        kitap.setKitapAdi(scanner.nextLine());
        
        System.out.print("ISBN: ");
        kitap.setIsbn(scanner.nextLine());
        
        System.out.print("Basım Yılı (YYYY-MM-DD): ");
        kitap.setBasimYili(LocalDate.parse(scanner.nextLine()));
        
        System.out.print("Yayınevi: ");
        kitap.setYayinevi(scanner.nextLine());
        
        System.out.print("Adet: ");
        kitap.setAdet(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Kategori ID: ");
        int kategoriId = Integer.parseInt(scanner.nextLine());
        kategoriService.kategoriGetir(kategoriId).ifPresent(kitap::setKategori);
        
        kitap.setAktif(true);
        
        kitapService.kitapKaydet(kitap);
        System.out.println("Kitap başarıyla eklendi.");
    }

    private void kitapGuncelle() {
        System.out.print("Güncellenecek Kitap ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Kitap kitap = new Kitap();
        
        System.out.print("Yeni Kitap Adı: ");
        kitap.setKitapAdi(scanner.nextLine());
        
        System.out.print("Yeni ISBN: ");
        kitap.setIsbn(scanner.nextLine());
        
        System.out.print("Yeni Basım Yılı (YYYY-MM-DD): ");
        kitap.setBasimYili(LocalDate.parse(scanner.nextLine()));
        
        System.out.print("Yeni Yayınevi: ");
        kitap.setYayinevi(scanner.nextLine());
        
        System.out.print("Yeni Adet: ");
        kitap.setAdet(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Yeni Kategori ID: ");
        int kategoriId = Integer.parseInt(scanner.nextLine());
        kategoriService.kategoriGetir(kategoriId).ifPresent(kitap::setKategori);
        
        kitap.setAktif(true);
        
        try {
            kitapService.kitapGuncelle(id, kitap);
            System.out.println("Kitap başarıyla güncellendi.");
        } catch (RuntimeException e) {
            System.out.println("Kitap güncellenirken hata oluştu!");
        }
    }

    private void kitapSil() {
        System.out.print("Silinecek Kitap ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            kitapService.kitapSil(id);
            System.out.println("Kitap başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Kitap silinirken hata oluştu!");
        }
    }

    private void kategoriyeGoreListele() {
        System.out.print("Kategori ID: ");
        int kategoriId = Integer.parseInt(scanner.nextLine());
        kategoriService.kategoriGetir(kategoriId).ifPresentOrElse(
            kategori -> {
                System.out.println("\n=== " + kategori.getTuru() + " Kategorisindeki Kitaplar ===");
                kitapService.kategoriyeGoreAktifKitaplariGetir(kategori).forEach(this::kitapYazdir);
            },
            () -> System.out.println("Kategori bulunamadı!")
        );
    }

    private void stoktaOlanKitaplariListele() {
        System.out.println("\n=== Stokta Olan Kitaplar ===");
        kitapService.stoktaOlanKitaplariGetir().forEach(this::kitapYazdir);
    }

    private void kitapYazdir(Kitap kitap) {
        System.out.printf("ID: %d - Ad: %s - ISBN: %s - Stok: %d%n",
            kitap.getKitapID(),
            kitap.getKitapAdi(),
            kitap.getIsbn(),
            kitap.getAdet());
    }
} 