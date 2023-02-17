package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;

import java.net.URI;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mvc;

    FilmController filmController = new FilmController();
    @MockBean
    FilmController service;

    @Test
    void getFilmsList() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/users")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void addFilmAndRefresh_Controller() throws Exception {
        Film film = Film.builder().id(1).name("Test")
                .description("Test")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(100L).build();
        Film filmEr = Film.builder().id(2).name("")
                .description("Test")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(100L).build();
        Film filmEr2 = Film.builder().id(3).name("Test")
                .description("")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(100L).build();
        Film filmEr3 = Film.builder().id(4).name("Test")
                .description("Test")
                .releaseDate(LocalDate.of(1700, 2, 13))
                .duration(100L).build();
        Film filmEr4 = Film.builder().id(5).name("Test")
                .description("Test")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(-100L).build();
        JsonMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String json = objectMapper.writeValueAsString(film);
        String errJson = objectMapper.writeValueAsString(filmEr);
        String errJson1 = objectMapper.writeValueAsString(filmEr2);
        String errJson2 = objectMapper.writeValueAsString(filmEr3);
        String errJson3 = objectMapper.writeValueAsString(filmEr4);

        mvc.getDispatcherServlet().getServletConfig();
        URI uri = new URI("http://localhost:8080/add-film");
        mvc.perform(post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        mvc.perform(post(uri).content(errJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(post(uri).content(errJson1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(post(uri).content(errJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(post(uri).content(errJson3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        URI uri2 = new URI("http://localhost:8080/upd-film");
        mvc.perform(put(uri2).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        mvc.perform(put(uri2).content(errJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(put(uri2).content(errJson1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(put(uri2).content(errJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        System.out.println(service.getFilmsList());
        mvc.perform(put(uri2).content(errJson3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void addAndPut_ToMap() {
        Film film = Film.builder().name("Test")
                .description("Test")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(100L).build();
        Film film2 = Film.builder().name("Test1")
                .description("Test")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(100L).build();
        Film film3 = Film.builder().name("TestTest").id(1)
                .description("Test")
                .releaseDate(LocalDate.of(2023, 2, 13))
                .duration(100L).build();

        filmController.addFilm(film);
        Assertions.assertEquals("Test", filmController.getFilmsList().get(0).getName());
        filmController.addFilm(film2);
        Assertions.assertEquals("Test1", filmController.getFilmsList().get(1).getName());
        filmController.updFilm(film3);
        Assertions.assertEquals("TestTest", filmController.getFilmsList().get(0).getName());

    }
}

