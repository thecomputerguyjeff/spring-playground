package com.allstate.compozed.springplayground.lesson;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void CreateDelegatesToRepository() throws Exception
    {
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

    /*
     @GetMapping("")
    public Iterable<LessonModel> list() {
        return this.repository.findAll();

    }
     */
    @Test
    public void GetArrayOfLessons() throws Exception
    {
        Iterable<LessonModel> lessons = new ArrayList<>();

      when(repository.findAll()).thenReturn(lessons);

        final MockHttpServletRequestBuilder get = get("/lessons");

        final ResultActions resultActions = mockMvc.perform(get);

        resultActions.andExpect(status().isOk())
        .andDo(print())
                .andExpect(jsonPath();


        verify(repository).findAll();





    }



}