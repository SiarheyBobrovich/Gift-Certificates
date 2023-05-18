package ru.clevertec.ecl.entity.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.ecl.entity.Order;

import java.time.LocalDateTime;

public class OrderListener {

    @PrePersist
    void createPurchase(Order order) {
        order.setPurchase(LocalDateTime.now());
    }
}
