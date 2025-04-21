package com.ekip.yakupmelih.library_manage_system;

import com.ekip.yakupmelih.library_manage_system.controller.KullaniciController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManageSystemApplication {

	public static void main(String[] args) {
		// Spring context'i başlatabilirsin istersen
		// SpringApplication.run(LibraryManageSystemApplication.class, args);

		// Terminal tabanlı başlatıcı
		KullaniciController.girisEkrani();
	}
}
