package com.example.product_api.dto.ProductDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Name é obrigatório")
    private String name;
    private String description;
    @NotNull(message = "Price é obrigatório")
    @Positive(message = "Price deve ser maior que zero")
    private Long price; // centavos
    private String images; // JSON/CSV conforme uso atual
}
