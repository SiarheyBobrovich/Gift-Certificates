package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Long> {

    /**
     * Find Tag by name
     *
     * @param name tag name
     * @return optional of tag
     */
    Optional<Tag> findByName(String name);
}
