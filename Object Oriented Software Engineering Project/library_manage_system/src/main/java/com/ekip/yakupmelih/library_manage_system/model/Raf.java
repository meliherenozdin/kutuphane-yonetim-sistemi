package com.ekip.yakupmelih.library_manage_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Raf {

    @Id
    @Column(length = 50)
    private String rafNo;

    @NotBlank
    @Column(nullable = false)
    private String bolum;
}
