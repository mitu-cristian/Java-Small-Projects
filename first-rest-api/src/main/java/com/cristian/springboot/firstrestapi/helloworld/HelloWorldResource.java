package com.cristian.springboot.firstrestapi.helloworld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {

    @RequestMapping("/hello-world-bean")
    public HelloWorldBean  helloWorlBean() {
        return new HelloWorldBean("Hello World");
    }

    //name is a path parameter
    @RequestMapping("/hello-world-path-param/{name}")
    public HelloWorldBean helloWorldPathParam(@PathVariable String name) {
        return new HelloWorldBean("Hello World, " + name);
    }

    @RequestMapping("/hello-world-path-param/{name}/message/{message}")
    public HelloWorldBean helloWorldBeanMultiplePathParam(@PathVariable String name,
                                                          @PathVariable String message) {
        return new HelloWorldBean("Hello World, " + name + ", " + message);
    }
}
