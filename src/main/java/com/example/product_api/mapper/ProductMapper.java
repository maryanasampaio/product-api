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
        dto.setCostPrice(product.getCostPrice());
        dto.setCondition(product.getCondition());
        dto.setCategory(product.getCategory());
        dto.setImages(product.getImages());
        dto.setStock(product.getStock());
        dto.setDimensions(product.getDimensions());
        dto.setMaterial(product.getMaterial());
        dto.setColor(product.getColor());
        dto.setBrand(product.getBrand());
        dto.setWarranty(product.getWarranty());
        dto.setFeatured(product.getFeatured());
        dto.setSoldDate(product.getSoldDate());
        dto.setDisponivel(product.getDisponivel());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    public void updateEntityFromDto(ProductRequestDTO dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setCondition(dto.getCondition());
        product.setCategory(dto.getCategory());
        product.setImages(dto.getImages());
        product.setStock(dto.getStock());
        product.setDimensions(dto.getDimensions());
        product.setMaterial(dto.getMaterial());
        product.setColor(dto.getColor());
        product.setBrand(dto.getBrand());
        product.setWarranty(dto.getWarranty());
        product.setFeatured(dto.getFeatured());
    }
}
