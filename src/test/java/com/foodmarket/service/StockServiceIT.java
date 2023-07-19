package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemDTO;
import com.foodmarket.model.dto.ItemQuantityInStockDTO;
import com.foodmarket.model.entity.ItemEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class StockServiceIT {

    @Autowired
    private StockService stockService;

    @Autowired
    private ItemService itemService;

    private final ItemDTO itemDTO1 = new ItemDTO(null, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDTO itemDTO2 = new ItemDTO(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDTO itemDTO3 = new ItemDTO(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @Test
    public void setProductCountTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        ItemQuantityInStockDTO itemQuantity = new ItemQuantityInStockDTO(itemEntity.getId(), 5);
        // when
        ItemQuantityInStockDTO saved = stockService.setItemQuantity(itemQuantity);
        // then
        assertNotNull(saved);
        assertEquals(itemQuantity, saved);
    }

    @Test
    public void getProductCountTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        ItemQuantityInStockDTO itemQuantity = new ItemQuantityInStockDTO(
                itemEntity.getId(), 5);
        stockService.setItemQuantity(itemQuantity);
        // when
        ItemQuantityInStockDTO saved = stockService.getItemQuantity(itemQuantity.id());
        // then
        assertNotNull(saved);
        assertEquals(itemQuantity, saved);
    }

    @Test
    public void getProductCountsTest() {
        // given
        ItemEntity itemEntity1 = itemService.addItemReturnEntity(itemDTO1);
        ItemEntity itemEntity2 = itemService.addItemReturnEntity(itemDTO2);
        ItemEntity itemEntity3 = itemService.addItemReturnEntity(itemDTO3);
        ItemQuantityInStockDTO itemQuantity1 = new ItemQuantityInStockDTO(
                itemEntity1.getId(), 5);
        ItemQuantityInStockDTO itemQuantity2 = new ItemQuantityInStockDTO(
                itemEntity2.getId(), 10);
        ItemQuantityInStockDTO itemQuantity3 = new ItemQuantityInStockDTO(
                itemEntity3.getId(), 15);
        stockService.setItemQuantity(itemQuantity1);
        stockService.setItemQuantity(itemQuantity2);
        stockService.setItemQuantity(itemQuantity3);
        List<ItemQuantityInStockDTO> expectedProductCounts = List.of(itemQuantity1, itemQuantity2, itemQuantity3);
        // when
        List<ItemQuantityInStockDTO> allProductCounts = stockService.getItemQuantities();
        // then
        assertNotNull(allProductCounts);
        assertFalse(allProductCounts.isEmpty());
        assertTrue(CollectionUtils.isEqualCollection(expectedProductCounts, allProductCounts));
    }

    @Test
    public void getNotExistingProductCountShouldThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> stockService.getItemQuantity(123));
    }

}
