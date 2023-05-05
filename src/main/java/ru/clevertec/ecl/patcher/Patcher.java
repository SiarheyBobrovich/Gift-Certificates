package ru.clevertec.ecl.patcher;

import ru.clevertec.ecl.pageable.Patch;

@FunctionalInterface
public interface Patcher<T> {

    void applyPatch(T t, Patch patch);
}
