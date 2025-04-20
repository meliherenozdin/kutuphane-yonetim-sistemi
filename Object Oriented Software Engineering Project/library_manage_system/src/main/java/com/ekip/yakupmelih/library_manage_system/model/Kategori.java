package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kategoriID;

    @NotBlank
    @Column(nullable = false)
    private String turu;
}
