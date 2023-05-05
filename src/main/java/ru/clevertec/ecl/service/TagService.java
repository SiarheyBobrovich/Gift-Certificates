package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;

import java.util.Collection;
import java.util.List;

public interface TagService extends CrudService<RequestTagDto, ResponseTagDto, Long> {
}
