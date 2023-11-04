package com.cristian.springboot.learnjpaandhibernate.course.springdatajpa;

import com.cristian.springboot.learnjpaandhibernate.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseSpringDataJpaCommandLineRunner implements CommandLineRunner {

    @Autowired
    private CourseSpringDataJpaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Course(1, "Learn AWS", "Cristian"));
        repository.save(new Course(2, "Learn Azure", "Cristian"));
        repository.save(new Course(3, "Learn Google Cloud", "Cristian"));

        repository.deleteById(1l);

        System.out.println(repository.findByName("Learn Azure"));
    }

}
