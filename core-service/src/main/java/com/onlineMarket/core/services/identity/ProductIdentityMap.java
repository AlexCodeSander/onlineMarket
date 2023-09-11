package com.onlineMarket.core.services.identity;

import com.onlineMarket.core.data.Product;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductIdentityMap {
    private Map<Long, Product> productMap;

    @PostConstruct
    private void init() {
        productMap = new HashMap<>();
    }

    public void addProduct(Product product) {
        productMap.put(product.getId(), product);
    }

    public Product getProduct(Long id) {
        return productMap.get(id);
    }

    public boolean isContains(Long id) {
        return productMap.containsKey(id);
    }
}