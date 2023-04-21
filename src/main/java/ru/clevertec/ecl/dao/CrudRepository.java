package ru.clevertec.ecl.dao;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    /**
     * Find entity in repository
     *
     * @param id entity ID
     * @return found entity if exist
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities in repository
     *
     * @return all found entities or empty list
     */
    List<T> findAll();

    /**
     * Delete an entity from repository
     *
     * @param id entity ID
     */
    void delete(ID id);

    /**
     * Save an entity in repository
     *
     * @param t entity to save
     * @return a saved entity
     */
    T save(T t);

    /**
     * Update an entity in repository
     *
     * @param t entity to save
     * @return an updated entity
     */
    T update(T t);
}
