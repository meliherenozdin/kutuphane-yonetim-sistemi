package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import com.ekip.yakupmelih.library_manage_system.service.BagisService;
import com.ekip.yakupmelih.library_manage_system.service.CezaService;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.OduncService;
import com.ekip.yakupmelih.library_manage_system.service.RezervasyonService;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.CezaRepository;
import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.model.Rezervasyon;
import com.ekip.yakupmelih.library_manage_system.service.UyeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UyeController implements KullaniciController.Kullanici {

    private Uye uye;
    private final Scanner scanner;
    private final KitapService kitapService;
    private final UyeService uyeService;
    private final OduncService oduncService;
    private final RezervasyonService rezervasyonService;
    private final BagisService bagisService;
    private final CezaService cezaService;

    @Autowired
    public UyeController(KitapService kitapService, UyeService uyeService, OduncService oduncService,
            RezervasyonService rezervasyonService, BagisService bagisService, CezaService cezaService) {
        this.kitapService = kitapService;
        this.uyeService = uyeService;
        this.oduncService = oduncService;
        this.rezervasyonService = rezervasyonService;
        this.bagisService = bagisService;
        this.cezaService = cezaService;
        this.scanner = new Scanner(System.in);
    }

    public void setUye(Uye uye) {
        this.uye = uye;
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Üye Menüsü ===");
            System.out.println("Hoş geldiniz, " + uye.getUyeAdi() + " " + uye.getUyeSoyad());
            System.out.println("1. Tüm Kitapları Listele");
            System.out.println("2. Aktif Kitapları Listele");
            System.out.println("3. Stoktaki Kitapları Listele");
            System.out.println("4. ISBN ile Kitap Ara");
            System.out.println("5. İsim ile Kitap Ara");
            System.out.println("6. Ödünç Aldığım Kitapları Listele");
            System.out.println("7. Rezervasyonlarımı Listele");
            System.out.println("8. Cezalarımı Listele");
            System.out.println("9. Bağışlarımı Listele");
            System.out.println("10. Kitap Ödünç Al");
            System.out.println("11. Kitap Rezervasyonu Yap");
            System.out.println("12. Kitap Bağışı Yap");
            System.out.println("13. Profil Bilgilerimi Görüntüle");
            System.out.println("14. Profil Bilgilerimi Güncelle");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> kitapListele(kitapService.tumKitaplariGetir());
                case "2" -> kitapListele(kitapService.aktifKitaplariGetir());
                case "3" -> kitapListele(kitapService.stoktaOlanKitaplariGetir());
                case "4" -> isbnIleKitapAra();
                case "5" -> isimIleKitapAra();
                case "6" -> oduncKitaplarim();
                case "7" -> rezervasyonlarim();
                case "8" -> cezalarim();
                case "9" -> bagislarim();
                case "10" -> kitapOduncAl();
                case "11" -> kitapRezervasyonYap();
                case "12" -> kitapBagisYap();
                case "13" -> profilBilgileriniGoruntule();
                case "14" -> profilBilgileriniGuncelle();
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

    private void oduncKitaplarim() {
        List<Odunc> oduncListem = oduncService.getOduncByUyeId(uye.getUyeID());

        if (oduncListem.isEmpty()) {
            System.out.println("Ödünç aldığınız kitap bulunamadı.");
            return;
        }

        System.out.println("\n=== Ödünç Aldığım Kitaplar ===");
        System.out.printf("%-5s %-30s %-15s %-12s %-12s %-10s%n",
                "ID", "Kitap Adı", "ISBN", "Alınma Tarihi", "Son Teslim", "Durum");
        System.out.println("-".repeat(90));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Odunc odunc : oduncListem) {
            System.out.printf("%-5d %-30s %-15s %-12s %-12s %-10s%n",
                    odunc.getOduncID(),
                    odunc.getKitap().getKitapAdi(),
                    odunc.getKitap().getIsbn(),
                    odunc.getOduncAlmaTarih().format(formatter),
                    odunc.getSonTeslimTarihi().format(formatter),
                    odunc.getDurum());
        }
    }

    private void rezervasyonlarim() {
        List<Rezervasyon> rezervasyonListem = rezervasyonService.aktifRezervasyonlariGetir().stream()
                .filter(r -> r.getUye().getUyeID() == uye.getUyeID())
                .toList();

        if (rezervasyonListem.isEmpty()) {
            System.out.println("Aktif rezervasyonunuz bulunamadı.");
            return;
        }

        System.out.println("\n=== Rezervasyonlarım ===");
        System.out.printf("%-5s %-30s %-15s %-12s %-10s%n",
                "ID", "Kitap Adı", "ISBN", "Rezervasyon Tarihi", "Durum");
        System.out.println("-".repeat(80));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Rezervasyon rezervasyon : rezervasyonListem) {
            System.out.printf("%-5d %-30s %-15s %-12s %-10s%n",
                    rezervasyon.getRezervasyonID(),
                    rezervasyon.getKitap().getKitapAdi(),
                    rezervasyon.getKitap().getIsbn(),
                    rezervasyon.getRezervasyonTarih().format(formatter),
                    rezervasyon.getDurum());
        }
    }

    private void cezalarim() {
        List<Ceza> cezaListem = cezaService.getCezaByUyeId(uye.getUyeID());

        if (cezaListem.isEmpty()) {
            System.out.println("Ceza kaydınız bulunamadı.");
            return;
        }

        System.out.println("\n=== Cezalarım ===");
        System.out.printf("%-5s %-30s %-12s %-12s %-10s%n",
                "ID", "Kitap Adı", "Ceza Tarihi", "Ceza Miktarı", "Durum");
        System.out.println("-".repeat(80));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Ceza ceza : cezaListem) {
            System.out.printf("%-5d %-30s %-12s %-12s %-10s%n",
                    ceza.getCezaID(),
                    ceza.getOdunc().getKitap().getKitapAdi(),
                    ceza.getCezaTarih().format(formatter),
                    ceza.getCezaMiktar() + " TL",
                    ceza.isAktif() ? "Aktif" : "Pasif");
        }
    }

    private void bagislarim() {
        List<Bagis> bagisListem = bagisService.aktifBagislariGetir().stream()
                .filter(b -> b.getUye().getUyeID() == uye.getUyeID())
                .toList();

        if (bagisListem.isEmpty()) {
            System.out.println("Bağış kaydınız bulunamadı.");
            return;
        }

        System.out.println("\n=== Bağışlarım ===");
        System.out.printf("%-5s %-30s %-15s %-5s %-12s%n",
                "ID", "Kitap Adı", "ISBN", "Adet", "Bağış Tarihi");
        System.out.println("-".repeat(80));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Bagis bagis : bagisListem) {
            System.out.printf("%-5d %-30s %-15s %-5d %-12s%n",
                    bagis.getBagisID(),
                    bagis.getKitap().getKitapAdi(),
                    bagis.getKitap().getIsbn(),
                    bagis.getAdet(),
                    bagis.getBagisTarih().format(formatter));
        }
    }

    private void kitapOduncAl() {
        System.out.println("\n=== Kitap Ödünç Alma ===");

        // Üyenin aktif cezası var mı kontrol et
        List<Ceza> aktifCezalar = cezaService.getCezaByUyeId(uye.getUyeID()).stream()
                .filter(Ceza::isAktif)
                .toList();

        if (!aktifCezalar.isEmpty()) {
            System.out.println("Aktif ceza kaydınız bulunduğundan kitap ödünç alamazsınız!");
            return;
        }

        // Kitap seçimi
        System.out.print("Ödünç almak istediğiniz kitabın ID'si: ");
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
            System.out.println("Bu kitap aktif değil! Ödünç alamazsınız.");
            return;
        }

        if (kitap.getAdet() <= 0) {
            System.out.println("Bu kitabın stokta mevcut adedi yok! Ödünç alamazsınız.");
            return;
        }

        try {
            oduncService.oduncAl(uye.getUyeID(), kitapID);
            System.out.println("Kitap başarıyla ödünç alındı.");
            System.out.println("Kitap: " + kitap.getKitapAdi());
            System.out.println("Ödünç Alma Tarihi: " + LocalDate.now());
            System.out.println("Son Teslim Tarihi: " + LocalDate.now().plusDays(15));
        } catch (Exception e) {
            System.out.println("Ödünç işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapRezervasyonYap() {
        System.out.println("\n=== Kitap Rezervasyonu Yapma ===");

        // Kitap seçimi
        System.out.print("Rezervasyon yapmak istediğiniz kitabın ID'si: ");
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
            System.out.println("Bu kitap aktif değil! Rezervasyon yapamazsınız.");
            return;
        }

        // Bu kitap için zaten rezervasyon var mı?
        boolean zatenRezerveEtmis = rezervasyonService.aktifRezervasyonlariGetir().stream()
                .anyMatch(r -> r.getUye().getUyeID() == uye.getUyeID() &&
                        r.getKitap().getKitapID() == kitapID &&
                        r.getDurum() == Rezervasyon.RezervasyonDurum.AKTIF);

        if (zatenRezerveEtmis) {
            System.out.println("Bu kitap için zaten aktif bir rezervasyonunuz bulunmaktadır!");
            return;
        }

        try {
            rezervasyonService.kaydet(uye.getUyeID(), kitapID);
            System.out.println("Kitap başarıyla rezerve edildi.");
            System.out.println("Kitap: " + kitap.getKitapAdi());
            System.out.println("Rezervasyon Tarihi: " + LocalDate.now());
        } catch (Exception e) {
            System.out.println("Rezervasyon işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void kitapBagisYap() {
        System.out.println("\n=== Kitap Bağışı Yapma ===");

        // Kitap seçimi
        System.out.print("Bağışlamak istediğiniz kitabın ID'si: ");
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
            System.out.println("Bu kitap aktif değil! Bağış yapamazsınız.");
            return;
        }

        // Adet
        System.out.print("Bağış yapacağınız kitap adedi: ");
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
        System.out.print("Açıklama (isteğe bağlı): ");
        String aciklama = scanner.nextLine();

        try {
            bagisService.bagisKaydet(uye.getUyeID(), kitapID, adet);
            System.out.println("Bağışınız başarıyla kaydedildi.");
            System.out.println("Kitap: " + kitap.getKitapAdi());
            System.out.println("Adet: " + adet);
            System.out.println("Bağış Tarihi: " + LocalDate.now());
            System.out.println("Kütüphanemize yaptığınız bağış için teşekkür ederiz!");
        } catch (Exception e) {
            System.out.println("Bağış işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void profilBilgileriniGoruntule() {
        System.out.println("\n=== Profil Bilgilerim ===");
        System.out.printf("Üye ID: %d%n", uye.getUyeID());
        System.out.printf("Ad: %s%n", uye.getUyeAdi());
        System.out.printf("Soyad: %s%n", uye.getUyeSoyad());
        System.out.printf("E-posta: %s%n", uye.getEmail());
        System.out.printf("Telefon: %s%n", uye.getTelNo() != null ? uye.getTelNo() : "-");
        System.out.printf("Adres: %s%n", uye.getAdres() != null ? uye.getAdres() : "-");
        System.out.printf("Durum: %s%n", uye.getDurum());
        System.out.printf("Üyelik Tarihi: %s%n", uye.getCreatedAt() != null ? uye.getCreatedAt().toLocalDate() : "-");
        System.out.printf("Son Güncelleme: %s%n", uye.getUpdatedAt() != null ? uye.getUpdatedAt().toLocalDate() : "-");
    }

    private void profilBilgileriniGuncelle() {
        System.out.println("\n=== Profil Bilgilerimi Güncelle ===");

        // Mevcut bilgileri göster
        System.out.println("Mevcut Bilgiler:");
        System.out.println("Ad: " + uye.getUyeAdi());
        System.out.println("Soyad: " + uye.getUyeSoyad());
        System.out.println("E-posta: " + uye.getEmail());
        System.out.println("Telefon: " + (uye.getTelNo() != null ? uye.getTelNo() : "-"));
        System.out.println("Adres: " + (uye.getAdres() != null ? uye.getAdres() : "-"));

        // Yeni bilgileri al
        System.out.print("\nYeni Ad (değiştirmek istemiyorsanız boş bırakın): ");
        String uyeAdi = scanner.nextLine();
        if (!uyeAdi.isEmpty()) {
            uye.setUyeAdi(uyeAdi);
        }

        System.out.print("Yeni Soyad (değiştirmek istemiyorsanız boş bırakın): ");
        String uyeSoyad = scanner.nextLine();
        if (!uyeSoyad.isEmpty()) {
            uye.setUyeSoyad(uyeSoyad);
        }

        System.out.print("Yeni Telefon (değiştirmek istemiyorsanız boş bırakın): ");
        String telNo = scanner.nextLine();
        if (!telNo.isEmpty()) {
            uye.setTelNo(telNo);
        }

        System.out.print("Yeni Adres (değiştirmek istemiyorsanız boş bırakın): ");
        String adres = scanner.nextLine();
        if (!adres.isEmpty()) {
            uye.setAdres(adres);
        }

        System.out.print("Yeni Şifre (değiştirmek istemiyorsanız boş bırakın): ");
        String sifre = scanner.nextLine();
        if (!sifre.isEmpty()) {
            uye.setSifre(sifre);
        }

        try {
            uyeService.uyeGuncelle(uye.getUyeID(), uye);
            System.out.println("Profil bilgileriniz başarıyla güncellendi.");
        } catch (Exception e) {
            System.out.println("Profil bilgileri güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }
}
