
package com.example.product_api.dto.ProductDTO;

import java.time.Instant;
import com.example.product_api.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long price; 
    private String images;
    private Instant createdAt;
    private Instant updatedAt;

    public static ProductResponseDTO from(Product p) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setSlug(p.getSlug());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setImages(p.getImages());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        return dto;
    }
}
