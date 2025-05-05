package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.service.KategoriService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class KategoriController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final KategoriService kategoriService;

    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Kategori İşlemleri Menüsü ===");
            System.out.println("1. Tüm Kategorileri Listele");
            System.out.println("2. Aktif Kategorileri Listele");
            System.out.println("3. Açıklamaya Göre Kategorileri Ara");
            System.out.println("4. Kategori Ekle");
            System.out.println("5. Kategori Güncelle");
            System.out.println("6. Kategori Sil (Pasif Yap)");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> kategoriListele(kategoriService.tumKategorileriGetir());
                case "2" -> kategoriListele(kategoriService.aktifKategorileriGetir());
                case "3" -> aciklamayaGoreKategoriAra();
                case "4" -> kategoriEkle();
                case "5" -> kategoriGuncelle();
                case "6" -> kategoriSil();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void kategoriListele(List<Kategori> kategoriler) {
        if (kategoriler.isEmpty()) {
            System.out.println("Listelenecek kategori bulunamadı.");
            return;
        }

        System.out.println("\n=== Kategoriler ===");
        System.out.printf("%-5s %-20s %-8s %-40s%n", "ID", "Tür", "Durum", "Açıklama");
        System.out.println("-".repeat(80));

        for (Kategori kategori : kategoriler) {
            System.out.printf("%-5d %-20s %-8s %-40s%n",
                    kategori.getKategoriID(),
                    kategori.getTuru(),
                    kategori.isAktif() ? "Aktif" : "Pasif",
                    kategori.getAciklama() != null ? kategori.getAciklama() : "-");
        }
    }

    private void aciklamayaGoreKategoriAra() {
        System.out.print("Aramak istediğiniz açıklama: ");
        String aciklama = scanner.nextLine();
        List<Kategori> kategoriler = kategoriService.aciklamayaGoreKategorileriGetir(aciklama);
        kategoriListele(kategoriler);
    }

    private void kategoriEkle() {
        Kategori yeniKategori = new Kategori();

        System.out.print("Kategori Türü: ");
        String turu = scanner.nextLine();

        if (turu.isEmpty()) {
            System.out.println("Kategori türü boş olamaz!");
            return;
        }

        System.out.print("Açıklama (opsiyonel): ");
        String aciklama = scanner.nextLine();

        yeniKategori.setTuru(turu);
        yeniKategori.setAciklama(aciklama.isEmpty() ? null : aciklama);
        yeniKategori.setAktif(true);

        try {
            Kategori eklenenKategori = kategoriService.kategoriEkle(yeniKategori);
            System.out.println("Kategori başarıyla eklendi. ID: " + eklenenKategori.getKategoriID());
        } catch (Exception e) {
            System.out.println("Kategori eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void kategoriGuncelle() {
        System.out.print("Güncellenecek Kategori ID: ");
        String kategoriIDStr = scanner.nextLine();

        int kategoriID;
        try {
            kategoriID = Integer.parseInt(kategoriIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
            return;
        }

        var kategoriOptional = kategoriService.kategoriBulById(kategoriID);
        if (kategoriOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip kategori bulunamadı!");
            return;
        }

        Kategori kategori = kategoriOptional.get();

        System.out.println("Mevcut Bilgiler:");
        System.out.println("Tür: " + kategori.getTuru());
        System.out.println("Açıklama: " + (kategori.getAciklama() != null ? kategori.getAciklama() : "-"));
        System.out.println("Durum: " + (kategori.isAktif() ? "Aktif" : "Pasif"));

        System.out.print("Yeni Tür (değiştirmek istemiyorsanız boş bırakın): ");
        String turu = scanner.nextLine();
        if (!turu.isEmpty()) {
            kategori.setTuru(turu);
        }

        System.out.print("Yeni Açıklama (değiştirmek istemiyorsanız boş bırakın): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            kategori.setAciklama(aciklama);
        }

        System.out.print("Durum (1: Aktif, 0: Pasif, değiştirmek istemiyorsanız boş bırakın): ");
        String durum = scanner.nextLine();
        if (!durum.isEmpty()) {
            kategori.setAktif(durum.equals("1"));
        }

        try {
            kategoriService.kategoriGuncelle(kategoriID, kategori);
            System.out.println("Kategori başarıyla güncellendi. ID: " + kategoriID);
        } catch (Exception e) {
            System.out.println("Kategori güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void kategoriSil() {
        System.out.print("Silinecek (pasif yapılacak) Kategori ID: ");
        String kategoriIDStr = scanner.nextLine();

        int kategoriID;
        try {
            kategoriID = Integer.parseInt(kategoriIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
            return;
        }

        try {
            kategoriService.kategoriSil(kategoriID);
            System.out.println("Kategori başarıyla pasif yapıldı. ID: " + kategoriID);
        } catch (Exception e) {
            System.out.println("Kategori silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}