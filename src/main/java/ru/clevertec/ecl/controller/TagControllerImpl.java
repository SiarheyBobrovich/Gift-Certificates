package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.controller.api.TagController;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.service.TagService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagControllerImpl implements TagController {

    private final TagService tagService;

    @Override
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseTagDto> getByIdTag(@PathVariable Long id) {
        ResponseTagDto tagDto = tagService.findById(id);
        return ResponseEntity.ok().body(tagDto);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseTagDto>> getAllTags(@PageableDefault(20) Pageable pageable) {
        Page<ResponseTagDto> tagDto = tagService.findAll(pageable);
        return ResponseEntity.ok().body(tagDto);
    }

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postTag(@RequestBody RequestTagDto dto) {
        tagService.create(dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putTag(@PathVariable Long id,
                                       @RequestBody @Valid RequestTagDto dto) {
        tagService.update(id, dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @Override
    @GetMapping("/popular")
    public ResponseEntity<ResponseTagDto> getMostPopularTag() {
        ResponseTagDto mostPopularTag = tagService.findMostPopularTag();
        return ResponseEntity.ok(mostPopularTag);
    }
}
