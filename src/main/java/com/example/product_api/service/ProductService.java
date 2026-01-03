package com.example.product_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product_api.dto.ProductDTO.ProductRequestDTO;
import com.example.product_api.dto.ProductDTO.ProductResponseDTO;
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

    public List<ProductResponseDTO> findAll(){
        return repository.findAll()
            .stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product p = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não existe"));
        return mapper.toResponse(p);
    }

    public ProductResponseDTO create(ProductRequestDTO dto){
        String slug = SlugUtil.slugify(dto.getName());

        if (repository.existsBySlug(slug)) {
            throw new RuntimeException("Produto com o mesmo slug já existe");
        }

        Product p = new Product();
        mapper.updateEntityFromDto(dto, p);
        p.setSlug(slug);

        Product saved = repository.save(p);
        return mapper.toResponse(saved);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto){
        Product existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não existe"));

        mapper.updateEntityFromDto(dto, existing);

        String newSlug = SlugUtil.slugify(dto.getName());
        if (!newSlug.equals(existing.getSlug())) {
            if (repository.existsBySlug(newSlug)) {
                throw new RuntimeException("Produto com o mesmo slug já existe");
            }
            existing.setSlug(newSlug);
        }

        Product saved = repository.save(existing);
        return mapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Produto não existe!");
        repository.deleteById(id);
    }

    

    
}
