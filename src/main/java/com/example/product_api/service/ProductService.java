package com.example.product_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product_api.model.Product;
import com.example.product_api.repository.ProductRepository;

@Service
public class ProductService {


    @Autowired
    private final ProductRepository repository;

    public ProductService(ProductRepository productRepository){
        this.repository = productRepository;
    }


    public List<Product> findAll(){
        return repository.findAll();
    }

       public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não existe"));
    }

    public Product create (Product product){
        return repository.save((product));
    }

    public Product update ( Long id, Product product){

        Product existing = findById(id);

          existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());

        return repository.save((existing));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Produto não existe!");
        repository.deleteById(id);
    }

}
