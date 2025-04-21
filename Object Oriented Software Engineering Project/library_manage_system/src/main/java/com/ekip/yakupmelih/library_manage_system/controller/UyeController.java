package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;

public class UyeController implements KullaniciController.Kullanici {

    @Override
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Üye Menü ===");
            System.out.println("1. Kitapları Listele");
            System.out.println("2. Kitap Ara");
            System.out.println("3. Ceza Bilgilerini Görüntüle");
            System.out.println("4. Kitap Bağışla");
            System.out.println("5. Ödünç Alınan Kitaplar");
            System.out.println("6. Kitap Rezerve Et");
            System.out.println("7. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();
            scanner.close();

            switch (secim) {
                case "1" -> kitaplariListele();
                case "2" -> kitapAra();
                case "3" -> cezalar();
                case "4" -> bagisYap();
                case "5" -> odunclariGoruntule();
                case "6" -> rezervasyonYap();
                case "7" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim.");
            }
        }
    }

    private void kitaplariListele() {
        System.out.println(">> Tüm kitaplar listeleniyor...");
        // kitapService.tumKitaplariGetir();

    }

    private void kitapAra() {
        System.out.println(">> Kitap aranıyor...");
    }

    private void cezalar() {
        System.out.println(">> Ceza bilgileri...");
    }

    private void bagisYap() {
        System.out.println(">> Bağış işlemi...");
    }

    private void odunclariGoruntule() {
        System.out.println(">> Ödünç alınan kitaplar...");
    }

    private void rezervasyonYap() {
        System.out.println(">> Rezervasyon işlemi...");
    }
}
