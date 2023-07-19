package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.model.dto.ItemQuantityInStockDto;
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

    private final ItemDto itemDto1 = new ItemDto(null, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDto itemDto2 = new ItemDto(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDto itemDto3 = new ItemDto(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @Test
    public void setProductCountTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        ItemQuantityInStockDto itemQuantity = new ItemQuantityInStockDto(itemEntity.getId(), 5);
        // when
        ItemQuantityInStockDto saved = stockService.setItemQuantity(itemQuantity);
        // then
        assertNotNull(saved);
        assertEquals(itemQuantity, saved);
    }

    @Test
    public void getProductCountTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        ItemQuantityInStockDto itemQuantity = new ItemQuantityInStockDto(
                itemEntity.getId(), 5);
        stockService.setItemQuantity(itemQuantity);
        // when
        ItemQuantityInStockDto saved = stockService.getItemQuantity(itemQuantity.id());
        // then
        assertNotNull(saved);
        assertEquals(itemQuantity, saved);
    }

    @Test
    public void getProductCountsTest() {
        // given
        ItemEntity itemEntity1 = itemService.addItemReturnEntity(itemDto1);
        ItemEntity itemEntity2 = itemService.addItemReturnEntity(itemDto2);
        ItemEntity itemEntity3 = itemService.addItemReturnEntity(itemDto3);
        ItemQuantityInStockDto itemQuantity1 = new ItemQuantityInStockDto(
                itemEntity1.getId(), 5);
        ItemQuantityInStockDto itemQuantity2 = new ItemQuantityInStockDto(
                itemEntity2.getId(), 10);
        ItemQuantityInStockDto itemQuantity3 = new ItemQuantityInStockDto(
                itemEntity3.getId(), 15);
        stockService.setItemQuantity(itemQuantity1);
        stockService.setItemQuantity(itemQuantity2);
        stockService.setItemQuantity(itemQuantity3);
        List<ItemQuantityInStockDto> expectedProductCounts = List.of(itemQuantity1, itemQuantity2, itemQuantity3);
        // when
        List<ItemQuantityInStockDto> allProductCounts = stockService.getItemQuantities();
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
