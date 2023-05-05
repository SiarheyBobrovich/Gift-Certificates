package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find tag by name
     * @param name tag name parameter
     * @return Optional of found tag, must be empty
     */
    Optional<Tag> findByName(String name);

    List<Tag> findByNameIn(Collection<String> names);
}
