package com.onlineMarket.core.services;

import com.onlineMarket.api.ResourceNotFoundException;
import com.onlineMarket.api.dto.ProductDto;
import com.onlineMarket.core.converters.ProductConverter;
import com.onlineMarket.core.repository.specifications.ProductSpecification;
import com.onlineMarket.core.data.Product;
import com.onlineMarket.core.repository.ProductRepository;
import com.onlineMarket.core.services.identity.ProductIdentityMap;
import com.onlineMarket.core.validators.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSpecification productSpecification;
    private final ProductIdentityMap productIdentityMap;
    private final ProductValidator productValidator;
    private final ProductConverter productConverter;

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDto findById(Long id) {
        if (!productIdentityMap.isContains(id)) {
            Optional<Product> product = productRepository.findById(id);
            product.ifPresent(productIdentityMap::addProduct);
            return productConverter.entityToDto(product.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));
        }
        return productConverter.entityToDto(productIdentityMap.getProduct(id));
    }
    public ProductDto saveProduct(ProductDto product) {
        product.setId(null);
        productValidator.validate(product);
        return productConverter.entityToDto(productRepository.save(productConverter.dtoToEntity(product)));
    }

    @Transactional
    public ProductDto updateProduct(Long productId, Product updatedProduct) {
                Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с ID " + productId + " не найден"));
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setPrice(updatedProduct.getPrice());
        Product savedProduct = productRepository.save(existingProduct);
        productIdentityMap.addProduct(savedProduct);
        return productConverter.entityToDto(savedProduct);
    }

    public Page<ProductDto> find(Integer minPrice, Integer maxPrice, String partTitle, Integer page) {
        Specification<Product> specification = Specification.where(null);
        if (minPrice != null) {
            specification = specification.and(productSpecification.priceGreaterOrEqualsThan(minPrice));
        }
        if (maxPrice != null) {
            specification = specification.and(productSpecification.priceLessOrEqualsThan(maxPrice));
        }
        if (partTitle != null) {
            specification = specification.and(productSpecification.titleLike(partTitle));
        }
        return  productRepository.findAll(specification, PageRequest.of(page - 1, 4)).map(productConverter::entityToDto);
    }
}