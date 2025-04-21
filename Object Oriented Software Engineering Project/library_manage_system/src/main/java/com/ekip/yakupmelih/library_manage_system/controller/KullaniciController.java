package com.ekip.yakupmelih.library_manage_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class KullaniciController {

    @Autowired
    private KullaniciFactory kullaniciFactory;

    public void girisEkrani() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Kütüphane Giriş Ekranı ===");
        System.out.println("1. Üye Girişi");
        System.out.println("2. Personel Girişi");
        System.out.print("Seçiminiz: ");
        String secim = scanner.nextLine();

        Kullanici kullanici = kullaniciFactory.olustur(secim);
        if (kullanici != null) {
            kullanici.menu();
        } else {
            System.out.println("Geçersiz seçim.");
        }
    }

    public interface Kullanici {
        void menu();
    }
}
