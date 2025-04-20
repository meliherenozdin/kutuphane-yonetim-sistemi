package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
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
}
