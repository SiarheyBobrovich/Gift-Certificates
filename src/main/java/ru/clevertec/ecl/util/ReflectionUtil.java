package ru.clevertec.ecl.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ReflectionUtil {

    public Class<?>[] getAllDeclaredInterfaces(Class<?> clazz) {
        List<Class<?>> interfaces = new ArrayList<>(Arrays.stream(clazz.getInterfaces())
                .toList());
        if (clazz.getSuperclass() == null) {
            return interfaces.toArray(Class[]::new);
        }

        interfaces.addAll(Arrays.asList(getAllDeclaredInterfaces(clazz.getSuperclass())));

        return interfaces.toArray(Class[]::new);
    }
}
