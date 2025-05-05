package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.model.Yazar;
import com.ekip.yakupmelih.library_manage_system.service.YazarService;

@Component
public class YazarController {

    private final YazarService yazarService;
    private final Scanner scanner;

    public YazarController(YazarService yazarService) {
        this.yazarService = yazarService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Yazar İşlemleri ===");
            System.out.println("1. Tüm Yazarları Listele");
            System.out.println("2. Yazar Ara");
            System.out.println("3. Yeni Yazar Ekle");
            System.out.println("4. Yazar Güncelle");
            System.out.println("5. Yazar Sil");
            System.out.println("6. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumYazarlariListele();
                case "2" -> yazarAra();
                case "3" -> yeniYazarEkle();
                case "4" -> yazarGuncelle();
                case "5" -> yazarSil();
                case "6" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumYazarlariListele() {
        System.out.println("\n=== Tüm Yazarlar ===");
        yazarService.tumYazarlariGetir().forEach(this::yazarYazdir);
    }

    private void yazarAra() {
        System.out.print("Yazar Adı: ");
        String yazarAdi = scanner.nextLine();
        List<Yazar> yazarlar = yazarService.yazarAra(yazarAdi);
        if (yazarlar.isEmpty()) {
            System.out.println("Yazar bulunamadı!");
        } else {
            yazarlar.forEach(this::yazarYazdir);
        }
    }

    private void yeniYazarEkle() {
        Yazar yazar = new Yazar();
        
        System.out.print("Yazar Adı: ");
        yazar.setYazarAdi(scanner.nextLine());
        
        System.out.print("Yazar Soyadı: ");
        yazar.setYazarSoyad(scanner.nextLine());
        
        System.out.print("Açıklama: ");
        yazar.setAciklama(scanner.nextLine());
        
        yazar.setAktif(true);
        
        yazarService.yazarKaydet(yazar);
        System.out.println("Yazar başarıyla eklendi.");
    }

    private void yazarGuncelle() {
        System.out.print("Güncellenecek Yazar ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Yazar yazar = new Yazar();
        
        System.out.print("Yeni Yazar Adı: ");
        yazar.setYazarAdi(scanner.nextLine());
        
        System.out.print("Yeni Yazar Soyadı: ");
        yazar.setYazarSoyad(scanner.nextLine());
        
        System.out.print("Yeni Açıklama: ");
        yazar.setAciklama(scanner.nextLine());
        
        yazar.setAktif(true);
        
        try {
            yazarService.yazarGuncelle(id, yazar);
            System.out.println("Yazar başarıyla güncellendi.");
        } catch (RuntimeException e) {
            System.out.println("Yazar güncellenirken hata oluştu!");
        }
    }

    private void yazarSil() {
        System.out.print("Silinecek Yazar ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            yazarService.yazarSil(id);
            System.out.println("Yazar başarıyla silindi.");
        } catch (RuntimeException e) {
            System.out.println("Yazar silinirken hata oluştu!");
        }
    }

    private void yazarYazdir(Yazar yazar) {
        System.out.printf("ID: %d - Ad: %s %s - Açıklama: %s%n",
            yazar.getYazarID(),
            yazar.getYazarAdi(),
            yazar.getYazarSoyad(),
            yazar.getAciklama());
    }
} 