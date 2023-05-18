package ru.clevertec.ecl.patcher;

import ru.clevertec.ecl.pageable.Patch;

@FunctionalInterface
public interface Patcher<T> {

    /**
     * Apply patch to T object
     *
     * @param t     object to be patched
     * @param patch patch field name, applied value
     */
    void applyPatch(T t, Patch patch);
}
