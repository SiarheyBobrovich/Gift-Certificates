package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find tag by name
     *
     * @param name tag name parameter
     * @return Optional of found tag, must be empty
     */
    Optional<Tag> findByName(String name);

    /**
     * Find tags by names
     *
     * @param names list of tag names
     * @return list of found tags
     */
    List<Tag> findByNameIn(Collection<String> names);

    /**
     * Get the most widely used tag of a user with the highest cost of all orders
     *
     * @return the most widely used tag
     */
    @Query(nativeQuery = true, value = """
            SELECT t.id, t.name
            FROM (SELECT o.user_id
                  FROM certificate.order o
                  GROUP BY o.user_id
                  ORDER BY sum(o.price) DESC 
                  LIMIT 1) u
                     JOIN certificate.order o USING (user_id)
                     JOIN certificate.gc_tag gt USING (gc_id)
                     JOIN certificate.tag t ON gt.t_id = t.id
            GROUP BY t.id, t.name
            ORDER BY count(t.id) DESC 
            LIMIT 1
            """)
    Optional<Tag> findTheMostWidelyTag();
}
