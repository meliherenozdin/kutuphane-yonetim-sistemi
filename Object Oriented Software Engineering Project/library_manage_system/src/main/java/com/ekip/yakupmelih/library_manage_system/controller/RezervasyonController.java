package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Rezervasyon;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.RezervasyonService;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class RezervasyonController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final RezervasyonService rezervasyonService;
    private final KitapService kitapService;
    private final UyeService uyeService;

    public RezervasyonController(RezervasyonService rezervasyonService, KitapService kitapService,
            UyeService uyeService) {
        this.rezervasyonService = rezervasyonService;
        this.kitapService = kitapService;
        this.uyeService = uyeService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Rezervasyon İşlemleri Menüsü ===");
            System.out.println("1. Tüm Rezervasyonları Listele");
            System.out.println("2. Aktif Rezervasyonları Listele");
            System.out.println("3. İptal Edilen Rezervasyonları Listele");
            System.out.println("4. Üye Bazlı Rezervasyonları Listele");
            System.out.println("5. Kitap Bazlı Rezervasyonları Listele");
            System.out.println("6. Yeni Rezervasyon Oluştur");
            System.out.println("7. Rezervasyon İptal Et");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> rezervasyonListele(rezervasyonService.aktifRezervasyonlariGetir());
                case "2" -> rezervasyonListele(
                        rezervasyonService.durumunaGoreRezervasyonlariGetir(Rezervasyon.RezervasyonDurum.AKTIF));
                case "3" -> rezervasyonListele(
                        rezervasyonService.durumunaGoreRezervasyonlariGetir(Rezervasyon.RezervasyonDurum.IPTAL));
                case "4" -> uyeBazliRezervasyonlariListele();
                case "5" -> kitapBazliRezervasyonlariListele();
                case "6" -> yeniRezervasyonOlustur();
                case "7" -> rezervasyonIptalEt();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void rezervasyonListele(List<Rezervasyon> rezervasyonList) {
        if (rezervasyonList.isEmpty()) {
            System.out.println("Listelenecek rezervasyon kaydı bulunamadı.");
            return;
        }

        System.out.println("\n=== Rezervasyon Kayıtları ===");
        System.out.printf("%-5s %-20s %-20s %-12s %-10s%n",
                "ID", "Üye Adı", "Kitap Adı", "Rezervasyon Tarihi", "Durum");
        System.out.println("-".repeat(80));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Rezervasyon rezervasyon : rezervasyonList) {
            System.out.printf("%-5d %-20s %-20s %-12s %-10s%n",
                    rezervasyon.getRezervasyonID(),
                    rezervasyon.getUye().getUyeAdi() + " " + rezervasyon.getUye().getUyeSoyad(),
                    rezervasyon.getKitap().getKitapAdi(),
                    rezervasyon.getRezervasyonTarih().format(formatter),
                    rezervasyon.getDurum());
        }
    }

    private void uyeBazliRezervasyonlariListele() {
        System.out.print("Rezervasyonlarını listelemek istediğiniz üyenin ID'si: ");
        String uyeIDStr = scanner.nextLine();

        try {
            int uyeID = Integer.parseInt(uyeIDStr);
            var uyeOptional = uyeService.uyeBulById(uyeID);

            if (uyeOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip üye bulunamadı!");
                return;
            }

            List<Rezervasyon> rezervasyonList = rezervasyonService.aktifRezervasyonlariGetir().stream()
                    .filter(r -> r.getUye().getUyeID() == uyeID)
                    .toList();

            rezervasyonListele(rezervasyonList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapBazliRezervasyonlariListele() {
        System.out.print("Rezervasyonlarını listelemek istediğiniz kitabın ID'si: ");
        String kitapIDStr = scanner.nextLine();

        try {
            int kitapID = Integer.parseInt(kitapIDStr);
            var kitapOptional = kitapService.kitapBulById(kitapID);

            if (kitapOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip kitap bulunamadı!");
                return;
            }

            List<Rezervasyon> rezervasyonList = rezervasyonService.kitapBazliRezervasyonlariGetir(kitapOptional.get());
            rezervasyonListele(rezervasyonList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void yeniRezervasyonOlustur() {
        System.out.println("\n=== Yeni Rezervasyon Oluşturma ===");

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
            System.out.println("Bu üye aktif değil! Rezervasyon yapılamaz.");
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
            System.out.println("Bu kitap aktif değil! Rezervasyon yapılamaz.");
            return;
        }

        try {
            rezervasyonService.kaydet(uyeID, kitapID);
            System.out.println("Rezervasyon başarıyla oluşturuldu.");
            System.out.println("Üye: " + uye.getUyeAdi() + " " + uye.getUyeSoyad());
            System.out.println("Kitap: " + kitap.getKitapAdi());
            System.out.println("Rezervasyon Tarihi: " + LocalDate.now());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Rezervasyon oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }

    private void rezervasyonIptalEt() {
        System.out.println("\n=== Rezervasyon İptal Etme ===");

        System.out.print("İptal edilecek rezervasyon ID'si: ");
        String rezervasyonIDStr = scanner.nextLine();
        int rezervasyonID;

        try {
            rezervasyonID = Integer.parseInt(rezervasyonIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz rezervasyon ID formatı!");
            return;
        }

        var rezervasyonOptional = rezervasyonService.rezervasyonBulById(rezervasyonID);
        if (rezervasyonOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip rezervasyon bulunamadı!");
            return;
        }

        Rezervasyon rezervasyon = rezervasyonOptional.get();
        if (rezervasyon.getDurum() != Rezervasyon.RezervasyonDurum.AKTIF) {
            System.out.println("Bu rezervasyon zaten iptal edilmiş veya durumu uygun değil!");
            return;
        }

        try {
            rezervasyon.setDurum(Rezervasyon.RezervasyonDurum.IPTAL);
            rezervasyonService.rezervasyonGuncelle(rezervasyonID, rezervasyon);
            System.out.println("Rezervasyon başarıyla iptal edildi.");
            System.out.println("Üye: " + rezervasyon.getUye().getUyeAdi() + " " + rezervasyon.getUye().getUyeSoyad());
            System.out.println("Kitap: " + rezervasyon.getKitap().getKitapAdi());
            System.out.println("Rezervasyon Tarihi: " + rezervasyon.getRezervasyonTarih());
            System.out.println("İptal Tarihi: " + LocalDate.now());
        } catch (Exception e) {
            System.out.println("İptal işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }
}