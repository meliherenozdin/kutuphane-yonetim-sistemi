package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Rezervasyon;
import com.ekip.yakupmelih.library_manage_system.service.RezervasyonService;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;

@Component
public class RezervasyonController {

    private final RezervasyonService rezervasyonService;
    private final UyeService uyeService;
    private final KitapService kitapService;
    private final Scanner scanner;

    public RezervasyonController(RezervasyonService rezervasyonService, UyeService uyeService, KitapService kitapService) {
        this.rezervasyonService = rezervasyonService;
        this.uyeService = uyeService;
        this.kitapService = kitapService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Rezervasyon İşlemleri ===");
            System.out.println("1. Tüm Rezervasyonları Listele");
            System.out.println("2. Rezervasyon Ara");
            System.out.println("3. Yeni Rezervasyon Ekle");
            System.out.println("4. Rezervasyon Güncelle");
            System.out.println("5. Rezervasyon Sil");
            System.out.println("6. Üye Rezervasyonlarını Listele");
            System.out.println("7. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumRezervasyonlariListele();
                case "2" -> rezervasyonAra();
                case "3" -> yeniRezervasyonEkle();
                case "4" -> rezervasyonGuncelle();
                case "5" -> rezervasyonSil();
                case "6" -> uyeRezervasyonlariListele();
                case "7" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumRezervasyonlariListele() {
        System.out.println("\n=== Tüm Rezervasyonlar ===");
        rezervasyonService.tumRezervasyonlariGetir().forEach(this::rezervasyonYazdir);
    }

    private void rezervasyonAra() {
        System.out.print("Rezervasyon ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        rezervasyonService.rezervasyonGetir(id).ifPresentOrElse(
            this::rezervasyonYazdir,
            () -> System.out.println("Rezervasyon bulunamadı!")
        );
    }

    private void yeniRezervasyonEkle() {
        Rezervasyon rezervasyon = new Rezervasyon();
        
        System.out.print("Üye ID: ");
        int uyeId = Integer.parseInt(scanner.nextLine());
        uyeService.uyeBulById(uyeId).ifPresentOrElse(
            uye -> {
                rezervasyon.setUye(uye);
                
                System.out.print("Kitap ID: ");
                int kitapId = Integer.parseInt(scanner.nextLine());
                kitapService.kitapGetir(kitapId).ifPresentOrElse(
                    kitap -> {
                        rezervasyon.setKitap(kitap);
                        rezervasyonService.rezervasyonKaydet(rezervasyon);
                        System.out.println("Rezervasyon başarıyla eklendi.");
                    },
                    () -> System.out.println("Kitap bulunamadı!")
                );
            },
            () -> System.out.println("Üye bulunamadı!")
        );
    }

    private void rezervasyonGuncelle() {
        System.out.print("Güncellenecek Rezervasyon ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        rezervasyonService.rezervasyonGetir(id).ifPresentOrElse(
            mevcutRezervasyon -> {
                System.out.print("Yeni Üye ID: ");
                int uyeId = Integer.parseInt(scanner.nextLine());
                uyeService.uyeBulById(uyeId).ifPresentOrElse(
                    uye -> {
                        mevcutRezervasyon.setUye(uye);
                        
                        System.out.print("Yeni Kitap ID: ");
                        int kitapId = Integer.parseInt(scanner.nextLine());
                        kitapService.kitapGetir(kitapId).ifPresentOrElse(
                            kitap -> {
                                mevcutRezervasyon.setKitap(kitap);
                                try {
                                    rezervasyonService.rezervasyonGuncelle(id, mevcutRezervasyon);
                                    System.out.println("Rezervasyon başarıyla güncellendi.");
                                } catch (RuntimeException e) {
                                    System.out.println("Rezervasyon güncellenirken hata oluştu!");
                                }
                            },
                            () -> System.out.println("Kitap bulunamadı!")
                        );
                    },
                    () -> System.out.println("Üye bulunamadı!")
                );
            },
            () -> System.out.println("Rezervasyon bulunamadı!")
        );
    }

    private void rezervasyonSil() {
        System.out.print("Silinecek Rezervasyon ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            rezervasyonService.rezervasyonSil(id);
            System.out.println("Rezervasyon başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Rezervasyon silinirken hata oluştu!");
        }
    }

    private void uyeRezervasyonlariListele() {
        System.out.print("Üye ID: ");
        int uyeId = Integer.parseInt(scanner.nextLine());
        uyeService.uyeBulById(uyeId).ifPresentOrElse(
            uye -> {
                List<Rezervasyon> rezervasyonlar = rezervasyonService.uyeRezervasyonlariGetir(uyeId);
                if (rezervasyonlar.isEmpty()) {
                    System.out.println("Üyeye ait rezervasyon bulunamadı!");
                } else {
                    System.out.println("\n=== Üye Rezervasyonları ===");
                    rezervasyonlar.forEach(this::rezervasyonYazdir);
                }
            },
            () -> System.out.println("Üye bulunamadı!")
        );
    }

    private void rezervasyonYazdir(Rezervasyon rezervasyon) {
        System.out.printf("ID: %d - Üye: %s %s - Kitap: %s - Durum: %s%n",
            rezervasyon.getRezervasyonID(),
            rezervasyon.getUye().getUyeAdi(),
            rezervasyon.getUye().getUyeSoyad(),
            rezervasyon.getKitap().getKitapAdi(),
            rezervasyon.getDurum());
    }
} 