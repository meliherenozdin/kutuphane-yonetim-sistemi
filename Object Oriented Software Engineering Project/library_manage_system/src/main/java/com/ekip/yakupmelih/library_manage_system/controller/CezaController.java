package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.service.CezaService;
import com.ekip.yakupmelih.library_manage_system.service.OduncService;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class CezaController implements KullaniciController.Kullanici {

    private final Scanner scanner;
    private final CezaService cezaService;
    private final OduncService oduncService;
    private final UyeService uyeService;

    public CezaController(CezaService cezaService, OduncService oduncService, UyeService uyeService) {
        this.cezaService = cezaService;
        this.oduncService = oduncService;
        this.uyeService = uyeService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Ceza İşlemleri Menüsü ===");
            System.out.println("1. Tüm Cezaları Listele");
            System.out.println("2. Üye Bazlı Cezaları Listele");
            System.out.println("3. Ödünç Bazlı Cezaları Listele");
            System.out.println("4. Tarih Aralığına Göre Cezaları Listele");
            System.out.println("5. Yeni Ceza Ekle");
            System.out.println("6. Ceza Güncelle");
            System.out.println("7. Ceza Sil");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> cezaListele(cezaService.aktifCezalariGetir());
                case "2" -> uyeBazliCezalariListele();
                case "3" -> oduncBazliCezalariListele();
                case "4" -> tarihAraliginaGoreCezalariListele();
                case "5" -> yeniCezaEkle();
                case "6" -> cezaGuncelle();
                case "7" -> cezaSil();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void cezaListele(List<Ceza> cezaList) {
        if (cezaList.isEmpty()) {
            System.out.println("Listelenecek ceza kaydı bulunamadı.");
            return;
        }

        System.out.println("\n=== Ceza Kayıtları ===");
        System.out.printf("%-5s %-20s %-20s %-12s %-12s%n",
                "ID", "Üye Adı", "Ceza Miktarı", "Ceza Tarihi", "Durum");
        System.out.println("-".repeat(80));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Ceza ceza : cezaList) {
            System.out.printf("%-5d %-20s %-20s %-12s %-12s%n",
                    ceza.getCezaID(),
                    ceza.getUye().getUyeAdi() + " " + ceza.getUye().getUyeSoyad(),
                    ceza.getCezaMiktar() + " TL",
                    ceza.getCezaTarih().format(formatter),
                    ceza.isAktif() ? "Aktif" : "Pasif");
        }
    }

    private void uyeBazliCezalariListele() {
        System.out.print("Cezalarını listelemek istediğiniz üyenin ID'si: ");
        String uyeIDStr = scanner.nextLine();

        try {
            int uyeID = Integer.parseInt(uyeIDStr);
            var uyeOptional = uyeService.uyeBulById(uyeID);

            if (uyeOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip üye bulunamadı!");
                return;
            }

            List<Ceza> cezaList = cezaService.getCezaByUyeId(uyeID);
            cezaListele(cezaList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void oduncBazliCezalariListele() {
        System.out.print("Cezalarını listelemek istediğiniz ödünç kaydının ID'si: ");
        String oduncIDStr = scanner.nextLine();

        try {
            int oduncID = Integer.parseInt(oduncIDStr);
            var oduncOptional = oduncService.oduncBulById(oduncID);

            if (oduncOptional.isEmpty()) {
                System.out.println("Belirtilen ID'ye sahip ödünç kaydı bulunamadı!");
                return;
            }

            List<Ceza> cezaList = cezaService.oduncBazliCezalariGetir(oduncOptional.get());
            cezaListele(cezaList);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ID formatı!");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void tarihAraliginaGoreCezalariListele() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("Başlangıç tarihini girin (GG/AA/YYYY): ");
        String startDateStr = scanner.nextLine();

        System.out.println("Bitiş tarihini girin (GG/AA/YYYY): ");
        String endDateStr = scanner.nextLine();

        try {
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            if (startDate.isAfter(endDate)) {
                System.out.println("Başlangıç tarihi bitiş tarihinden sonra olamaz!");
                return;
            }

            List<Ceza> cezaList = cezaService.tarihAraliginaGoreCezalariGetir(startDate, endDate);
            cezaListele(cezaList);
        } catch (DateTimeParseException e) {
            System.out.println("Geçersiz tarih formatı! Lütfen GG/AA/YYYY formatında girin.");
        } catch (Exception e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void yeniCezaEkle() {
        System.out.println("\n=== Yeni Ceza Ekleme ===");

        // Ödünç kaydı seçimi
        System.out.print("Ödünç ID: ");
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
        if (odunc.getDurum() != Odunc.OduncDurum.TESLIM_EDILDI) {
            System.out.println("Bu ödünç kaydı teslim edilmemiş! Ceza eklenemez.");
            return;
        }

        // Ceza miktarı
        System.out.print("Ceza Miktarı (TL): ");
        String cezaMiktarStr = scanner.nextLine();
        BigDecimal cezaMiktar;

        try {
            cezaMiktar = new BigDecimal(cezaMiktarStr);
            if (cezaMiktar.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Ceza miktarı negatif olamaz!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ceza miktarı formatı!");
            return;
        }

        // Açıklama
        System.out.print("Açıklama: ");
        String aciklama = scanner.nextLine();

        try {
            Ceza ceza = new Ceza();
            ceza.setOdunc(odunc);
            ceza.setUye(odunc.getUye());
            ceza.setCezaMiktar(cezaMiktar);
            ceza.setCezaTarih(LocalDate.now());
            ceza.setAciklama(aciklama);
            ceza.setAktif(true);

            cezaService.cezaEkle(ceza);
            System.out.println("Ceza başarıyla eklendi.");
            System.out.println("Üye: " + odunc.getUye().getUyeAdi() + " " + odunc.getUye().getUyeSoyad());
            System.out.println("Kitap: " + odunc.getKitap().getKitapAdi());
            System.out.println("Ceza Miktarı: " + cezaMiktar + " TL");
            System.out.println("Ceza Tarihi: " + LocalDate.now());
        } catch (Exception e) {
            System.out.println("Ceza eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void cezaGuncelle() {
        System.out.println("\n=== Ceza Güncelleme ===");

        System.out.print("Güncellenecek ceza ID'si: ");
        String cezaIDStr = scanner.nextLine();
        int cezaID;

        try {
            cezaID = Integer.parseInt(cezaIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ceza ID formatı!");
            return;
        }

        var cezaOptional = cezaService.cezaBulById(cezaID);
        if (cezaOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip ceza bulunamadı!");
            return;
        }

        Ceza ceza = cezaOptional.get();

        // Ceza miktarı
        System.out.print("Yeni Ceza Miktarı (TL) (değiştirmek istemiyorsanız boş bırakın): ");
        String cezaMiktarStr = scanner.nextLine();
        if (!cezaMiktarStr.isEmpty()) {
            try {
                BigDecimal cezaMiktar = new BigDecimal(cezaMiktarStr);
                if (cezaMiktar.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Ceza miktarı negatif olamaz!");
                    return;
                }
                ceza.setCezaMiktar(cezaMiktar);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz ceza miktarı formatı!");
                return;
            }
        }

        // Açıklama
        System.out.print("Yeni Açıklama (değiştirmek istemiyorsanız boş bırakın): ");
        String aciklama = scanner.nextLine();
        if (!aciklama.isEmpty()) {
            ceza.setAciklama(aciklama);
        }

        // Durum
        System.out.print("Durum (1: Aktif, 0: Pasif) (değiştirmek istemiyorsanız boş bırakın): ");
        String durumStr = scanner.nextLine();
        if (!durumStr.isEmpty()) {
            try {
                int durum = Integer.parseInt(durumStr);
                ceza.setAktif(durum == 1);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz durum formatı!");
                return;
            }
        }

        try {
            cezaService.cezaGuncelle(cezaID, ceza);
            System.out.println("Ceza başarıyla güncellendi.");
        } catch (Exception e) {
            System.out.println("Ceza güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void cezaSil() {
        System.out.println("\n=== Ceza Silme ===");

        System.out.print("Silinecek ceza ID'si: ");
        String cezaIDStr = scanner.nextLine();
        int cezaID;

        try {
            cezaID = Integer.parseInt(cezaIDStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz ceza ID formatı!");
            return;
        }

        var cezaOptional = cezaService.cezaBulById(cezaID);
        if (cezaOptional.isEmpty()) {
            System.out.println("Belirtilen ID'ye sahip ceza bulunamadı!");
            return;
        }

        System.out.print("Bu cezayı silmek istediğinizden emin misiniz? (E/H): ");
        String onay = scanner.nextLine();

        if (onay.equalsIgnoreCase("E")) {
            try {
                cezaService.cezaSil(cezaID);
                System.out.println("Ceza başarıyla silindi.");
            } catch (Exception e) {
                System.out.println("Ceza silinirken bir hata oluştu: " + e.getMessage());
            }
        } else {
            System.out.println("Ceza silme işlemi iptal edildi.");
        }
    }
}