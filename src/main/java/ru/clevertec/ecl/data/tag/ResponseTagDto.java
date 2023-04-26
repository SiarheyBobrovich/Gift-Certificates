package ru.clevertec.ecl.data.tag;

import lombok.Builder;

@Builder
public record ResponseTagDto(Long id, String name) {
}
