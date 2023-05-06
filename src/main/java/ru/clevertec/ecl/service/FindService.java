package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindService<RESPONSE, ID> {

    /**
     * Find RESPONSE by ID parameter
     *
     * @param id - entity ID parameter
     * @return entity as RESPONSE
     */
    RESPONSE findById(ID id);

    /**
     * Find oll of Entities, map to RESPONSE and return
     *
     * @return List of mapped entity
     */
    Page<RESPONSE> findAll(Pageable pageable);
}
