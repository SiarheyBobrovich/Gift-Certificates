package ru.clevertec.ecl.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;

public interface TagController {

    ResponseEntity<ResponseTagDto> getByIdTag(Long id);

    ResponseEntity<Page<ResponseTagDto>> getAllTags(Pageable pageable);

    ResponseEntity<Void> postTag(RequestTagDto dto);

    ResponseEntity<Void> putTag(Long id,
                                RequestTagDto dto);

    ResponseEntity<Void> deleteTag(Long id);

    ResponseEntity<ResponseTagDto> getMostPopularTag();
}
