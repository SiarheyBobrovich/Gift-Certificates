package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.tag.ResponseTagDto;

public interface CrudService<REQUEST, RESPONSE, ID> {

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

    /**
     * Create an entity by REQUEST
     *
     * @param dto REQUEST object(dto)
     */
    void create(REQUEST dto);

    /**
     * Update Entity by REQUEST object
     *
     * @param id  entity ID
     * @param dto REQUEST object(dto)
     */
    void update(ID id, REQUEST dto);

    /**
     * Delete an entity by ID
     *
     * @param id entity ID
     */
    void delete(ID id);
}
