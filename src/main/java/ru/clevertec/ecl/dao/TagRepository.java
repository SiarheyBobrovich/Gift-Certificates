package ru.clevertec.ecl.dao;

import org.hibernate.annotations.NamedNativeQuery;
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

    List<Tag> findByNameIn(Collection<String> names);

    @Query(nativeQuery = true, value = """
            select t.id, t.name
            from (SELECT o.user_id
                  from certificate.orders o
                  group by o.user_id
                  order by sum(o.price) desc
                  limit 1) u
                     join certificate.orders o USING (user_id)
                     join certificate.gc_tag gt USING (gc_id)
                     join certificate.tag t ON gt.t_id = t.id
            group by t.id, t.name
            limit 1
            """)
    Optional<Tag> findTheMostPopularTag();
}
