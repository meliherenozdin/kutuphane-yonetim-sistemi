package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Raf;
import com.ekip.yakupmelih.library_manage_system.model.Yazar;
import com.ekip.yakupmelih.library_manage_system.service.KategoriService;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.RafService;
import com.ekip.yakupmelih.library_manage_system.service.YazarService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class KitapController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final KitapService kitapService;
    private final KategoriService kategoriService;
    private final YazarService yazarService;
    private final RafService rafService;

    public KitapController(KitapService kitapService, KategoriService kategoriService,
            YazarService yazarService, RafService rafService) {
        this.kitapService = kitapService;
        this.kategoriService = kategoriService;
        this.yazarService = yazarService;
        this.rafService = rafService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Kitap İşlemleri Menüsü ===");
            System.out.println("1. Tüm Kitapları Listele");
            System.out.println("2. Aktif Kitapları Listele");
            System.out.println("3. Stoktaki Kitapları Listele");
            System.out.println("4. ISBN ile Kitap Ara");
            System.out.println("5. İsim ile Kitap Ara");
            System.out.println("6. Kitap Ekle");
            System.out.println("7. Kitap Güncelle");
            System.out.println("8. Kitap Sil (Pasif Yap)");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> kitapListele(kitapService.tumKitaplariGetir());
                case "2" -> kitapListele(kitapService.aktifKitaplariGetir());
                case "3" -> kitapListele(kitapService.stoktaOlanKitaplariGetir());
                case "4" -> isbnIleKitapAra();
                case "5" -> isimIleKitapAra();
                case "6" -> kitapEkle();
                case "7" -> kitapGuncelle();
                case "8" -> kitapSil();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void kitapListele(List<Kitap> kitaplar) {
        if (kitaplar.isEmpty()) {
            System.out.println("Listelenecek kitap bulunamadı.");
            return;
        }

        System.out.println("\n=== Kitaplar ===");
        System.out.printf("%-5s %-30s %-15s %-10s %-20s %-5s %-10s%n",
                "ID", "Kitap Adı", "ISBN", "Basım Yılı", "Yayınevi", "Adet", "Durum");
        System.out.println("-".repeat(100));

        for (Kitap kitap : kitaplar) {
            System.out.printf("%-5d %-30s %-15s %-10s %-20s %-5d %-10s%n",
                    kitap.getKitapID(),
                    kitap.getKitapAdi(),
                    kitap.getIsbn(),
                    kitap.getBasimYili(),
                    kitap.getYayinevi() != null ? kitap.getYayinevi() : "-",
                    kitap.getAdet(),
                    kitap.getDurum());
        }
    }

    private void isbnIleKitapAra() {
        System.out.print("Aramak istediğiniz ISBN: ");
        String isbn = scanner.nextLine();
        kitapService.kitapBulByIsbn(isbn).ifPresentOrElse(
                kitap -> {
                    System.out.println("\nBulunan Kitap:");
                    System.out.printf("ID: %d%n", kitap.getKitapID());
                    System.out.printf("Kitap Adı: %s%n", kitap.getKitapAdi());
                    System.out.printf("ISBN: %s%n", kitap.getIsbn());
                    System.out.printf("Basım Yılı: %s%n", kitap.getBasimYili());
                    System.out.printf("Yayınevi: %s%n", kitap.getYayinevi() != null ? kitap.getYayinevi() : "-");
                    System.out.printf("Adet: %d%n", kitap.getAdet());
                    System.out.printf("Kategori: %s%n",
                            kitap.getKategori() != null ? kitap.getKategori().getTuru() : "-");
                    System.out.printf("Yazar: %s %s%n",
                            kitap.getYazar() != null ? kitap.getYazar().getYazarAdi() : "-",
                            kitap.getYazar() != null ? kitap.getYazar().getYazarSoyad() : "");
                    System.out.printf("Raf No: %s%n", kitap.getRaf() != null ? kitap.getRaf().getRafNo() : "-");
                    System.out.printf("Durum: %s%n", kitap.getDurum());
                    System.out.printf("Açıklama: %s%n", kitap.getAciklama() != null ? kitap.getAciklama() : "-");
                },
                () -> System.out.println("Bu ISBN'e sahip kitap bulunamadı."));
    }

    private void isimIleKitapAra() {
        System.out.print("Aramak istediğiniz kitap adı: ");
        String kitapAdi = scanner.nextLine();
        kitapService.kitapBulByAd(kitapAdi).ifPresentOrElse(
                kitap -> {
                    System.out.println("\nBulunan Kitap:");
                    System.out.printf("ID: %d%n", kitap.getKitapID());
                    System.out.printf("Kitap Adı: %s%n", kitap.getKitapAdi());
                    System.out.printf("ISBN: %s%n", kitap.getIsbn());
                    System.out.printf("Basım Yılı: %s%n", kitap.getBasimYili());
                    System.out.printf("Yayınevi: %s%n", kitap.getYayinevi() != null ? kitap.getYayinevi() : "-");
                    System.out.printf("Adet: %d%n", kitap.getAdet());
                    System.out.printf("Kategori: %s%n",
                            kitap.getKategori() != null ? kitap.getKategori().getTuru() : "-");
                    System.out.printf("Yazar: %s %s%n",
                            kitap.getYazar() != null ? kitap.getYazar().getYazarAdi() : "-",
                            kitap.getYazar() != null ? kitap.getYazar().getYazarSoyad() : "");
                    System.out.printf("Raf No: %s%n", kitap.getRaf() != null ? kitap.getRaf().getRafNo() : "-");
                    System.out.printf("Durum: %s%n", kitap.getDurum());
                    System.out.printf("Açıklama: %s%n", kitap.getAciklama() != null ? kitap.getAciklama() : "-");
                },
                () -> System.out.println("Bu isme sahip kitap bulunamadı."));
    }

    private void kitapEkle() {
        Kitap yeniKitap = new Kitap();

        System.out.print("Kitap Adı: ");
        String kitapAdi = scanner.nextLine();
        if (kitapAdi.isEmpty()) {
            System.out.println("Kitap adı boş olamaz!");
            return;
        }
        yeniKitap.setKitapAdi(kitapAdi);

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        if (isbn.isEmpty()) {
            System.out.println("ISBN boş olamaz!");
            return;
        }

        // ISBN kontrol
        if (kitapService.kitapBulByIsbn(isbn).isPresent()) {
            System.out.println("Bu ISBN zaten kullanılıyor!");
            return;
        }
        yeniKitap.setIsbn(isbn);

        System.out.print("Basım Yılı (YYYY-MM-DD): ");
        String basimYiliStr = scanner.nextLine();
        try {
            LocalDate basimYili = LocalDate.parse(basimYiliStr, DateTimeFormatter.ISO_DATE);
            yeniKitap.setBasimYili(basimYili);
        } catch (DateTimeParseException e) {
            System.out.println("Geçersiz tarih formatı! Lütfen YYYY-MM-DD formatında girin.");
            return;
        }

        System.out.print("Yayınevi: ");
        String yayinevi = scanner.nextLine();
        yeniKitap.setYayinevi(yayinevi);

        System.out.print("Adet: ");
        String adetStr = scanner.nextLine();
        try {
            int adet = Integer.parseInt(adetStr);
            if (adet < 0) {
                System.out.println("Adet negatif olamaz!");
                return;
            }
            yeniKitap.setAdet(adet);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz adet formatı!");
            return;
        }

        // Kategori seçimi
        System.out.println("\nMevcut Kategoriler:");
        List<Kategori> kategoriler = kategoriService.aktifKategorileriGetir();
        if (kategoriler.isEmpty()) {
            System.out.println("Aktif kategori bulunamadı! Önce kategori ekleyin.");
            return;
        }

        for (Kategori kategori : kategoriler) {
            System.out.printf("%d - %s%n", kategori.getKategoriID(), kategori.getTuru());
        }

        System.out.print("Kategori ID (boş bırakılabilir): ");
        String kategoriIDStr = scanner.nextLine();
        if (!kategoriIDStr.isEmpty()) {
            try {
                int kategoriID = Integer.parseInt(kategoriIDStr);
                kategoriService.kategoriBulById(kategoriID).ifPresent(yeniKitap::setKategori);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz kategori ID formatı!");
            }
        }

        // Yazar seçimi
        System.out.println("\nMevcut Yazarlar:");
        List<Yazar> yazarlar = yazarService.aktifYazarlariGetir();
        if (yazarlar.isEmpty()) {
            System.out.println("Aktif yazar bulunamadı! Önce yazar ekleyin.");
            return;
        }

        for (Yazar yazar : yazarlar) {
            System.out.printf("%d - %s %s%n", yazar.getYazarID(), yazar.getYazarAdi(), yazar.getYazarSoyad());
        }

        System.out.print("Yazar ID (boş bırakılabilir): ");
        String yazarIDStr = scanner.nextLine();
        if (!yazarIDStr.isEmpty()) {
            try {
                int yazarID = Integer.parseInt(yazarIDStr);
                yazarService.yazarBulById(yazarID).ifPresent(yeniKitap::setYazar);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz yazar ID formatı!");
            }
        }

        // Raf seçimi
        System.out.println("\nMevcut Raflar:");
        List<Raf> raflar = rafService.aktifRaflariGetir();
        if (!raflar.isEmpty()) {
            for (Raf raf : raflar) {
                System.out.printf("%s - %s%n", raf.getRafNo(), raf.getBolum());
            }

            System.out.print("Raf No (boş bırakılabilir): ");
            String rafNo = scanner.nextLine();
            if (!rafNo.isEmpty()) {
                rafService.rafBulByRafNo(rafNo).ifPresent(yeniKitap::setRaf);
            }
        } else {
            System.out.println("Aktif raf bulunamadı.");
        }

        System.out.print("Açıklama (opsiyonel): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            yeniKitap.setAciklama(aciklama);
        }

        // Kitap durumu
        yeniKitap.setDurum(Kitap.KitapDurum.AKTIF);
        yeniKitap.setAktif(true);

        try {
            Kitap eklenenKitap = kitapService.kitapEkle(yeniKitap);
            System.out.println("Kitap başarıyla eklendi. ID: " + eklenenKitap.getKitapID());
        } catch (Exception e) {
            System.out.println("Kitap eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapGuncelle() {
        System.out.print("Güncellenecek Kitap ID: ");
        String kitapIDStr = scanner.nextLine();

        int kitapID;
        try {
            kitapID = Integer.parseInt(kitapIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
            return;
        }

        var kitapOptional = kitapService.kitapBulById(kitapID);
        if (kitapOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip kitap bulunamadı!");
            return;
        }

        Kitap kitap = kitapOptional.get();

        System.out.println("Mevcut Bilgiler:");
        System.out.println("Kitap Adı: " + kitap.getKitapAdi());
        System.out.println("ISBN: " + kitap.getIsbn());
        System.out.println("Basım Yılı: " + kitap.getBasimYili());
        System.out.println("Yayınevi: " + (kitap.getYayinevi() != null ? kitap.getYayinevi() : "-"));
        System.out.println("Adet: " + kitap.getAdet());
        System.out.println("Kategori: " + (kitap.getKategori() != null ? kitap.getKategori().getTuru() : "-"));
        System.out.println("Yazar: " +
                (kitap.getYazar() != null ? kitap.getYazar().getYazarAdi() + " " + kitap.getYazar().getYazarSoyad()
                        : "-"));
        System.out.println("Raf No: " + (kitap.getRaf() != null ? kitap.getRaf().getRafNo() : "-"));
        System.out.println("Durum: " + kitap.getDurum());
        System.out.println("Açıklama: " + (kitap.getAciklama() != null ? kitap.getAciklama() : "-"));

        System.out.print("Yeni Kitap Adı (değiştirmek istemiyorsanız boş bırakın): ");
        String kitapAdi = scanner.nextLine();
        if (!kitapAdi.isEmpty()) {
            kitap.setKitapAdi(kitapAdi);
        }

        System.out.print("Yeni ISBN (değiştirmek istemiyorsanız boş bırakın): ");
        String isbn = scanner.nextLine();
        if (!isbn.isEmpty() && !isbn.equals(kitap.getIsbn())) {
            // ISBN kontrol
            var existingKitap = kitapService.kitapBulByIsbn(isbn);
            if (existingKitap.isPresent() && existingKitap.get().getKitapID() != kitap.getKitapID()) {
                System.out.println("Bu ISBN zaten başka bir kitap tarafından kullanılıyor!");
            } else {
                kitap.setIsbn(isbn);
            }
        }

        System.out.print("Yeni Basım Yılı (YYYY-MM-DD, değiştirmek istemiyorsanız boş bırakın): ");
        String basimYiliStr = scanner.nextLine();
        if (!basimYiliStr.isEmpty()) {
            try {
                LocalDate basimYili = LocalDate.parse(basimYiliStr, DateTimeFormatter.ISO_DATE);
                kitap.setBasimYili(basimYili);
            } catch (DateTimeParseException e) {
                System.out.println("Geçersiz tarih formatı! Bu alan güncellenmedi.");
            }
        }

        System.out.print("Yeni Yayınevi (değiştirmek istemiyorsanız boş bırakın): ");
        String yayinevi = scanner.nextLine();
        if (!yayinevi.isEmpty()) {
            kitap.setYayinevi(yayinevi);
        }

        System.out.print("Yeni Adet (değiştirmek istemiyorsanız boş bırakın): ");
        String adetStr = scanner.nextLine();
        if (!adetStr.isEmpty()) {
            try {
                int adet = Integer.parseInt(adetStr);
                if (adet < 0) {
                    System.out.println("Adet negatif olamaz! Bu alan güncellenmedi.");
                } else {
                    kitap.setAdet(adet);
                }
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz adet formatı! Bu alan güncellenmedi.");
            }
        }

        // Kategori güncelleme
        System.out.print("Yeni Kategori ID (değiştirmek istemiyorsanız boş bırakın): ");
        String kategoriIDStr = scanner.nextLine();
        if (!kategoriIDStr.isEmpty()) {
            try {
                int kategoriID = Integer.parseInt(kategoriIDStr);
                var kategoriOptional = kategoriService.kategoriBulById(kategoriID);
                if (kategoriOptional.isPresent()) {
                    kitap.setKategori(kategoriOptional.get());
                } else {
                    System.out.println("Belirtilen ID'ye sahip kategori bulunamadı! Bu alan güncellenmedi.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz kategori ID formatı! Bu alan güncellenmedi.");
            }
        }

        // Yazar güncelleme
        System.out.print("Yeni Yazar ID (değiştirmek istemiyorsanız boş bırakın): ");
        String yazarIDStr = scanner.nextLine();
        if (!yazarIDStr.isEmpty()) {
            try {
                int yazarID = Integer.parseInt(yazarIDStr);
                var yazarOptional = yazarService.yazarBulById(yazarID);
                if (yazarOptional.isPresent()) {
                    kitap.setYazar(yazarOptional.get());
                } else {
                    System.out.println("Belirtilen ID'ye sahip yazar bulunamadı! Bu alan güncellenmedi.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz yazar ID formatı! Bu alan güncellenmedi.");
            }
        }

        // Raf güncelleme
        System.out.print("Yeni Raf No (değiştirmek istemiyorsanız boş bırakın): ");
        String rafNo = scanner.nextLine();
        if (!rafNo.isEmpty()) {
            var rafOptional = rafService.rafBulByRafNo(rafNo);
            if (rafOptional.isPresent()) {
                kitap.setRaf(rafOptional.get());
            } else {
                System.out.println("Belirtilen raf numarası bulunamadı! Bu alan güncellenmedi.");
            }
        }

        // Durum güncelleme
        System.out.println(
                "Kitap Durumu (1: AKTIF, 2: PASIF, 3: KAYIP, 4: HASARLI, değiştirmek istemiyorsanız boş bırakın): ");
        String durumStr = scanner.nextLine();
        if (!durumStr.isEmpty()) {
            try {
                int durumIndex = Integer.parseInt(durumStr);
                switch (durumIndex) {
                    case 1 -> kitap.setDurum(Kitap.KitapDurum.AKTIF);
                    case 2 -> kitap.setDurum(Kitap.KitapDurum.PASIF);
                    case 3 -> kitap.setDurum(Kitap.KitapDurum.KAYIP);
                    case 4 -> kitap.setDurum(Kitap.KitapDurum.HASARLI);
                    default -> System.out.println("Geçersiz durum seçimi! Bu alan güncellenmedi.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz durum formatı! Bu alan güncellenmedi.");
            }
        }

        System.out.print("Yeni Açıklama (değiştirmek istemiyorsanız boş bırakın): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            kitap.setAciklama(aciklama);
        }

        System.out.print("Aktiflik Durumu (1: Aktif, 0: Pasif, değiştirmek istemiyorsanız boş bırakın): ");
        String aktifStr = scanner.nextLine();
        if (!aktifStr.isEmpty()) {
            kitap.setAktif(aktifStr.equals("1"));
        }

        try {
            kitapService.kitapGuncelle(kitapID, kitap);
            System.out.println("Kitap başarıyla güncellendi. ID: " + kitapID);
        } catch (Exception e) {
            System.out.println("Kitap güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapSil() {
        System.out.print("Silinecek (pasif yapılacak) Kitap ID: ");
        String kitapIDStr = scanner.nextLine();

        int kitapID;
        try {
            kitapID = Integer.parseInt(kitapIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
            return;
        }

        try {
            kitapService.kitapSil(kitapID);
            System.out.println("Kitap başarıyla pasif yapıldı. ID: " + kitapID);
        } catch (Exception e) {
            System.out.println("Kitap silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}