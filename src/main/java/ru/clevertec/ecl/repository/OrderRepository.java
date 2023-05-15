package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.clevertec.ecl.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

    /**
     * Find page or orders by user ID
     *
     * @param userId   current user ID
     * @param pageable current page
     * @return found orders page
     */
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
}
