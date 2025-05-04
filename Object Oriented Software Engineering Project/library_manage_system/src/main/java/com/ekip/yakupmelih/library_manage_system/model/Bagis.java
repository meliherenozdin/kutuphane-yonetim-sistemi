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
public class Bagis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bagisID;

    @ManyToOne
    @JoinColumn(name = "uyeID", nullable = false)
    private Uye uye;

    @ManyToOne
    @JoinColumn(name = "kitapID", nullable = false)
    private Kitap kitap;

    @Column(nullable = false)
    private LocalDate bagisTarih = LocalDate.now();

    @Column(nullable = false)
    private boolean aktif = true;

    @Column(length = 500)
    private String aciklama;

    @Column(nullable = false)
    private int adet;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
