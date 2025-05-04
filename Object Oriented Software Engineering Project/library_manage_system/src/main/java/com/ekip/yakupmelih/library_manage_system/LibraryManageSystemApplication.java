package com.ekip.yakupmelih.library_manage_system;

import com.ekip.yakupmelih.library_manage_system.controller.KullaniciController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibraryManageSystemApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(LibraryManageSystemApplication.class, args);

		KullaniciController controller = context.getBean(KullaniciController.class);
		controller.girisEkrani();
	}
}
