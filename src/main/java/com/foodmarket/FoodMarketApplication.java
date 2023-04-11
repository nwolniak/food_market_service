package com.foodmarket;

import com.foodmarket.configuration.FoodMarketConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = FoodMarketConfiguration.class)
public class FoodMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodMarketApplication.class, args);
    }

}
