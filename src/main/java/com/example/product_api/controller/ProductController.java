package com.example.product_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_api.dto.ProductDTO.ProductRequestDTO;
import com.example.product_api.dto.ProductDTO.ProductResponseDTO;
import com.example.product_api.service.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/produtos")

public class ProductController {

    private final ProductService service;
    
    @GetMapping
    public List<ProductResponseDTO> findAll(){
        return service.findAll();
    }

    @GetMapping("/produto/{id}")
    public ProductResponseDTO findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping
    public ProductResponseDTO save(@Valid @RequestBody ProductRequestDTO product){
        return service.create(product);
    }

    @PutMapping("/atualizar/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO product){
        return service.update(id, product);
    }

    @DeleteMapping("/deletar/{id}")
    public String delete(@PathVariable Long id){
    service.delete(id);
    return "Produto deletado com sucesso!";
    }

}
