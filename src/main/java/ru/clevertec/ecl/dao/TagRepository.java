package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Long> {
    Optional<Tag> findTagByName(String name);

}
