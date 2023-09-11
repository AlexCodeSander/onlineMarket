package com.onlineMarket.core.controllers;

import com.onlineMarket.api.dto.ProductDto;
import com.onlineMarket.core.converters.ProductConverter;
import com.onlineMarket.core.data.Product;
import com.onlineMarket.core.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public Page<ProductDto> showProducts(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "part_title", required = false) String partTitle
    ) {
        if (page < 1) {
            page = 1;
        }
        return productService.find(minPrice, maxPrice, partTitle, page);
    }

    @DeleteMapping()
    public void deleteProductById(@RequestParam Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping()
    public void addProduct(@RequestBody ProductDto productDto) {
        productService.saveProduct(productDto);
    }

    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }
}