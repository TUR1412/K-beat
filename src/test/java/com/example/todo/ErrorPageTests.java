package com.example.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ErrorPageTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testErrorPageIncludesRequestId() throws Exception {
        mockMvc.perform(get("/error").accept(MediaType.TEXT_HTML))
                .andExpect(status().isInternalServerError())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("requestId"));
    }
}
