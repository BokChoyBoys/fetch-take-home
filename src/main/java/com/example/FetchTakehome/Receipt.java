package com.example.FetchTakehome;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
public record Receipt(UUID id,
                       String retailer,
                       LocalDate purchaseDate,
                       LocalTime purchaseTime,
                       LineItem[] items,
                       String total) {
    public Receipt {
        id = UUID.randomUUID();
    }
}