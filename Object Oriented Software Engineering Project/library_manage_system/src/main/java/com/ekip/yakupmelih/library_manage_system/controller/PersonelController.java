package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;

public class PersonelController implements KullaniciController.Kullanici {

    @Override
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Personel Menü ===");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Kitap Sil");
            System.out.println("3. Kitap Güncelle");
            System.out.println("4. Kitap Ara");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();
            scanner.close();

            switch (secim) {
                case "1" -> kitapEkle();
                case "2" -> kitapSil();
                case "3" -> kitapGuncelle();
                case "4" -> kitapAra();
                case "5" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim.");
            }
        }
    }

    private void kitapEkle() {
        System.out.println(">> Kitap ekleme işlemi...");
    }

    private void kitapSil() {
        System.out.println(">> Kitap silme işlemi...");
    }

    private void kitapGuncelle() {
        System.out.println(">> Kitap güncelleme işlemi...");
    }

    private void kitapAra() {
        System.out.println(">> Kitap aranıyor...");
    }
}
