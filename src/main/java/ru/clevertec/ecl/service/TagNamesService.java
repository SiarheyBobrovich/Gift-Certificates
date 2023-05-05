package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.tag.ResponseTagDto;

import java.util.Collection;
import java.util.List;

public interface TagNamesService {

    /**
     * Find all tags by name
     *
     * @param names collection of names
     * @return List of existing tags
     */
    List<ResponseTagDto> findByNameIn(Collection<String> names);

}
