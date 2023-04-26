package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Long> {

    /**
     * Find tag by name
     * @param name tag name parameter
     * @return Optional of found tag, must be empty
     */
    Optional<Tag> findTagByName(String name);

}
