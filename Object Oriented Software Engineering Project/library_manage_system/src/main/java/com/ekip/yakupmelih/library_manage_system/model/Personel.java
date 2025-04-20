package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "telNo")
})
public class Personel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personelID;

    @NotBlank
    @Column(nullable = false)
    private String perAdi;

    @NotBlank
    @Column(nullable = false)
    private String perSoyad;

    @Column(unique = true, length = 15)
    private String telNo;

    @Email
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate isBaslamaTarih = LocalDate.now();
}
