package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.controller.api.TagController;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagControllerImpl implements TagController {

    private final TagService tagService;

    @Override
    public ResponseEntity<ResponseTagDto> getByIdTag(Long id) {
        ResponseTagDto tagDto = tagService.findById(id);
        return ResponseEntity.ok().body(tagDto);
    }

    @Override
    public ResponseEntity<List<ResponseTagDto>> getAllTags() {
        List<ResponseTagDto> tagDto = tagService.findAll();
        return ResponseEntity.ok().body(tagDto);
    }

    @Override
    public ResponseEntity<Void> postTag(RequestTagDto dto) {
        tagService.create(dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> putTag(Long id,
                                       RequestTagDto dto) {
        tagService.update(id, dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> deleteTag(Long id) {
        tagService.delete(id);
        return ResponseEntity.status(204).build();
    }
}
