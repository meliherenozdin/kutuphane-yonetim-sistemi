package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Kitap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kitapID;

    @NotBlank
    @Column(nullable = false)
    private String kitapAdi;

    @NotBlank
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @NotNull
    @Column(nullable = false)
    private LocalDate basimYili;

    private String yayinevi;

    
    @Column(nullable = false)
    private int adet;

    @ManyToOne
    @JoinColumn(name = "kategoriID", nullable = true)
    private Kategori kategori;

    @ManyToOne
    @JoinColumn(name = "yazarID", nullable = true)
    private Yazar yazar;

    @ManyToOne
    @JoinColumn(name = "rafNo", referencedColumnName = "rafNo", nullable = true)
    private Raf raf;
}
