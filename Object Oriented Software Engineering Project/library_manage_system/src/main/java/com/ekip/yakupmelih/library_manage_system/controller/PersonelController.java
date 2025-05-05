package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Personel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class PersonelController implements KullaniciController.Kullanici {

    private final Scanner scanner = new Scanner(System.in);
    private Personel personel;

    @Autowired
    private KullaniciFactory kullaniciFactory;

    public void setPersonel(Personel personel) {
        this.personel = personel;
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Personel Menüsü ===");
            System.out.println("Hoş geldiniz, " + personel.getPerAdi() + " " + personel.getPerSoyad());
            System.out.println("1. Kitap İşlemleri");
            System.out.println("2. Üye İşlemleri");
            System.out.println("3. Yazar İşlemleri");
            System.out.println("4. Kategori İşlemleri");
            System.out.println("5. Raf İşlemleri");
            System.out.println("6. Ödünç İşlemleri");
            System.out.println("7. Rezervasyon İşlemleri");
            System.out.println("8. Ceza İşlemleri");
            System.out.println("9. Bağış İşlemleri");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            if (secim.equals("0")) {
                return;
            }

            KullaniciController.Kullanici seciliKontrolcu = kullaniciFactory.kontrolcuSecimi(secim);
            if (seciliKontrolcu != null) {
                seciliKontrolcu.menu();
            } else {
                System.out.println("Geçersiz seçim!");
            }
        }
    }
}
