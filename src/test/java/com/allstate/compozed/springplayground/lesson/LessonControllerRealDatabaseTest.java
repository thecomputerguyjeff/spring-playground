/* See the file "LICENSE" for the full license governing this code. */
package com.allstate.compozed.springplayground.lesson;

/**
 * @author Dale "Ducky" Lotts
 * @since 7/19/17.
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerRealDatabaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository repository;


    @Transactional
    @Rollback
    @Test
    public void listReturnsExistingLessons() throws Exception {

        // Setup
        final LessonModel lessonOne = new LessonModel();
        lessonOne.setTitle("Spelling 001 with Dale oLtts");

        final LessonModel lessonTwo = new LessonModel();
        lessonTwo.setTitle("ACID for CRUDL");

        repository.save(Arrays.asList(lessonOne, lessonTwo));

        // Exercise
        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(lessonOne.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("Spelling 001 with Dale oLtts")));

        // Assert

    }

    @Transactional
    @Rollback
    @Test
    public void createReturnsSameLesson() throws Exception {


        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"My first lesson\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("My first lesson")));
    }

    @Transactional
    @Rollback
    @Test
    public void readReturns404IfDoesntExist() throws Exception {

        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isNotFound());


    }

    @Transactional
    @Rollback
    @Test
    public void readReturnsItemIfInDatabase() throws Exception {


        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"My first lesson\"}"));

        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("My first lesson")));


    }

    @Transactional
    @Rollback
    @Test
    public void deleteReturns404IfNotInDatabase() throws Exception {
        mockMvc.perform(delete("/lessons/123"))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Rollback
    @Test
    public void deleteDeletesEntryIfInDatabase() throws Exception {
        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"My first lesson\"}"));

        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("My first lesson")));


        mockMvc.perform(delete("/lessons/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isNotFound());

    }

    @Transactional
    @Rollback
    @Test
    public void updateReturns404IfNotInDatabase() throws Exception {
        mockMvc.perform(put("/lessons/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Title\"}"))
                .andExpect(status().isNotFound());

    }

    @Transactional
    @Rollback
    @Test
    public void updateShouldUpdateRecordIfInDatabase() throws Exception {
        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"My first lesson\"}"));

        mockMvc.perform(put("/lessons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")));

        mockMvc.perform(get("/lessons/1"))
                .andExpect(jsonPath("$.title", is("New Title")));

    }


}