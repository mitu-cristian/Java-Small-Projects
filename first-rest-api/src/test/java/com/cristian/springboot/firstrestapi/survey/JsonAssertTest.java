package com.cristian.springboot.firstrestapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class JsonAssertTest {

    @Test
    void jsonAssert_learningBasics() throws JSONException {

        String expectedResponse = """
                    {"id":"Question1","description":"Most Popular Cloud Platform Today"}
                """;
        String actualResponse = """
                       {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"]}
                """;
        JSONAssert.assertEquals(expectedResponse, actualResponse, false);
    }
}
