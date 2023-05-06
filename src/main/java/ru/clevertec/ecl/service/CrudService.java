package ru.clevertec.ecl.service;

public interface CrudService<REQUEST, RESPONSE, ID> extends FindService<RESPONSE, ID> {

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
