package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import ru.clevertec.ecl.entity.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    /**
     * Find user by ID
     *
     * @param id user ID
     * @return optional of found user
     */
    Optional<User> findById(Long id);

    /**
     * Find page of users
     *
     * @param pageable current page
     * @return found users page
     */
    Page<User> findAll(Pageable pageable);
}
