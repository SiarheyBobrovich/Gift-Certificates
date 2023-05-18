package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.controller.open_api.TagOpenApi;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.logging.Logging;
import ru.clevertec.ecl.service.TagService;

@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagControllerImpl implements TagOpenApi {

    private final TagService tagService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTagDto> getByIdTag(@PathVariable Long id) {
        ResponseTagDto tagDto = tagService.findById(id);

        return ResponseEntity.ok(tagDto);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseTagDto>> getAllTags(@PageableDefault(20) Pageable pageable) {
        Page<ResponseTagDto> tagDto = tagService.findAll(pageable);

        return ResponseEntity.ok(tagDto);
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseTagDto> postTag(@RequestBody @Valid RequestTagDto dto) {
        ResponseTagDto responseTagDto = tagService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseTagDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTagDto> putTag(@PathVariable Long id,
                                                 @RequestBody @Valid RequestTagDto dto) {
        ResponseTagDto tagDto = tagService.update(id, dto);

        return ResponseEntity.ok(tagDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.delete(id);

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/widely")
    public ResponseEntity<ResponseTagDto> getMostWidelyTag() {
        ResponseTagDto mostPopularTag = tagService.findMostWidelyTag();

        return ResponseEntity.ok(mostPopularTag);
    }
}
