package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.controller.KullaniciController.Kullanici;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.model.Personel;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import com.ekip.yakupmelih.library_manage_system.repository.PersonelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class KullaniciFactory {
    private final ApplicationContext context;
    private final UyeRepository uyeRepository;
    private final PersonelRepository personelRepository;
    private final Scanner scanner;

    @Autowired
    public KullaniciFactory(ApplicationContext context, UyeRepository uyeRepository,
            PersonelRepository personelRepository) {
        this.context = context;
        this.uyeRepository = uyeRepository;
        this.personelRepository = personelRepository;
        this.scanner = new Scanner(System.in);
    }

    // Factory Method
    public Kullanici kullaniciOlustur(String kullaniciTipi) {
        return switch (kullaniciTipi) {
            case "1" -> uyeOlustur();
            case "2" -> personelOlustur();
            default -> null;
        };
    }

    // Concrete Factory Methods
    private Kullanici uyeOlustur() {
        try {
            System.out.print("E-posta adresinizi girin: ");
            String email = scanner.nextLine();
            System.out.print("Şifrenizi girin: ");
            String sifre = scanner.nextLine();

            Optional<Uye> uyeOptional = uyeRepository.findByEmailAndSifre(email, sifre);
            if (uyeOptional.isPresent()) {
                UyeController uyeController = context.getBean(UyeController.class);
                uyeController.setUye(uyeOptional.get());
                return uyeController;
            } else {
                System.out.println("E-posta veya şifre hatalı.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Üye oluşturulurken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    private Kullanici personelOlustur() {
        try {
            System.out.print("E-posta adresinizi girin: ");
            String email = scanner.nextLine();
            System.out.print("Şifrenizi girin: ");
            String sifre = scanner.nextLine();

            Optional<Personel> personelOptional = personelRepository.findByEmailAndSifre(email, sifre);
            if (personelOptional.isPresent()) {
                PersonelController personelController = context.getBean(PersonelController.class);
                personelController.setPersonel(personelOptional.get());
                return personelController;
            } else {
                System.out.println("E-posta veya şifre hatalı.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Personel oluşturulurken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }
}
