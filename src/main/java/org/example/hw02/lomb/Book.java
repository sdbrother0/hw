package org.example.hw02.lomb;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class Book {
    private String title;
    private String author;
    private int pages;
    private BigDecimal price;
}
