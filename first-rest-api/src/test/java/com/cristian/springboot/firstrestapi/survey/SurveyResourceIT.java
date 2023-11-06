package com.cristian.springboot.firstrestapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SurveyResourceIT {

    String str = """
                {"description": "Your Favorite Cloud Platform",
                "options": [
                    "AWS",
                    "Azure",
                    "Google Cloud",
                    "Oracle Cloud"
                ],
                "correctAnswer": "Google Cloud"}""";

    @Autowired
    private TestRestTemplate template;

    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTIONS_URL = "/surveys/Survey1/questions";


    @Test
    @Order(1)
    void retrieveAllSurveys() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange("/surveys", HttpMethod.GET, httpEntity, String.class);
        String expectedResponse = """
                [{"id": "Survey1"}]
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(1)
    void retrieveSurveyById() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange("/surveys/Survey1", HttpMethod.GET, httpEntity, String.class);
        String expectedResponse = """
                {"id": "Survey1"}
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(1)
    void retrieveAllSurveyQuestions() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange("/surveys/Survey1/questions", HttpMethod.GET, httpEntity, String.class);
        String expectedResponse = """
                    [ {"id": "Question1"},
               {"id": "Question2"},
               {"id": "Question3"}
               ]
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(1)
    void retrieveOneSurveyQuestion_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity
                = template.exchange(SPECIFIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);
//        ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
//        System.out.println(responseEntity.getBody());
//        System.out.println(responseEntity.getHeaders());
        String expectedResponse = """
                    {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"]}
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals(responseEntity.getHeaders().get("Content-Type").get(0), "application/json");
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(1)
    void addNewSurveyQuestion_basicScenario() {
        String requestBody = """
                {
                        "description": "Your favourite language",
                        "options": [
                            "Russian",
                            "German",
                            "Spanish",
                            "Turkish"
                        ],
                    "correctAnswer": "German"
                }
                """;
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> responseEntity
                = template.exchange(GENERIC_QUESTIONS_URL, HttpMethod.POST, httpEntity, String.class);
//        System.out.println(responseEntity.getHeaders());
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions"));

//        DELETE
//        Send an HTTP delete request to the location provided in the locationHeader

        ResponseEntity<String> responseEntityDelete
                = template.exchange(locationHeader, HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());

//        template.delete(locationHeader);
    }

    @Test
    @Order(2)
    void deleteSurveyQuestion() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange("/surveys/Survey1/questions/Question1", HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.getBody() == null);
    }

    @Test
    @Order(3)
    void updateSurveyQuestion() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        String requestBody = """
                {
                        "id": "Question1 upd",
                        "description": "Most Popular Cloud Platform",
                        "options": [
                            "AWS",
                            "Microdoft Azure",
                            "Google Cloud",
                            "Oracle Cloud"
                        ],
                        "correctAnswer": "AWS"
                    }
                """;
        HttpEntity httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = template.exchange("/surveys/Survey1/questions/Question1", HttpMethod.PUT, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.getBody() == null);
    }

    private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic " + performBasicAuthEncoding("cristian", "dummy"));
        return headers;
    }

    String performBasicAuthEncoding(String user, String password) {
        String combined = user + ":" + password;
//        Base64 Encoding => Bytes
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        return new String(encodedBytes);
    }


}
