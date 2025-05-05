package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.service.BagisService;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class BagisController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final BagisService bagisService;
    private final KitapService kitapService;
    private final UyeService uyeService;

    public BagisController(BagisService bagisService, KitapService kitapService, UyeService uyeService) {
        this.bagisService = bagisService;
        this.kitapService = kitapService;
        this.uyeService = uyeService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Bağış İşlemleri Menüsü ===");
            System.out.println("1. Tüm Bağışları Listele");
            System.out.println("2. Açıklama ile Bağış Ara");
            System.out.println("3. Üye Bazlı Bağışları Listele");
            System.out.println("4. Kitap Bazlı Bağışları Listele");
            System.out.println("5. Yeni Bağış Ekle");
            System.out.println("6. Bağış Güncelle");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> bagisListele(bagisService.aktifBagislariGetir());
                case "2" -> aciklamaIleBagisAra();
                case "3" -> uyeBazliBagislariListele();
                case "4" -> kitapBazliBagislariListele();
                case "5" -> yeniBagisEkle();
                case "6" -> bagisGuncelle();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void bagisListele(List<Bagis> bagisList) {
        if (bagisList.isEmpty()) {
            System.out.println("Listelenecek bağış kaydı bulunamadı.");
            return;
        }

        System.out.println("\n=== Bağış Kayıtları ===");
        System.out.printf("%-5s %-20s %-20s %-5s %-12s %-30s%n",
                "ID", "Üye Adı", "Kitap Adı", "Adet", "Bağış Tarihi", "Açıklama");
        System.out.println("-".repeat(100));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Bagis bagis : bagisList) {
            System.out.printf("%-5d %-20s %-20s %-5d %-12s %-30s%n",
                    bagis.getBagisID(),
                    bagis.getUye().getUyeAdi() + " " + bagis.getUye().getUyeSoyad(),
                    bagis.getKitap().getKitapAdi(),
                    bagis.getAdet(),
                    bagis.getBagisTarih().format(formatter),
                    bagis.getAciklama() != null ? bagis.getAciklama() : "-");
        }
    }

    private void aciklamaIleBagisAra() {
        System.out.print("Aramak istediğiniz açıklama: ");
        String aciklama = scanner.nextLine();

        List<Bagis> bagisList = bagisService.aciklamaIleBagisAra(aciklama);
        bagisListele(bagisList);
    }

    private void uyeBazliBagislariListele() {
        System.out.print("Bağışlarını listelemek istediğiniz üyenin ID'si: ");
        String uyeIDStr = scanner.nextLine();

        try {
            int uyeID = Integer.parseInt(uyeIDStr);
            var uyeOptional = uyeService.uyeBulById(uyeID);

            if (uyeOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip üye bulunamadı!");
                return;
            }

            Uye uye = uyeOptional.get();
            List<Bagis> bagisList = bagisService.aktifBagislariGetir().stream()
                    .filter(b -> b.getUye().getUyeID() == uyeID)
                    .toList();

            bagisListele(bagisList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapBazliBagislariListele() {
        System.out.print("Bağışlarını listelemek istediğiniz kitabın ID'si: ");
        String kitapIDStr = scanner.nextLine();

        try {
            int kitapID = Integer.parseInt(kitapIDStr);
            var kitapOptional = kitapService.kitapBulById(kitapID);

            if (kitapOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip kitap bulunamadı!");
                return;
            }

            Kitap kitap = kitapOptional.get();
            List<Bagis> bagisList = bagisService.aktifBagislariGetir().stream()
                    .filter(b -> b.getKitap().getKitapID() == kitapID)
                    .toList();

            bagisListele(bagisList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void yeniBagisEkle() {
        System.out.println("\n=== Yeni Bağış Ekleme ===");

        // Üye seçimi
        System.out.print("Üye ID: ");
        String uyeIDStr = scanner.nextLine();
        int uyeID;

        try {
            uyeID = Integer.parseInt(uyeIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz üye ID formatı!");
            return;
        }

        var uyeOptional = uyeService.uyeBulById(uyeID);
        if (uyeOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip üye bulunamadı!");
            return;
        }

        Uye uye = uyeOptional.get();
        if (!uye.isAktif() || uye.getDurum() != Uye.UyeDurum.AKTIF) {
            System.out.println("Bu üye aktif değil! Bağış yapamaz.");
            return;
        }

        // Kitap seçimi
        System.out.print("Kitap ID: ");
        String kitapIDStr = scanner.nextLine();
        int kitapID;

        try {
            kitapID = Integer.parseInt(kitapIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz kitap ID formatı!");
            return;
        }

        var kitapOptional = kitapService.kitapBulById(kitapID);
        if (kitapOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip kitap bulunamadı!");
            return;
        }

        Kitap kitap = kitapOptional.get();
        if (!kitap.isAktif() || kitap.getDurum() != Kitap.KitapDurum.AKTIF) {
            System.out.println("Bu kitap aktif değil! Bağış kabul edilemez.");
            return;
        }

        // Adet
        System.out.print("Bağış Adedi: ");
        String adetStr = scanner.nextLine();
        int adet;

        try {
            adet = Integer.parseInt(adetStr);
            if (adet <= 0) {
                System.out.println("Bağış adedi pozitif olmalıdır!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz adet formatı!");
            return;
        }

        // Açıklama
        System.out.print("Açıklama: ");
        String aciklama = scanner.nextLine();

        try {
            bagisService.bagisKaydet(uyeID, kitapID, adet);
            System.out.println("Bağış başarıyla kaydedildi.");
            System.out.println("Üye: " + uye.getUyeAdi() + " " + uye.getUyeSoyad());
            System.out.println("Kitap: " + kitap.getKitapAdi());
            System.out.println("Adet: " + adet);
            System.out.println("Bağış Tarihi: " + LocalDate.now());
        } catch (Exception e) {
            System.out.println("Bağış kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void bagisGuncelle() {
        System.out.println("\n=== Bağış Güncelleme ===");

        System.out.print("Güncellenecek bağış ID'si: ");
        String bagisIDStr = scanner.nextLine();
        int bagisID;

        try {
            bagisID = Integer.parseInt(bagisIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz bağış ID formatı!");
            return;
        }

        var bagisOptional = bagisService.bagisBulById(bagisID);
        if (bagisOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip bağış bulunamadı!");
            return;
        }

        Bagis bagis = bagisOptional.get();
        int eskiAdet = bagis.getAdet();

        // Açıklama
        System.out.print("Yeni Açıklama (değiştirmek istemiyorsanız boş bırakın): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            bagis.setAciklama(aciklama);
        }

        // Durum
        System.out.print("Durum (1: Aktif, 0: Pasif) (değiştirmek istemiyorsanız boş bırakın): ");
        String durumStr = scanner.nextLine();
        if (!durumStr.isEmpty()) {
            try {
                int durum = Integer.parseInt(durumStr);
                bagis.setAktif(durum == 1);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz durum formatı!");
                return;
            }
        }

        try {
            bagisService.bagisGuncelle(bagisID, bagis);
            System.out.println("Bağış başarıyla güncellendi.");
        } catch (Exception e) {
            System.out.println("Bağış güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }
}