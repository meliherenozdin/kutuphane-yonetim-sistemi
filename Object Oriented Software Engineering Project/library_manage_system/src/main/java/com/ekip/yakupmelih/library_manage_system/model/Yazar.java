package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Yazar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int yazarID;

    @NotBlank
    @Column(nullable = false)
    private String yazarAdi;

    @NotBlank
    @Column(nullable = false)
    private String yazarSoyad;
}
