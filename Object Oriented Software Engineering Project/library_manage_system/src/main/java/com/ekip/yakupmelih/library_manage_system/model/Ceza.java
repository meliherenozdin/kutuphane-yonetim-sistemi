package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Ceza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cezaID;

    @ManyToOne
    @JoinColumn(name = "oduncID", nullable = false)
    private Odunc odunc;

    @ManyToOne
    @JoinColumn(name = "uyeID", nullable = false)
    private Uye uye;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal cezaMiktar;

    @Column(nullable = false)
    private LocalDate cezaTarih = LocalDate.now();

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
