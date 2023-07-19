package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.model.entity.ItemEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class ItemServiceIT {

    @Autowired
    private ItemService itemService;

    private final ItemDto itemDto1 = new ItemDto(1L, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDto itemDto2 = new ItemDto(2L, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDto itemDto3 = new ItemDto(3L, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @Test
    public void addItemTest() {
        // when
        ItemDto saved = itemService.addItem(itemDto1);
        // then
        assertNotNull(saved);
        assertEquals(itemDto1.name(), saved.name());
    }

    @Test
    public void getItemTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        // when
        ItemDto product = itemService.getItem(itemEntity.getId());
        // then
        assertNotNull(product);
        assertEquals(itemDto1, product);
    }

    @Test
    public void addMultipleItemsTest() {
        // given
        List<ItemDto> expectedItems = List.of(itemDto1, itemDto2, itemDto3);
        // when
        itemService.addItem(itemDto1);
        itemService.addItem(itemDto2);
        itemService.addItem(itemDto3);
        List<ItemDto> allItems = itemService.getAllItems();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void addItemMultipleTimesTest() {
        // given
        List<ItemDto> expectedItems = List.of(itemDto1);
        // when
        itemService.addItem(itemDto1);
        itemService.addItem(itemDto1);
        List<ItemDto> allItems = itemService.getAllItems();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void getNotExistingItemShouldThrowException() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.getItem(123));
    }

    @Test
    public void getNotExistingItemEntityShouldThrowException() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.getItemEntity(123));
    }

}
