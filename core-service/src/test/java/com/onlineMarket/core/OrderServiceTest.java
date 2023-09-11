package com.onlineMarket.core;

import com.onlineMarket.api.dto.CartDto;
import com.onlineMarket.api.dto.CartItemDto;
import com.onlineMarket.api.dto.OrderDetailsDto;
import com.onlineMarket.core.data.Order;
import com.onlineMarket.core.data.Product;
import com.onlineMarket.core.integrations.CartServiceIntegration;
import com.onlineMarket.core.repository.OrdersRepository;
import com.onlineMarket.core.services.OrderService;
import com.onlineMarket.core.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@SpringBootTest (classes = OrderService.class)
//@ActiveProfiles ("test")
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private CartServiceIntegration cartServiceIntegration;
    @MockBean
    private ProductService productsService;
    @MockBean
    private OrdersRepository ordersRepository;

    @Test
    public void createOrderTest() {
        CartDto cartDto = new CartDto();
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(10L);
        cartItemDto.setProductTitle("Pen");
        cartItemDto.setQuantity(5);
        cartItemDto.setPricePerProduct(BigDecimal.valueOf(5.00));
        cartItemDto.setPrice(BigDecimal.valueOf(50.00));
        cartDto.setTotalPrice(BigDecimal.valueOf(50.00));
        cartDto.setProducts(List.of(cartItemDto));
        Mockito.doReturn(cartDto).when(cartServiceIntegration).getCart(null);
        Product product = new Product(10L, "Pen", BigDecimal.valueOf(10.0));
        Mockito.doReturn(Optional.of(product)).when(productsService).findById(10L);
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto("Berlin", "777 77 77");
        Order order = orderService.createOrder("Admin", orderDetailsDto);
        Assertions.assertEquals(order.getTotalPrice(), BigDecimal.valueOf(50.00));
        Mockito.verify(ordersRepository, Mockito.times(1)).save(ArgumentMatchers.eq(order));
    }
}
