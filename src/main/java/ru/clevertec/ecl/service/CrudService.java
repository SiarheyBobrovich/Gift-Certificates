package ru.clevertec.ecl.service;

import java.util.List;

public interface CrudService<REQUEST, RESPONSE, ID> {

    RESPONSE findById(ID id);

    List<RESPONSE> findAll();

    void create(REQUEST dto);

    void update(ID id, REQUEST dto);

    void delete(ID id);
}
