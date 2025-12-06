-- Migration: Create Product Table
-- Author: Maryana Sampaio
-- Date: 2025-12-06
-- Description: Initial migration to create the products table

CREATE TABLE tb_produto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price BIGINT NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
