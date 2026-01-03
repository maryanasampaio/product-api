package com.example.product_api.mapper;

import org.springframework.stereotype.Component;

import com.example.product_api.dto.ProductDTO.ProductRequestDTO;
import com.example.product_api.dto.ProductDTO.ProductResponseDTO;
import com.example.product_api.model.Product;

@Component
public class ProductMapper {
    public ProductResponseDTO toResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImages(product.getImages());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    public void updateEntityFromDto(ProductRequestDTO dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImages(dto.getImages());
    }
}
