package com.ccs.desafiocaju.domain.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Entity
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    private String city;

    @Size(max = 2)
    @NotBlank
    private String country;

    @NotNull
    @Size(max = 4)
    private String mcc;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

}