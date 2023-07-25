package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.ItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private final ItemEntity itemEntity1 = new ItemEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemEntity itemEntity2 = new ItemEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemEntity itemEntity3 = new ItemEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final ItemEntity itemEntityEqual1 = new ItemEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemEntity itemEntityEqual2 = new ItemEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");

    @Test
    public void saveTest() {
        // when
        ItemEntity saved = itemRepository.save(itemEntity1);
        // then
        assertNotNull(saved);
        assertEquals(itemEntity1, saved);
    }

    @Test
    public void saveMultipleItemsTest() {
        // given
        List<ItemEntity> expectedItems = List.of(itemEntity1, itemEntity2, itemEntity3);
        // when
        itemRepository.save(itemEntity1);
        itemRepository.save(itemEntity2);
        itemRepository.save(itemEntity3);
        List<ItemEntity> allItems = itemRepository.findAll();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void saveItemMultipleTimesTest() {
        // given
        List<ItemEntity> expectedItems = List.of(itemEntity1);
        // when
        itemRepository.save(itemEntity1);
        itemRepository.save(itemEntity1);
        List<ItemEntity> allItems = itemRepository.findAll();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void saveEqualItemsTest() {
        // given
        List<ItemEntity> expectedItems = List.of(itemEntityEqual1);
        // when
        itemRepository.save(itemEntityEqual1);
        itemRepository.save(itemEntityEqual2);
        List<ItemEntity> allItems = itemRepository.findAll();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void findByNameTest() {
        // when
        itemRepository.save(itemEntity1);
        Optional<ItemEntity> fromRepositoryOptional = itemRepository.findByName(itemEntity1.getName());
        // then
        assertTrue(fromRepositoryOptional.isPresent());
        ItemEntity itemEntity = fromRepositoryOptional.get();
        assertEquals(itemEntity1, itemEntity);
    }

    @Test
    public void findByIdTest() {
        // when
        itemRepository.save(itemEntity1);
        Optional<ItemEntity> fromRepositoryOptional = itemRepository.findById(itemEntity1.getId());
        // then
        assertTrue(fromRepositoryOptional.isPresent());
        ItemEntity itemEntity = fromRepositoryOptional.get();
        assertEquals(itemEntity1, itemEntity);
    }

    @Test
    public void deleteByIdTest() {
        // given
        ItemEntity saved = itemRepository.save(itemEntity1);
        // when
        itemRepository.deleteById(saved.getId());
        Optional<ItemEntity> deleted = itemRepository.findById(saved.getId());
        // then
        assertTrue(deleted.isEmpty());
    }

}
