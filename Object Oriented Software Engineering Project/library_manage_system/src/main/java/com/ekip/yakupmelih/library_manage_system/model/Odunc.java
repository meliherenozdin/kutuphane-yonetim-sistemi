package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
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
public class Odunc {

    public enum OduncDurum {
        AKTIF, TESLIM_EDILDI, GECIKMIS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oduncID;

    @ManyToOne
    @JoinColumn(name = "uyeID", nullable = false)
    private Uye uye;

    @ManyToOne
    @JoinColumn(name = "kitapID", nullable = false)
    private Kitap kitap;

    @Column(nullable = false)
    private LocalDate oduncAlmaTarih = LocalDate.now();

    private LocalDate gercekTeslimTarih;

    @Column(nullable = false)
    private LocalDate sonTeslimTarihi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OduncDurum durum = OduncDurum.AKTIF;

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
