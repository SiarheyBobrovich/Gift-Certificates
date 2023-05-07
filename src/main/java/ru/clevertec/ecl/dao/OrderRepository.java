package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.clevertec.ecl.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Page<Order> findAllByUserId(Long userId, Pageable pageable);
}
