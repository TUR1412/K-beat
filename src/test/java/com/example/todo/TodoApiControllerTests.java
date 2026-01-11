package com.example.todo;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoPriority;
import com.example.todo.repository.TodoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void testCreateListCompleteReopenDelete() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$").isArray());

        String body =
                """
                {
                  "description": "Write tests",
                  "priority": "HIGH",
                  "dueDate": "2025-12-17"
                }
                """;

        mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value("Write tests"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        assertThat(todoRepository.findAll()).hasSize(1);
        Todo created = todoRepository.findAll().get(0);

        mockMvc.perform(post("/api/todos/" + created.getId() + "/complete"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$.completed").value(true));

        mockMvc.perform(post("/api/todos/" + created.getId() + "/reopen"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(delete("/api/todos/" + created.getId()))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("X-Request-Id"));
        assertThat(todoRepository.findAll()).isEmpty();
    }

    @Test
    void testCreateValidationError() throws Exception {
        String body =
                """
                {
                  "description": "",
                  "priority": "LOW",
                  "dueDate": "2025-12-17"
                }
                """;

        MvcResult result =
                mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON).content(body))
                        .andExpect(status().isBadRequest())
                        .andExpect(header().exists("X-Request-Id"))
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.actionable_suggestion").exists())
                        .andExpect(jsonPath("$.request_id").exists())
                        .andReturn();

        String requestId = result.getResponse().getHeader("X-Request-Id");
        assertThat(requestId).isNotBlank();

        JsonNode payload = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(payload.get("request_id").asText()).isEqualTo(requestId);
    }

    @Test
    void testReopenNotFound() throws Exception {
        mockMvc.perform(post("/api/todos/99999/reopen"))
                .andExpect(status().isNotFound())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.actionable_suggestion").exists());
    }

    @Test
    void testRequestIdIsEchoedIfProvided() throws Exception {
        mockMvc.perform(get("/api/todos").header("X-Request-Id", "test-req-123"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Request-Id", "test-req-123"));
    }

    @Test
    void testTodoPriorityEnumIsStable() {
        assertThat(TodoPriority.valueOf("HIGH")).isEqualTo(TodoPriority.HIGH);
        assertThat(TodoPriority.valueOf("MEDIUM")).isEqualTo(TodoPriority.MEDIUM);
        assertThat(TodoPriority.valueOf("LOW")).isEqualTo(TodoPriority.LOW);
    }
}
