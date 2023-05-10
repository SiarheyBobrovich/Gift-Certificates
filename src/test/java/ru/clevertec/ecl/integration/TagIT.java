package ru.clevertec.ecl.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.util.StaticPostgresTestContainer;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TagIT extends StaticPostgresTestContainer {

    @Autowired
    private MockMvc testClient;

    private static final String URI = "/api/v1/tags";

    @Test
    @Sql("classpath:sql_script/insert_tag.sql")
    void getByIdTag() throws Exception {
        testClient.perform(MockMvcRequestBuilders.get(URI + "/1"))
                .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(200));
    }

    @Test
    void getByIdTagNotExist() throws Exception {
        testClient.perform(MockMvcRequestBuilders.get(URI + "/1"))
                .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(400));
    }
}
