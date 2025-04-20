package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "telNo")
})
public class Uye {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uyeID;

    @NotBlank
    @Column(nullable = false)
    private String uyeAdi;

    @NotBlank
    @Column(nullable = false)
    private String uyeSoyad;

    @Email
    @Column(unique = true)
    private String email;

    @Column(unique = true, length = 15)
    private String telNo;

    private String adres;
}
