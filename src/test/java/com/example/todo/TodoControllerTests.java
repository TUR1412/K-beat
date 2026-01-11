package com.example.todo;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoPriority;
import com.example.todo.repository.TodoRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void testGetTodos() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todos"));
    }

    @Test
    void testAddTodo() throws Exception {
        LocalDate dueDate = LocalDate.of(2025, 12, 17);

        mockMvc.perform(post("/todos")
                        .param("description", "Study")
                        .param("priority", TodoPriority.HIGH.name())
                        .param("dueDate", dueDate.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(redirectedUrl("/todos"));

        assertThat(todoRepository.findAll()).hasSize(1);
        Todo saved = todoRepository.findAll().get(0);
        assertThat(saved.getDescription()).isEqualTo("Study");
        assertThat(saved.getPriority()).isEqualTo(TodoPriority.HIGH);
        assertThat(saved.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    void testCompleteTodo() throws Exception {
        LocalDate dueDate = LocalDate.of(2025, 12, 20);
        Todo todo = todoRepository.save(new Todo("Sleep", TodoPriority.MEDIUM, dueDate));

        mockMvc.perform(post("/todos/complete/" + todo.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(redirectedUrl("/todos"));

        Todo updated = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    void testReopenTodo() throws Exception {
        LocalDate dueDate = LocalDate.of(2025, 12, 20);
        Todo todo = todoRepository.save(new Todo("Sleep", TodoPriority.MEDIUM, dueDate));
        todo.setCompleted(true);
        todoRepository.save(todo);

        mockMvc.perform(post("/todos/reopen/" + todo.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(redirectedUrl("/todos"));

        Todo updated = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(updated.isCompleted()).isFalse();
    }

    @Test
    void testDeleteTodo() throws Exception {
        LocalDate dueDate = LocalDate.of(2025, 12, 25);
        Todo todo = todoRepository.save(new Todo("Eat", TodoPriority.LOW, dueDate));

        mockMvc.perform(post("/todos/delete/" + todo.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(redirectedUrl("/todos"));

        assertThat(todoRepository.findAll()).isEmpty();
    }
}
