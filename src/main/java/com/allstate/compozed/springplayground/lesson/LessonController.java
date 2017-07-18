package com.allstate.compozed.springplayground.lesson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public LessonModel create(@RequestBody LessonModel lesson)
    {
        return this.repository.save(lesson);
    }
}
