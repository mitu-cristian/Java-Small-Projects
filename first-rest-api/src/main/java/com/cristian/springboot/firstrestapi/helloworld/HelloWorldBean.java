package com.cristian.springboot.firstrestapi.helloworld;

public class HelloWorldBean {

    private String message;

    public HelloWorldBean(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
