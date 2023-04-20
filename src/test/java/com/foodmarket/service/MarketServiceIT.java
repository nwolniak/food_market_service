package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class MarketServiceIT {

    @Autowired
    private MarketService marketService;

    private final ProductDTO productDTO1 = new ProductDTO("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final Set<ProductDTO> productDTOList1 = Set.of(productDTO1);
    private final Set<ProductDTO> productDTOList2 = Set.of(productDTO1, productDTO2);
    private final Set<ProductDTO> productDTOList3 = Set.of(productDTO1, productDTO2, productDTO3);

    private final OrderDTO orderDTO1 = new OrderDTO(productDTOList1);
    private final OrderDTO orderDTO2 = new OrderDTO(productDTOList2);
    private final OrderDTO orderDTO3 = new OrderDTO(productDTOList3);

    @Test
    public void addOrderTest() {
        // when
        OrderDTO saved = marketService.addOrder(orderDTO1);
        // then
        assertNotNull(saved);
        assertEquals(orderDTO1, saved);
    }

    @Test
    public void addMultipleOrdersTest() {
        // when
        marketService.addOrder(orderDTO1);
        marketService.addOrder(orderDTO2);
        marketService.addOrder(orderDTO3);
        List<OrderDTO> allOrders = marketService.getAllOrders();
        // then
        List<OrderDTO> expectedOrdersList = List.of(orderDTO1, orderDTO2, orderDTO3);
        assertFalse(allOrders.isEmpty());
        assertEquals(expectedOrdersList, allOrders);
    }

    @Test
    public void addOrderMultipleTimesTest() {
        // when
        marketService.addOrder(orderDTO1);
        marketService.addOrder(orderDTO1);
        List<OrderDTO> allOrders = marketService.getAllOrders();
        // then
        List<OrderDTO> expectedOrdersList = List.of(orderDTO1, orderDTO1);
        assertFalse(allOrders.isEmpty());
        assertEquals(expectedOrdersList, allOrders);
    }

    @Test
    public void addProductTest() {
        // when
        ProductDTO saved = marketService.addProduct(productDTO1);
        // then
        assertNotNull(saved);
        assertEquals(productDTO1, saved);
        assertEquals(1, marketService.getQuantityInStock(productDTO1));
    }

    @Test
    public void addMultipleProductsTest() {
        // when
        marketService.addProduct(productDTO1);
        marketService.addProduct(productDTO2);
        marketService.addProduct(productDTO3);
        List<ProductDTO> allProducts = marketService.getAllProducts();
        // then
        List<ProductDTO> expectedProductDtoList = List.of(productDTO1, productDTO2, productDTO3);
        assertFalse(allProducts.isEmpty());
        assertEquals(expectedProductDtoList, allProducts);
        allProducts.forEach(productDTO ->
                assertEquals(1, marketService.getQuantityInStock(productDTO)));
    }

    @Test
    public void addProductMultipleTimesTest() {
        // when
        marketService.addProduct(productDTO1);
        marketService.addProduct(productDTO1);
        List<ProductDTO> allProducts = marketService.getAllProducts();
        // then
        List<ProductDTO> expectedProductDtoList = List.of(productDTO1);
        assertFalse(allProducts.isEmpty());
        assertEquals(expectedProductDtoList, allProducts);
        assertEquals(1, marketService.getQuantityInStock(productDTO1));
        allProducts.forEach(productDTO ->
                assertEquals(1, marketService.getQuantityInStock(productDTO)));
    }

    @Test
    public void getProductByNameTest() {
        // given
        marketService.addProduct(productDTO1);
        // when
        ProductDTO fromService = marketService.getProductByName(productDTO1.name());
        // then
        assertNotNull(fromService);
        assertEquals(productDTO1, fromService);
    }

    @Test
    public void getProductByNameNotFoundException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            marketService.getProductByName("random name");
        });
    }

    @Test
    public void getProductByIdNotFoundException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            marketService.getProductById(new Random().nextLong());
        });
    }

}
