package com.example.product_api.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product_api.dto.ProductDTO.ProductRequestDTO;
import com.example.product_api.dto.ProductDTO.ProductResponseDTO;
import com.example.product_api.dto.ProductDTO.SoldProductRequestDTO;
import com.example.product_api.exception.ConflictException;
import com.example.product_api.exception.NotFoundException;
import com.example.product_api.model.Product;
import com.example.product_api.util.SlugUtil;
import com.example.product_api.mapper.ProductMapper;
import com.example.product_api.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository repository;
    @Autowired
    private final ProductMapper mapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper){
        this.repository = productRepository;
        this.mapper = productMapper;
    }

    public List<ProductResponseDTO> findAll(String category, String condition, Boolean featured, String soldDate){
        Specification<Product> spec = Specification.where(null);

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }

        if (condition != null && !condition.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("condition"), condition));
        }

        if (featured != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("featured"), featured));
        }

        if ("null".equalsIgnoreCase(soldDate)) {
            spec = spec.and((root, query, cb) -> cb.isNull(root.get("soldDate")));
        }

        return repository.findAll(spec)
            .stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product p = repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        return mapper.toResponse(p);
    }

    public ProductResponseDTO create(ProductRequestDTO dto){
        String slug = SlugUtil.slugify(dto.getName());

        if (repository.existsBySlug(slug)) {
            throw new ConflictException("Produto com este nome já existe");
        }

        Product p = new Product();
        mapper.updateEntityFromDto(dto, p);
        p.setSlug(slug);
        p.setSoldDate(null);

        Product saved = repository.save(p);
        return mapper.toResponse(saved);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto){
        Product existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        String newSlug = SlugUtil.slugify(dto.getName());

        if (repository.existsBySlugAndIdNot(newSlug, id)) {
            throw new ConflictException("Produto com este nome já existe");
        }

        mapper.updateEntityFromDto(dto, existing);
        existing.setSlug(newSlug);

        Product saved = repository.save(existing);
        return mapper.toResponse(saved);
    }

    public Long delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado");
        }
        repository.deleteById(id);
        return id;
    }

    public ProductResponseDTO markAsSold(Long id, SoldProductRequestDTO dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        Instant soldAt = dto != null && dto.getSoldDate() != null ? dto.getSoldDate() : Instant.now();
        existing.setSoldDate(soldAt);
        existing.setStock(0);

        Product saved = repository.save(existing);
        return mapper.toResponse(saved);
    }

}
