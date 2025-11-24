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

import com.example.product_api.model.Product;
import com.example.product_api.service.ProductService;

@RestController
@RequestMapping("/produtos")

public class ProductController {

    private final ProductService service;

    public ProductController(ProductService productService){
        this.service = productService;
    }

    @GetMapping
    public List<Product> findAll(){
        return service.findAll();
    }

    @GetMapping("produto/{id}")
    public Product findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping("/cadastrar")
    public Product save(@RequestBody Product product){
        return service.create(product);
    }

    @PutMapping("/atualizar/{id}")

    public Product update(@PathVariable Long id, @RequestBody Product product){
        return service.update(id, product);
    }

    @DeleteMapping("/deletar/{id}")
    public String delete(@PathVariable Long id){
    service.delete(id);
    return "Produto deletado com sucesso!";
    }

}
