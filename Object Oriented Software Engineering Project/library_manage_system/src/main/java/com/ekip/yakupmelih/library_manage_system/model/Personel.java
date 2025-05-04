package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "telNo")
})
public class Personel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personelID;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String perAdi;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String perSoyad;

    @Column(unique = true, length = 15)
    private String telNo;

    @Email
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private LocalDate isBaslamaTarih = LocalDate.now();

    @Column(nullable = false)
    private boolean aktif = true;

    @Column(length = 500)
    private String aciklama;

    // Şifreler hash'li olarak saklanmalıdır!
    @Column(nullable = false, length = 255)
    private String sifre;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
