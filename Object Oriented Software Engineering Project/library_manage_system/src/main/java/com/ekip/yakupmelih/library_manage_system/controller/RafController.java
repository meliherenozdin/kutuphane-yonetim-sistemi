package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Raf;
import com.ekip.yakupmelih.library_manage_system.service.RafService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class RafController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final RafService rafService;

    public RafController(RafService rafService) {
        this.rafService = rafService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Raf İşlemleri Menüsü ===");
            System.out.println("1. Tüm Rafları Listele");
            System.out.println("2. Aktif Rafları Listele");
            System.out.println("3. Bölüme Göre Rafları Ara");
            System.out.println("4. Raf Ekle");
            System.out.println("5. Raf Güncelle");
            System.out.println("6. Raf Sil (Pasif Yap)");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> rafListele(rafService.tumRaflariGetir());
                case "2" -> rafListele(rafService.aktifRaflariGetir());
                case "3" -> bolumeGoreRafAra();
                case "4" -> rafEkle();
                case "5" -> rafGuncelle();
                case "6" -> rafSil();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void rafListele(List<Raf> raflar) {
        if (raflar.isEmpty()) {
            System.out.println("Listelenecek raf bulunamadı.");
            return;
        }

        System.out.println("\n=== Raflar ===");
        System.out.printf("%-10s %-20s %-8s %-40s%n", "Raf No", "Bölüm", "Durum", "Açıklama");
        System.out.println("-".repeat(80));

        for (Raf raf : raflar) {
            System.out.printf("%-10s %-20s %-8s %-40s%n",
                    raf.getRafNo(),
                    raf.getBolum(),
                    raf.isAktif() ? "Aktif" : "Pasif",
                    raf.getAciklama() != null ? raf.getAciklama() : "-");
        }
    }

    private void bolumeGoreRafAra() {
        System.out.print("Aramak istediğiniz bölüm adı: ");
        String bolum = scanner.nextLine();
        List<Raf> raflar = rafService.bolumeGoreRaflariGetir(bolum);
        rafListele(raflar);
    }

    private void rafEkle() {
        Raf yeniRaf = new Raf();

        System.out.print("Raf No: ");
        String rafNo = scanner.nextLine();

        // Raf no kontrolü
        if (rafService.rafBulByRafNo(rafNo).isPresent()) {
            System.out.println("Bu raf numarası zaten kullanılıyor!");
            return;
        }

        System.out.print("Bölüm: ");
        String bolum = scanner.nextLine();

        System.out.print("Açıklama (opsiyonel): ");
        String aciklama = scanner.nextLine();

        yeniRaf.setRafNo(rafNo);
        yeniRaf.setBolum(bolum);
        yeniRaf.setAciklama(aciklama.isEmpty() ? null : aciklama);
        yeniRaf.setAktif(true);

        try {
            rafService.rafEkle(yeniRaf);
            System.out.println("Raf başarıyla eklendi: " + rafNo);
        } catch (Exception e) {
            System.out.println("Raf eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void rafGuncelle() {
        System.out.print("Güncellenecek Raf No: ");
        String rafNo = scanner.nextLine();

        var rafOptional = rafService.rafBulByRafNo(rafNo);
        if (rafOptional.isEmpty()) {
            System.out.println("Belirtilen raf numarası bulunamadı!");
            return;
        }

        Raf raf = rafOptional.get();

        System.out.println("Mevcut Bilgiler:");
        System.out.println("Bölüm: " + raf.getBolum());
        System.out.println("Açıklama: " + (raf.getAciklama() != null ? raf.getAciklama() : "-"));
        System.out.println("Durum: " + (raf.isAktif() ? "Aktif" : "Pasif"));

        System.out.print("Yeni Bölüm (değiştirmek istemiyorsanız boş bırakın): ");
        String bolum = scanner.nextLine();
        if (!bolum.isEmpty()) {
            raf.setBolum(bolum);
        }

        System.out.print("Yeni Açıklama (değiştirmek istemiyorsanız boş bırakın): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            raf.setAciklama(aciklama);
        }

        System.out.print("Durum (1: Aktif, 0: Pasif, değiştirmek istemiyorsanız boş bırakın): ");
        String durum = scanner.nextLine();
        if (!durum.isEmpty()) {
            raf.setAktif(durum.equals("1"));
        }

        try {
            rafService.rafGuncelle(rafNo, raf);
            System.out.println("Raf başarıyla güncellendi: " + rafNo);
        } catch (Exception e) {
            System.out.println("Raf güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void rafSil() {
        System.out.print("Silinecek (pasif yapılacak) Raf No: ");
        String rafNo = scanner.nextLine();

        try {
            rafService.rafSil(rafNo);
            System.out.println("Raf başarıyla pasif yapıldı: " + rafNo);
        } catch (Exception e) {
            System.out.println("Raf silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}