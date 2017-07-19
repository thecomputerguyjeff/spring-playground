package com.allstate.compozed.springplayground.lesson;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by localadmin on 7/18/17.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonRepository repository;


//    @Test
//    public void CreateDelegatesToRepository(){
//        final LessonModel lessonModel = new LessonModel();
//        lessonModel.setTitle("Learned a Mock Database");
//    }

    @Test
    public void CreateDelegatesToRepository() throws Exception {
//Setup
        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());


        final MockHttpServletRequestBuilder post = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Mock me another one!\"}");


        //Exercise
        final ResultActions resultActions = mockMvc.perform(post);

//Asserts
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Mock me another one!")));

        verify(repository).save(any(LessonModel.class));


    }

    @Test
    public void GetArrayOfLessons() throws Exception {
        Iterable<LessonModel> lessons = new ArrayList<>();
//setup
        when(repository.findAll()).thenReturn(lessons);

        final MockHttpServletRequestBuilder get = get("/lessons");
//exercise
        final ResultActions resultActions = mockMvc.perform(get);
//asssert
        resultActions.andExpect(status().isOk())
                .andDo(print())

                .andExpect(jsonPath("$.length()", is(0)));


        verify(repository).findAll();


    }

    @Test
    public void ReadThrows404WhenIDDoesntExist() throws Exception {
        when(repository.findOne(100L)).thenReturn(null);
        final MockHttpServletRequestBuilder testRead = get("/lessons/100");

        final ResultActions resultActions = mockMvc.perform(testRead);

        resultActions.andExpect(status().isNotFound()).andDo(print());

        verify(repository).findOne(100L);

    }

    @Test
    public void readReturnsLessonWhenIDExists() throws Exception {
        LessonModel lesson = new LessonModel();
        lesson.setId(1l);
        lesson.setTitle("First Lesson");
        when(repository.findOne(1L)).thenReturn(lesson);
        final MockHttpServletRequestBuilder testRead = get("/lessons/1");

        final ResultActions resultActions = mockMvc.perform(testRead);


        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(repository).findOne(1L);


    }

    @Test
    public void updateShouldCreateNewRecordIfDoesntExist() throws Exception {
        when(repository.findOne(any(long.class))).then(returnsFirstArg());

        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());


        final MockHttpServletRequestBuilder testUpdate = put("/lessons/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Create Title\"}");

        final ResultActions resultActions = mockMvc.perform(testUpdate);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Create Title")));

        verify(repository).save(any(LessonModel.class));

    }

    @Test
    public void updateShouldUpdateRecordIfExists() throws Exception {
        LessonModel lesson = new LessonModel();
        lesson.setId(1l);
        lesson.setTitle("Original Title");

        when(repository.findOne(1L)).thenReturn(lesson);


        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());

        final MockHttpServletRequestBuilder testUpdate = put("/lessons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Title\"}");

        final ResultActions resultActions = mockMvc.perform(testUpdate);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")));

        verify(repository).save(any(LessonModel.class));


    }

    @Test
    public void deleteThrows404WhenIdDoesntExist() throws Exception {
        when(repository.findOne(100L)).thenReturn(null);
        final MockHttpServletRequestBuilder testDelete = delete("/lessons/100");

        final ResultActions resultActions = mockMvc.perform(testDelete);

        resultActions.andExpect(status().isNotFound()).andDo(print());

        verify(repository).findOne(100L);

    }

    @Test
    public void deleteDeletesWhenExists() throws Exception {

        LessonModel lesson = new LessonModel();
        lesson.setId(1l);
        lesson.setTitle("Original Title");
        long id = 1;

        when(repository.findOne(1L)).thenReturn(lesson);

        final MockHttpServletRequestBuilder testDelete = delete("/lessons/{id}", id);

        final ResultActions resultActions = mockMvc.perform(testDelete);

        resultActions.andExpect(status().isOk());
        verify(repository).delete(1L);


    }


}