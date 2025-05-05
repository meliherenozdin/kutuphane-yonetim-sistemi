package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import com.ekip.yakupmelih.library_manage_system.service.BagisService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class BagisController {

    private final BagisService bagisService;
    private final Scanner scanner = new Scanner(System.in);

    public BagisController(BagisService bagisService) {
        this.bagisService = bagisService;
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== Bağış İşlemleri ===");
            System.out.println("1. Tüm Bağışları Listele");
            System.out.println("2. Bağış Detayı Göster");
            System.out.println("3. Bağış Ekle");
            System.out.println("4. Bağış Güncelle");
            System.out.println("5. Bağış Sil");
            System.out.println("6. Bağışçıya Göre Bağışları Listele");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> tumBagislariGetir();
                case "2" -> bagisDetayGoster();
                case "3" -> bagisEkle();
                case "4" -> bagisGuncelle();
                case "5" -> bagisSil();
                case "6" -> bagisciBagislariGetir();
                case "0" -> { return; }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void tumBagislariGetir() {
        List<Bagis> bagislar = bagisService.tumBagislariGetir();
        bagislar.forEach(System.out::println);
    }

    private void bagisDetayGoster() {
        System.out.print("Bağış ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Bagis> bagis = bagisService.bagisGetir(id);
        bagis.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Bağış bulunamadı.")
        );
    }

    private void bagisEkle() {
        Bagis bagis = new Bagis();
        System.out.print("Bağışçı (Üye) ID: ");
        // bagis.setUye(...); // Uye nesnesini uygun şekilde set etmelisin
        System.out.print("Kitap ID: ");
        // bagis.setKitap(...); // Kitap nesnesini uygun şekilde set etmelisin
        System.out.print("Adet: ");
        bagis.setAdet(Integer.parseInt(scanner.nextLine()));
        System.out.print("Açıklama: ");
        bagis.setAciklama(scanner.nextLine());
        // Diğer gerekli alanları da doldurabilirsin

        bagisService.bagisKaydet(bagis);
        System.out.println("Bağış başarıyla eklendi.");
    }

    private void bagisGuncelle() {
        System.out.print("Güncellenecek Bağış ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Bagis> mevcut = bagisService.bagisGetir(id);
        if (mevcut.isEmpty()) {
            System.out.println("Bağış bulunamadı.");
            return;
        }
        Bagis bagis = mevcut.get();
        System.out.print("Yeni Adet (" + bagis.getAdet() + "): ");
        bagis.setAdet(Integer.parseInt(scanner.nextLine()));
        System.out.print("Yeni Açıklama (" + bagis.getAciklama() + "): ");
        bagis.setAciklama(scanner.nextLine());
        // Diğer alanlar da eklenebilir

        bagisService.bagisGuncelle(id, bagis);
        System.out.println("Bağış güncellendi.");
    }

    private void bagisSil() {
        System.out.print("Silinecek Bağış ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            bagisService.bagisSil(id);
            System.out.println("Bağış silindi.");
        } catch (Exception e) {
            System.out.println("Bağış silinemedi.");
        }
    }

    private void bagisciBagislariGetir() {
        System.out.print("Bağışçı (Üye) ID: ");
        int bagisciId = Integer.parseInt(scanner.nextLine());
        List<Bagis> bagislar = bagisService.bagisciBagislariGetir(bagisciId);
        bagislar.forEach(System.out::println);
    }
} 