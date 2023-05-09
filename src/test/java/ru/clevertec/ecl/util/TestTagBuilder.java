package ru.clevertec.ecl.util;

import lombok.Builder;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

@Builder(toBuilder = true, setterPrefix = "with")
public class TestTagBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "One";

    public Optional<Tag> getOptional() {
        return Optional.of(getTagl());

    }

    public Tag getTagl() {
        return Tag.builder()
                .id(id)
                .name(name)
                .build();

    }

    public RequestTagDto getRequestTag() {
        return new RequestTagDto(name);
    }

    public ResponseTagDto getResponseTag() {
        return new ResponseTagDto(id, name);
    }
}
