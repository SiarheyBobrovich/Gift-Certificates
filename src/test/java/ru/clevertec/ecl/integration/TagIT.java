package ru.clevertec.ecl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.util.IntegrationTest;
import ru.clevertec.ecl.util.PostgresTestContainer;
import ru.clevertec.ecl.util.TestTagBuilder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.ecl.util.TestTagBuilder.*;

@Sql(value = "classpath:sql_script/insert_tag.sql")
@IntegrationTest
public class TagIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URI = "/api/v1/tags";

    @Test
    @SneakyThrows
    void getByIdTag() {
        Tag tag = builder().build().getTag();
        String expectedJson = mapper.writeValueAsString(tag);

        mockMvc.perform(get(URI + "/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    @SneakyThrows
    void getByIdNotFound() {
        mockMvc.perform(get(URI + "/{id}", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    void getAllTags() {
        List<Tag> tagList = List.of(
                builder().build().getTag(),
                Tag.builder().id(2L).name("Two").build());
        Pageable pageable = Pageable.ofSize(20);
        PageDto<Tag> pageDto = new PageDto<>(tagList, pageable, 2);
        String expectedJson = mapper.writeValueAsString(pageDto);

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Nested
    class Create {

        @Test
        @SneakyThrows
        void postTag() {
            TestTagBuilder tagBuilder = builder().withId(3L).withName("new_tag_abc").build();
            RequestTagDto requestTag = tagBuilder.getRequestTag();
            ResponseTagDto responseTag = tagBuilder.getResponseTag();

            String content = mapper.writeValueAsString(requestTag);
            String expectedContent = mapper.writeValueAsString(responseTag);

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expectedContent));
        }
    }

    @Test
    @SneakyThrows
    void putTag() {
        TestTagBuilder tagBuilder = builder().withName("updated").build();
        RequestTagDto requestTag = tagBuilder.getRequestTag();
        ResponseTagDto responseTag = tagBuilder.getResponseTag();
        Long id = responseTag.id();
        String requestJson = mapper.writeValueAsString(requestTag);
        String expectedJson = mapper.writeValueAsString(responseTag);

        mockMvc.perform(put(URI + "/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @SneakyThrows
    void deleteTag() {
        long id = 1L;

        mockMvc.perform(delete(URI + "/{id}", id))
                .andExpect(status().isOk());
    }

    @Nested
    @Sql("classpath:sql_script/popular_tag.sql")
    class WidelyTag {

        @Test
        @SneakyThrows
        void theMostWidelyTag() {
            ResponseTagDto responseTag = builder().withId(3L).withName("#3").build().getResponseTag();
            String expectedJson = mapper.writeValueAsString(responseTag);

            mockMvc.perform(get(URI + "/widely"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }
    }
}
