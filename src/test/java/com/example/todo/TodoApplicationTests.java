package com.example.todo;

import com.example.todo.controller.TodoController;
import com.example.todo.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTests {

    @Autowired
    private TodoController todoController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        todoController.clearTodos();
    }

    @Test
    public void testGetTodos() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", new ArrayList<>()));
    }

    @Test
    public void testAddTodo() throws Exception {
        mockMvc.perform(post("/todos")
                        .param("description", "Study")
                        .param("priority", "High")
                        .param("dueDate", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos"));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", Arrays.asList(
                        new Todo(1L, "Study", "High", LocalDate.now())
                )));
    }

    @Test
    public void testCompleteTodo() throws Exception {
        mockMvc.perform(post("/todos")
                        .param("description", "Sleep")
                        .param("priority", "Medium")
                        .param("dueDate", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos"));

        mockMvc.perform(post("/todos/complete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos"));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", Arrays.asList(
                        new Todo(1L, "Sleep", "Medium", LocalDate.now(), true)
                )));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(post("/todos")
                        .param("description", "Eat")
                        .param("priority", "Low")
                        .param("dueDate", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos"));

        mockMvc.perform(post("/todos/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos"));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", new ArrayList<>()));
    }
}
