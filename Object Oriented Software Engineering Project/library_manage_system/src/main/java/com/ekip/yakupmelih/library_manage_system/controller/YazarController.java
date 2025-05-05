package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Yazar;
import com.ekip.yakupmelih.library_manage_system.service.YazarService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class YazarController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final YazarService yazarService;

    public YazarController(YazarService yazarService) {
        this.yazarService = yazarService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Yazar İşlemleri Menüsü ===");
            System.out.println("1. Tüm Yazarları Listele");
            System.out.println("2. Aktif Yazarları Listele");
            System.out.println("3. İsme Göre Yazarları Ara");
            System.out.println("4. Yazar Ekle");
            System.out.println("5. Yazar Güncelle");
            System.out.println("6. Yazar Sil (Pasif Yap)");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> yazarListele(yazarService.tumYazarlariGetir());
                case "2" -> yazarListele(yazarService.aktifYazarlariGetir());
                case "3" -> ismeGoreYazarAra();
                case "4" -> yazarEkle();
                case "5" -> yazarGuncelle();
                case "6" -> yazarSil();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void yazarListele(List<Yazar> yazarlar) {
        if (yazarlar.isEmpty()) {
            System.out.println("Listelenecek yazar bulunamadı.");
            return;
        }

        System.out.println("\n=== Yazarlar ===");
        System.out.printf("%-5s %-15s %-15s %-8s %-40s%n", "ID", "Ad", "Soyad", "Durum", "Açıklama");
        System.out.println("-".repeat(80));

        for (Yazar yazar : yazarlar) {
            System.out.printf("%-5d %-15s %-15s %-8s %-40s%n",
                    yazar.getYazarID(),
                    yazar.getYazarAdi(),
                    yazar.getYazarSoyad(),
                    yazar.isAktif() ? "Aktif" : "Pasif",
                    yazar.getAciklama() != null ? yazar.getAciklama() : "-");
        }
    }

    private void ismeGoreYazarAra() {
        System.out.print("Aramak istediğiniz yazar adı: ");
        String yazarAdi = scanner.nextLine();
        List<Yazar> yazarlar = yazarService.adaGoreYazarlariGetir(yazarAdi);
        yazarListele(yazarlar);
    }

    private void yazarEkle() {
        Yazar yeniYazar = new Yazar();

        System.out.print("Yazar Adı: ");
        String yazarAdi = scanner.nextLine();

        if (yazarAdi.isEmpty()) {
            System.out.println("Yazar adı boş olamaz!");
            return;
        }

        System.out.print("Yazar Soyadı: ");
        String yazarSoyad = scanner.nextLine();

        if (yazarSoyad.isEmpty()) {
            System.out.println("Yazar soyadı boş olamaz!");
            return;
        }

        System.out.print("Açıklama (opsiyonel): ");
        String aciklama = scanner.nextLine();

        yeniYazar.setYazarAdi(yazarAdi);
        yeniYazar.setYazarSoyad(yazarSoyad);
        yeniYazar.setAciklama(aciklama.isEmpty() ? null : aciklama);
        yeniYazar.setAktif(true);

        try {
            Yazar eklenenYazar = yazarService.yazarEkle(yeniYazar);
            System.out.println("Yazar başarıyla eklendi. ID: " + eklenenYazar.getYazarID());
        } catch (Exception e) {
            System.out.println("Yazar eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void yazarGuncelle() {
        System.out.print("Güncellenecek Yazar ID: ");
        String yazarIDStr = scanner.nextLine();

        int yazarID;
        try {
            yazarID = Integer.parseInt(yazarIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
            return;
        }

        var yazarOptional = yazarService.yazarBulById(yazarID);
        if (yazarOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip yazar bulunamadı!");
            return;
        }

        Yazar yazar = yazarOptional.get();

        System.out.println("Mevcut Bilgiler:");
        System.out.println("Ad: " + yazar.getYazarAdi());
        System.out.println("Soyad: " + yazar.getYazarSoyad());
        System.out.println("Açıklama: " + (yazar.getAciklama() != null ? yazar.getAciklama() : "-"));
        System.out.println("Durum: " + (yazar.isAktif() ? "Aktif" : "Pasif"));

        System.out.print("Yeni Ad (değiştirmek istemiyorsanız boş bırakın): ");
        String yazarAdi = scanner.nextLine();
        if (!yazarAdi.isEmpty()) {
            yazar.setYazarAdi(yazarAdi);
        }

        System.out.print("Yeni Soyad (değiştirmek istemiyorsanız boş bırakın): ");
        String yazarSoyad = scanner.nextLine();
        if (!yazarSoyad.isEmpty()) {
            yazar.setYazarSoyad(yazarSoyad);
        }

        System.out.print("Yeni Açıklama (değiştirmek istemiyorsanız boş bırakın): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            yazar.setAciklama(aciklama);
        }

        System.out.print("Durum (1: Aktif, 0: Pasif, değiştirmek istemiyorsanız boş bırakın): ");
        String durum = scanner.nextLine();
        if (!durum.isEmpty()) {
            yazar.setAktif(durum.equals("1"));
        }

        try {
            yazarService.yazarGuncelle(yazarID, yazar);
            System.out.println("Yazar başarıyla güncellendi. ID: " + yazarID);
        } catch (Exception e) {
            System.out.println("Yazar güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void yazarSil() {
        System.out.print("Silinecek (pasif yapılacak) Yazar ID: ");
        String yazarIDStr = scanner.nextLine();

        int yazarID;
        try {
            yazarID = Integer.parseInt(yazarIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
            return;
        }

        try {
            yazarService.yazarSil(yazarID);
            System.out.println("Yazar başarıyla pasif yapıldı. ID: " + yazarID);
        } catch (Exception e) {
            System.out.println("Yazar silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}