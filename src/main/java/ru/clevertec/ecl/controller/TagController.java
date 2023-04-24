package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseTagDto> getByIdTag(@PathVariable Long id) {
        ResponseTagDto tagDto = tagService.findById(id);
        return ResponseEntity.ok().body(tagDto);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseTagDto>> getAllTags() {
        List<ResponseTagDto> tagDto = tagService.findAll();
        return ResponseEntity.ok().body(tagDto);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postTag(@RequestBody RequestTagDto dto) {
        tagService.create(dto);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putTag(@PathVariable Long id,
                                       @RequestBody RequestTagDto dto) {
        tagService.update(id, dto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.status(204).build();
    }
}
