package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.OduncService;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class OduncController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final OduncService oduncService;
    private final KitapService kitapService;
    private final UyeService uyeService;

    public OduncController(OduncService oduncService, KitapService kitapService, UyeService uyeService) {
        this.oduncService = oduncService;
        this.kitapService = kitapService;
        this.uyeService = uyeService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Ödünç İşlemleri Menüsü ===");
            System.out.println("1. Tüm Ödünç Kayıtlarını Listele");
            System.out.println("2. Aktif Ödünç Kayıtlarını Listele");
            System.out.println("3. Gecikmiş Ödünç Kayıtlarını Listele");
            System.out.println("4. Teslim Edilmiş Ödünç Kayıtlarını Listele");
            System.out.println("5. Üye Bazlı Ödünç Kayıtlarını Listele");
            System.out.println("6. Kitap Bazlı Ödünç Kayıtlarını Listele");
            System.out.println("7. Ödünç Kitap Ver");
            System.out.println("8. Kitap İade Al");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> oduncListele(oduncService.aktifOdunclariGetir());
                case "2" -> oduncListele(oduncService.durumunaGoreOdunclariGetir(Odunc.OduncDurum.AKTIF));
                case "3" -> oduncListele(oduncService.gecikmisOdunclariGetir(LocalDate.now()));
                case "4" -> oduncListele(oduncService.durumunaGoreOdunclariGetir(Odunc.OduncDurum.TESLIM_EDILDI));
                case "5" -> uyeBazliOduncleriListele();
                case "6" -> kitapBazliOduncleriListele();
                case "7" -> oduncKitapVer();
                case "8" -> kitapIadeAl();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void oduncListele(List<Odunc> oduncList) {
        if (oduncList.isEmpty()) {
            System.out.println("Listelenecek ödünç kaydı bulunamadı.");
            return;
        }

        System.out.println("\n=== Ödünç Kayıtları ===");
        System.out.printf("%-5s %-20s %-20s %-12s %-12s %-12s %-10s%n",
                "ID", "Üye Adı", "Kitap Adı", "Alınma Tarihi", "Son Teslim", "Teslim Tarihi", "Durum");
        System.out.println("-".repeat(100));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Odunc odunc : oduncList) {
            System.out.printf("%-5d %-20s %-20s %-12s %-12s %-12s %-10s%n",
                    odunc.getOduncID(),
                    odunc.getUye().getUyeAdi() + " " + odunc.getUye().getUyeSoyad(),
                    odunc.getKitap().getKitapAdi(),
                    odunc.getOduncAlmaTarih().format(formatter),
                    odunc.getSonTeslimTarihi().format(formatter),
                    odunc.getGercekTeslimTarih() != null ? odunc.getGercekTeslimTarih().format(formatter) : "-",
                    odunc.getDurum());
        }
    }

    private void uyeBazliOduncleriListele() {
        System.out.print("Ödünç kayıtlarını listelemek istediğiniz üyenin ID'si: ");
        String uyeIDStr = scanner.nextLine();

        try {
            int uyeID = Integer.parseInt(uyeIDStr);
            var uyeOptional = uyeService.uyeBulById(uyeID);

            if (uyeOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip üye bulunamadı!");
                return;
            }

            List<Odunc> oduncList = oduncService.getOduncByUyeId(uyeID);
            oduncListele(oduncList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapBazliOduncleriListele() {
        System.out.print("Ödünç kayıtlarını listelemek istediğiniz kitabın ID'si: ");
        String kitapIDStr = scanner.nextLine();

        try {
            int kitapID = Integer.parseInt(kitapIDStr);
            var kitapOptional = kitapService.kitapBulById(kitapID);

            if (kitapOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip kitap bulunamadı!");
                return;
            }

            List<Odunc> oduncList = oduncService.kitapBazliOdunclariGetir(kitapOptional.get());
            oduncListele(oduncList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void oduncKitapVer() {
        System.out.println("\n=== Ödünç Kitap Verme ===");

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
            System.out.println("Bu üye aktif değil! Ödünç kitap verilemez.");
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
            System.out.println("Bu kitap aktif değil! Ödünç verilemez.");
            return;
        }

        if (kitap.getAdet() <= 0) {
            System.out.println("Bu kitabın stokta mevcut adedi yok! Ödünç verilemez.");
            return;
        }

        try {
            oduncService.oduncAl(uyeID, kitapID);
            System.out.println("Kitap başarıyla ödünç verildi.");
            System.out.println("Üye: " + uye.getUyeAdi() + " " + uye.getUyeSoyad());
            System.out.println("Kitap: " + kitap.getKitapAdi());
            System.out.println("Ödünç Alma Tarihi: " + LocalDate.now());
            System.out.println("Son Teslim Tarihi: " + LocalDate.now().plusDays(15));
        } catch (Exception e) {
            System.out.println("Ödünç işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapIadeAl() {
        System.out.println("\n=== Kitap İade Alma ===");

        System.out.print("İade edilecek ödünç kaydının ID'si: ");
        String oduncIDStr = scanner.nextLine();
        int oduncID;

        try {
            oduncID = Integer.parseInt(oduncIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ödünç ID formatı!");
            return;
        }

        var oduncOptional = oduncService.oduncBulById(oduncID);
        if (oduncOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip ödünç kaydı bulunamadı!");
            return;
        }

        Odunc odunc = oduncOptional.get();
        if (odunc.getDurum() != Odunc.OduncDurum.AKTIF) {
            System.out.println("Bu kitap zaten iade edilmiş veya durumu uygun değil!");
            return;
        }

        try {
            oduncService.kitapIadeEt(oduncID);
            System.out.println("Kitap başarıyla iade alındı.");
            System.out.println("Üye: " + odunc.getUye().getUyeAdi() + " " + odunc.getUye().getUyeSoyad());
            System.out.println("Kitap: " + odunc.getKitap().getKitapAdi());
            System.out.println("Ödünç Alma Tarihi: " + odunc.getOduncAlmaTarih());
            System.out.println("Son Teslim Tarihi: " + odunc.getSonTeslimTarihi());
            System.out.println("İade Tarihi: " + LocalDate.now());

            if (LocalDate.now().isAfter(odunc.getSonTeslimTarihi())) {
                System.out.println("DİKKAT: Bu kitap geç teslim edilmiştir! Ceza uygulanabilir.");
            }
        } catch (Exception e) {
            System.out.println("İade işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }
}