package com.foodmarket.service;

import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductEntity;
import com.foodmarket.model.mapping.ProductMapper;
import com.foodmarket.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MarketServiceTest {

    @InjectMocks
    private MarketService marketService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private final ProductDTO productDTO1 = new ProductDTO("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void getProductByIdTest() {
        // given
        ProductEntity productEntity = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
        productEntity.setId(1L);
        // when
        Mockito.when(productRepository.findById(productEntity.getId()))
                .thenReturn(Optional.of(productEntity));
        Mockito.when(productMapper.productEntityToProductDto(productEntity))
                .thenReturn(productDTO1);
        ProductDTO fromService = marketService.getProductById(productEntity.getId());
        // then
        assertNotNull(fromService);
        assertEquals(productDTO1, fromService);
    }

}
