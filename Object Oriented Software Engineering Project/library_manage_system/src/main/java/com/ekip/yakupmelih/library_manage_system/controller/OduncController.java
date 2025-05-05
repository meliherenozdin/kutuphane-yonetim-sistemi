package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.service.OduncService;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;

@Component
public class OduncController {

    private final OduncService oduncService;
    private final UyeService uyeService;
    private final KitapService kitapService;
    private final Scanner scanner;

    public OduncController(OduncService oduncService, UyeService uyeService, KitapService kitapService) {
        this.oduncService = oduncService;
        this.uyeService = uyeService;
        this.kitapService = kitapService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Ödünç İşlemleri ===");
            System.out.println("1. Tüm Ödünçleri Listele");
            System.out.println("2. Ödünç Ara");
            System.out.println("3. Yeni Ödünç Ekle");
            System.out.println("4. Ödünç Güncelle");
            System.out.println("5. Ödünç Sil");
            System.out.println("6. Üye Ödünçlerini Listele");
            System.out.println("7. Kitap İade Et");
            System.out.println("8. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumOduncleriListele();
                case "2" -> oduncAra();
                case "3" -> yeniOduncEkle();
                case "4" -> oduncGuncelle();
                case "5" -> oduncSil();
                case "6" -> uyeOduncleriListele();
                case "7" -> kitapIade();
                case "8" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumOduncleriListele() {
        System.out.println("\n=== Tüm Ödünçler ===");
        oduncService.tumOduncleriGetir().forEach(this::oduncYazdir);
    }

    private void oduncAra() {
        System.out.print("Ödünç ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        oduncService.oduncGetir(id).ifPresentOrElse(
            this::oduncYazdir,
            () -> System.out.println("Ödünç bulunamadı!")
        );
    }

    private void yeniOduncEkle() {
        Odunc odunc = new Odunc();
        
        System.out.print("Üye ID: ");
        int uyeId = Integer.parseInt(scanner.nextLine());
        uyeService.uyeBulById(uyeId).ifPresentOrElse(
            uye -> {
                odunc.setUye(uye);
                
                System.out.print("Kitap ID: ");
                int kitapId = Integer.parseInt(scanner.nextLine());
                kitapService.kitapGetir(kitapId).ifPresentOrElse(
                    kitap -> {
                        odunc.setKitap(kitap);
                        oduncService.oduncKaydet(odunc);
                        System.out.println("Ödünç başarıyla eklendi.");
                    },
                    () -> System.out.println("Kitap bulunamadı!")
                );
            },
            () -> System.out.println("Üye bulunamadı!")
        );
    }

    private void oduncGuncelle() {
        System.out.print("Güncellenecek Ödünç ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        oduncService.oduncGetir(id).ifPresentOrElse(
            mevcutOdunc -> {
                System.out.print("Yeni Üye ID: ");
                int uyeId = Integer.parseInt(scanner.nextLine());
                uyeService.uyeBulById(uyeId).ifPresentOrElse(
                    uye -> {
                        mevcutOdunc.setUye(uye);
                        
                        System.out.print("Yeni Kitap ID: ");
                        int kitapId = Integer.parseInt(scanner.nextLine());
                        kitapService.kitapGetir(kitapId).ifPresentOrElse(
                            kitap -> {
                                mevcutOdunc.setKitap(kitap);
                                try {
                                    oduncService.oduncGuncelle(id, mevcutOdunc);
                                    System.out.println("Ödünç başarıyla güncellendi.");
                                } catch (RuntimeException e) {
                                    System.out.println("Ödünç güncellenirken hata oluştu!");
                                }
                            },
                            () -> System.out.println("Kitap bulunamadı!")
                        );
                    },
                    () -> System.out.println("Üye bulunamadı!")
                );
            },
            () -> System.out.println("Ödünç bulunamadı!")
        );
    }

    private void oduncSil() {
        System.out.print("Silinecek Ödünç ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            oduncService.oduncSil(id);
            System.out.println("Ödünç başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Ödünç silinirken hata oluştu!");
        }
    }

    private void uyeOduncleriListele() {
        System.out.print("Üye ID: ");
        int uyeId = Integer.parseInt(scanner.nextLine());
        uyeService.uyeBulById(uyeId).ifPresentOrElse(
            uye -> {
                List<Odunc> oduncler = oduncService.uyeOduncleriGetir(uyeId);
                if (oduncler.isEmpty()) {
                    System.out.println("Üyeye ait ödünç bulunamadı!");
                } else {
                    System.out.println("\n=== Üye Ödünçleri ===");
                    oduncler.forEach(this::oduncYazdir);
                }
            },
            () -> System.out.println("Üye bulunamadı!")
        );
    }

    private void kitapIade() {
        System.out.print("İade Edilecek Ödünç ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            oduncService.kitapIade(id);
            System.out.println("Kitap başarıyla iade edildi.");
        } catch (RuntimeException e) {
            System.out.println("Kitap iade edilirken hata oluştu!");
        }
    }

    private void oduncYazdir(Odunc odunc) {
        System.out.printf("ID: %d - Üye: %s %s - Kitap: %s - Durum: %s%n",
            odunc.getOduncID(),
            odunc.getUye().getUyeAdi(),
            odunc.getUye().getUyeSoyad(),
            odunc.getKitap().getKitapAdi(),
            odunc.getDurum());
    }
} 