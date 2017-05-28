package net.songpon.v1.controller;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.query.TodoQuery;
import net.songpon.service.TodoServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoServiceImpl service;

    @Test
    public void testGetTodo() throws Exception {
        when(service.getTodo("96d4cdf2-0d47-4975-999a-1d82ec138aa8"))
                .thenReturn(mockTodo("96d4cdf2-0d47-4975-999a-1d82ec138aa8"));
        this.mockMvc.perform(get("/api/v1/todos/96d4cdf2-0d47-4975-999a-1d82ec138aa8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json("{\"id\":\"96d4cdf2-0d47-4975-999a-1d82ec138aa8\"" +
                        ",\"title\":\"mock todo\"" +
                        ",\"description\":\"Mocking todo and test with mockito\"}"));
    }

    @Test
    public void testListTodos() throws Exception {
        List<Todo> todos = new ArrayList<>();
        todos.add(mockTodo("11d4cdf2-0d47-4975-999a-1d82ec138bbc"));
        todos.add(mockTodo("22d4cdf2-0d47-4975-999a-1d82ec138bzz"));
        todos.add(mockTodo("33d4cdf2-0d47-4975-999a-1d82ec138bzz"));
        todos.add(mockTodo("44d4cdf2-0d47-4975-999a-1d82ec138ber"));
        todos.add(mockTodo("55d4cdf2-0d47-4975-999a-1d82ec138btt"));
        todos.add(mockTodo("66d4cdf2-0d47-4975-999a-1d82ec138bi8"));
        String expectedJson = "{\"size\":6,\"total\":6,\"data\":[{\"id\":\"11d4cdf2-0d47-4975-999a-1d82ec138bbc\"" +
                ",\"title\":\"mock todo\",\"description\":\"Mocking todo and test with mockito\"}" +
                ",{\"id\":\"22d4cdf2-0d47-4975-999a-1d82ec138bzz\",\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}" +
                ",{\"id\":\"33d4cdf2-0d47-4975-999a-1d82ec138bzz\",\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}" +
                ",{\"id\":\"44d4cdf2-0d47-4975-999a-1d82ec138ber\",\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}" +
                ",{\"id\":\"55d4cdf2-0d47-4975-999a-1d82ec138btt\",\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}" +
                ",{\"id\":\"66d4cdf2-0d47-4975-999a-1d82ec138bi8\",\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}]}";
        when(service.listTodos(any(TodoQuery.class))).thenReturn(todos);
        when(service.count(any(TodoQuery.class))).thenReturn(6);
        this.mockMvc.perform(get("/api/v1/todos?size=6")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testCreateTodo() throws Exception {
        String requestBody = "{\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}";
        when(service.create(any(Todo.class))).thenReturn(mockTodo("created-id"));
        MvcResult mvcResult = this.mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
        Assert.assertEquals(201, mvcResult.getResponse().getStatus());
        Assert.assertNotNull(mvcResult.getResponse().getHeader("Location"));
        verify(service, times(1)).create(any(Todo.class));
    }

    @Test
    public void testUpdateTodo() throws Exception {
        String requestBody = "{\"id\":\"96d4cdf2-0d47-4975-999a-1d82ec138aa8\"" +
                ",\"title\":\"mock todo\"" +
                ",\"description\":\"Mocking todo and test with mockito\"}";
        when(service.update(any(Todo.class))).thenReturn(any(Todo.class));
        MvcResult mvcResult = this.mockMvc.perform(put("/api/v1/todos/96d4cdf2-0d47-4975-999a-1d82ec138aa8")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        verify(service, times(1)).update(any(Todo.class));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        doNothing().when(service).delete("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
        this.mockMvc.perform(delete("/api/v1/todos/96d4cdf2-0d47-4975-999a-1d82ec138aa8"))
                .andExpect(status().isOk());
        verify(service, times(1)).delete("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
    }

    @Test
    public void testGetNoneExistTodo() throws Exception {
        when(service.getTodo("96d4cdf2-0d47-4975-999a-1d82ec138aa8")).thenThrow(EntityFoundException.class);
        this.mockMvc.perform(get("/api/v1/todos/96d4cdf2-0d47-4975-999a-1d82ec138aa8")).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void testCreateEmptyTitleTodo() throws Exception {
        String requestBody = "{\"description\": \"Finish travel data migration\"}";
        when(service.create(any(Todo.class))).thenReturn(mockTodo("created-id"));
        this.mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEmptyTitleTodo() throws Exception {
        String requestBody = "{\"id\": \"96d4cdf2-0d47-4975-999a-1d82ec138aa8\"" +
                ",\"description\": \"Finish travel data migration\"}";
        MvcResult mvcResult = this.mockMvc.perform(put("/api/v1/todos/96d4cdf2-0d47-4975-999a-1d82ec138aa8")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
        Assert.assertEquals(400, mvcResult.getResponse().getStatus());
        verify(service, times(0)).update(any(Todo.class));
    }

    @Test
    public void testDeleteNoneExistTodo() throws Exception {
        doNothing().when(service).delete("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
        this.mockMvc.perform(delete("/api/v1/todos/96d4cdf2-0d47-4975-999a-1d82ec138aa8"))
                .andExpect(status().isOk());
        verify(service, times(1)).delete("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
    }

    private Todo mockTodo(String id) {
        return new Todo(id, "mock todo", "Mocking todo and test with mockito");
    }
}