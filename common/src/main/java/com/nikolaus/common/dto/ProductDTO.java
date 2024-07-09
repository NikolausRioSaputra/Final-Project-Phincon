package com.nikolaus.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO  {

    @NotNull(message = "id tidak boleh kosong")
    private Long id;

    @NotBlank(message = "Nama tidak boleh kosong")
    private String name;

    @NotNull(message = "Harga tidak boleh kosong")
    private float price;

    @NotNull(message = "Kategori tidak boleh kosong")
    private String category;

    @NotNull(message = "Tanggal pembuatan tidak boleh kosong")
    private LocalDateTime createdAt;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    private String description;

    @NotBlank(message = "URL gambar tidak boleh kosong")
    private String imageUrl;

    @NotNull(message = "Jumlah stok tidak boleh kosong")
    private Integer stockQuantity;

    @NotNull(message = "Tanggal update tidak boleh kosong")
    private LocalDateTime updatedAt;

}
