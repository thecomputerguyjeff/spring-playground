package com.allstate.compozed.springplayground.lesson;

import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by localadmin on 7/18/17.
 */

@RestController
@RequestMapping("/lessons")
final class LessonController {

    private final LessonRepository repository;

    public LessonController(LessonRepository repository) {
        this.repository = repository;

    }

    @GetMapping("")
    public Iterable<LessonModel> list() {
        return this.repository.findAll();

    }


    @PostMapping("")
    public LessonModel create(@RequestBody LessonModel lesson) {
        return this.repository.save(lesson);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonModel> read(@PathVariable long lessonId) {
        LessonModel lesson = this.repository.findOne(lessonId);
        if (null == lesson) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>(lesson, HttpStatus.OK);


    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonModel> update(@PathVariable long lessonId, @RequestBody LessonModel lesson) {
        LessonModel checkLesson = this.repository.findOne(lessonId);
        if (null == checkLesson) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        if (checkLesson.getId() != lessonId)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST );
        }
        return new ResponseEntity<>(this.repository.save(lesson), HttpStatus.OK);


    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> delete(@PathVariable long lessonId) {
        LessonModel ret = this.repository.findOne(lessonId);
        if (null == ret) {



            return new ResponseEntity<>(HttpStatus.NOT_FOUND);




        }


        this.repository.delete(lessonId);
        return new ResponseEntity<>(HttpStatus.OK);


    }


}
