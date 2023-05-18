package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;

public interface TagService extends CrudService<RequestTagDto, ResponseTagDto, Long> {

    /**
     * Get the most widely used tag of a user with the highest cost of all orders
     *
     * @return the most widely used tag as dto
     */
    ResponseTagDto findMostWidelyTag();
}
