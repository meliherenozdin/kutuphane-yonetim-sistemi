package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Kutuphane {

    @Id
    @Column(length = 20)
    private String vergiNo; // @Id alanı olarak kullanılıyor

    @NotBlank
    @Column(nullable = false)
    private String adi;
}
