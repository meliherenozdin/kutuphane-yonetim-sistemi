package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"kitapID", "durum"})
})
public class Rezervasyon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rezervasyonID;

    @ManyToOne
    @JoinColumn(name = "uyeID", nullable = false)
    private Uye uye;

    @ManyToOne
    @JoinColumn(name = "kitapID", nullable = false)
    private Kitap kitap;

    @Column(nullable = false)
    private LocalDate rezervasyonTarih = LocalDate.now();

    private String durum = "Boşta";
}
