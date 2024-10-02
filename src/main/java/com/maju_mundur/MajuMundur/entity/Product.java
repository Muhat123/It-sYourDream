package com.maju_mundur.MajuMundur.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Nama barang wajib diisi")
    private String name;

    @NotNull(message = "Harga wajib dimasukan, kalau gratis isikan 0")
    private Double price;
    private String description;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
    @NotNull(message = "Quantity wajib dimasukan, isikan 0 jika stock belum ada")
    private int quantity;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> productPoster;
}
