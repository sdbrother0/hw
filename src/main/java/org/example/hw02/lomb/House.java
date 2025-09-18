package org.example.hw02.lomb;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class House {
    private final String address;
    private final int rooms;
    private final double area;
    private final BigDecimal price;
}
