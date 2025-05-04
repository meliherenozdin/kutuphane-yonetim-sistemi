package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Kitap {

    public enum KitapDurum {
        AKTIF, PASIF, KAYIP, HASARLI
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kitapID;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String kitapAdi;

    @NotBlank
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @NotNull
    @Column(nullable = false)
    private LocalDate basimYili;

    @Column(length = 255)
    private String yayinevi;

    @Min(0)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KitapDurum durum = KitapDurum.AKTIF;

    @Column(nullable = false)
    private boolean aktif = true;

    @Column(length = 500)
    private String aciklama;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
