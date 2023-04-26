package ru.clevertec.ecl.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.clevertec.ecl.exception.FilterException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Getter
public class Filter {

    private final String tagName;

    private final String partOfNameOrDescription;

    @Builder.Default
    private final Map<String, Order> sortFieldName = new HashMap<>();

    public void addSortFieldName(List<String> sort) {
        if (Objects.isNull(sort)) {
            return;
        }

        boolean isNameOrCreateDate = sort.stream()
                .allMatch(s -> s.matches("^name(_((asc)|(desc)))?") ||
                        s.matches("^createDate(_((asc)|(desc)))?"));

        if (!isNameOrCreateDate) {
            throw new FilterException("sort param must be 'name[_asc/_desc] or createDate[_asc/_desc]'");
        }

        sort.stream()
                .map(x -> x.split("_"))
                .forEach(x -> {
                    int length = x.length;
                    sortFieldName.put(x[0], length == 1 ? Order.ASC : Order.valueOf(x[1].toUpperCase()));
                });
    }
}
