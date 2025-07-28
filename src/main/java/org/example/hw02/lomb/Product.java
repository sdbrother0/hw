package org.example.hw02.lomb;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product {
    private String name;
    private String category;
    private BigDecimal price;
}
