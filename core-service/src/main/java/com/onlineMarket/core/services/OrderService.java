package com.onlineMarket.core.services;

import com.onlineMarket.api.ResourceNotFoundException;
import com.onlineMarket.api.dto.CartDto;
import com.onlineMarket.api.dto.OrderDetailsDto;


import com.onlineMarket.api.dto.OrderDto;
import com.onlineMarket.core.converters.OrderConverter;
import com.onlineMarket.core.converters.ProductConverter;
import com.onlineMarket.core.integrations.CartServiceIntegration;

import com.onlineMarket.core.data.Order;
import com.onlineMarket.core.data.OrderItem;
import com.onlineMarket.core.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final CartServiceIntegration cartServiceIntegration;
    private final ProductService productsService;
    private final OrderConverter orderConverter;
    private final ProductConverter productConverter;

    @Transactional
    public Order createOrder(String username, OrderDetailsDto orderDetailsDto) {
        CartDto currentCart = cartServiceIntegration.getCart(username);
        Order order = new Order();
        order.setAddress(orderDetailsDto.getAddress());
        order.setPhone(orderDetailsDto.getPhone());
        order.setUsername(username);
        order.setTotalPrice(currentCart.getTotalPrice());
        List<OrderItem> items = currentCart.getProducts().stream()
                .map(o -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuantity(o.getQuantity());
                    item.setPricePerProduct(o.getPricePerProduct());
                    item.setPrice(o.getPrice());
                    item.setProduct(productConverter.dtoToEntity(productsService.findById(o.getProductId())));
                    return item;
                }).collect(Collectors.toList());
        order.setItems(items);
        ordersRepository.save(order);
        cartServiceIntegration.clearCart(username);
        return order;
    }

    public List<OrderDto> findOrdersByUsername(String username) {
        return ordersRepository.findAllByUsername(username).stream()
                .map(orderConverter::entityToDto).collect(Collectors.toList());
    }
}