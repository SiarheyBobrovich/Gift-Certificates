package ru.clevertec.ecl.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/api/v1/tags")
public interface TagController {

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseTagDto> getByIdTag(@PathVariable Long id);

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ResponseTagDto>> getAllTags();

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> postTag(@RequestBody RequestTagDto dto);

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> putTag(@PathVariable Long id,
                                @RequestBody RequestTagDto dto);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteTag(@PathVariable Long id);
}
