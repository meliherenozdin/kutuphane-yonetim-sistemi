package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.controller.KullaniciController.Kullanici;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class KullaniciFactory {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UyeRepository uyeRepository;

    public Kullanici olustur(String secim) {
        Scanner scanner = new Scanner(System.in);

        return switch (secim) {
            case "1" -> {
                System.out.print("E-posta adresinizi girin: ");
                String email = scanner.nextLine();

                Optional<Uye> uyeOptional = uyeRepository.findByEmail(email);
                if (uyeOptional.isPresent()) {
                    UyeController uyeController = context.getBean(UyeController.class);
                    uyeController.setUye(uyeOptional.get());
                    yield uyeController;
                } else {
                    System.out.println("Üye bulunamadı.");
                    yield null;
                }
            }
            case "2" -> context.getBean(PersonelController.class);
            default -> null;
        };
    }
}
