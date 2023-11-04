package com.cristian.springboot.learnjpaandhibernate.course.jdbc;

import com.cristian.springboot.learnjpaandhibernate.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseJdbcCommandLineRunner implements CommandLineRunner {

    @Autowired
    private CourseJdbcRepository repository;

    @Override
    public void run(String... args) throws Exception {
//        repository.insert(new Course(1, "Learn Azure", "Cristian"));
//        repository.insert(new Course(2, "AWS", "Cristian"));
//        repository.insert(new Course(3, "Google Cloud", "Cristian"));
//        repository.deleteById(1);
//        System.out.println(repository.findById(2));
    }


}
