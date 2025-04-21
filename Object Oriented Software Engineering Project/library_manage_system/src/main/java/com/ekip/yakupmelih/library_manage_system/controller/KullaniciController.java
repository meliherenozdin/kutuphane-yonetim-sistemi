package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;

public class KullaniciController {

    public static void girisEkrani() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Kütüphane Giriş Ekranı ===");
        System.out.println("1. Üye Girişi");
        System.out.println("2. Personel Girişi");
        System.out.print("Seçiminiz: ");
        String secim = scanner.nextLine();
        scanner.close();

        Kullanici kullanici = KullaniciFactory.olustur(secim);
        if (kullanici != null) {
            kullanici.menu();
        } else {
            System.out.println("Geçersiz seçim.");
        }
    }

    public interface Kullanici {
        void menu();
    }

    public static class KullaniciFactory {
        public static Kullanici olustur(String secim) {
            return switch (secim) {
                case "1" -> new UyeController();
                case "2" -> new PersonelController();
                default -> null;
            };
        }
    }
}
