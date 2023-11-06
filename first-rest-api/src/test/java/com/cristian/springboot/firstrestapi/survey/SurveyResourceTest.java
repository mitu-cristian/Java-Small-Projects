package com.cristian.springboot.firstrestapi.survey;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false) // disable Spring Security
public class SurveyResourceTest {

    @MockBean
    private SurveyService surveyService;
    @Autowired
    private MockMvc mockMvc;

    private static String GENERIC_SURVEYS_URL = "http://localhost:8080/surveys";
    private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions";

    @Test
    void retrieveAllSurveys() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GENERIC_SURVEYS_URL).accept(MediaType.APPLICATION_JSON);
        String expectedResponse = """
                    [{"id": "Survey1"}]
                """;
        List<Question> questions = new ArrayList<>(List.of(new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS")));

        List<Survey> survey = new ArrayList<>(List.of (new Survey("Survey1","Example", "Example", questions )));
        when(surveyService.retrieveAllSurveys()).thenReturn(survey);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void retrieveSurveyById() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GENERIC_SURVEYS_URL + "/Survey1")
                .accept(MediaType.APPLICATION_JSON);
        Survey survey = new Survey("Survey1");
        when(surveyService.retrieveSurveyById("Survey1")).thenReturn(survey);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                {"id": "Survey1"}
                """;
        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void retrieveAllSurveyQuestions() throws Exception {
        List<Question> questions = new ArrayList<>(List.of(new Question("Question1"), new Question("Question2"), new Question("Question3")));
        when(surveyService.retrieveAllSurveyQuestions("Survey1")).thenReturn(questions);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GENERIC_SURVEYS_URL + "/Survey1/questions")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
               [ {"id": "Question1"},
               {"id": "Question2"},
               {"id": "Question3"}
               ]
                """;
        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void retrieveOneSurveyQuestion_404Scenario() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    void retrieveOneSurveyQuestion_basicScenario() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);

        Question question = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        when(surveyService.retrieveOneSurveyQuestion("Survey1", "Question1")).thenReturn(question);

        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();

        String expectedResponse = """
                {"id":"Question1",
                "description":"Most Popular Cloud Platform Today",
                "options":["AWS","Azure","Google Cloud","Oracle Cloud"],
                "correctAnswer":"AWS"}
                """;

        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);

//        System.out.println(mvcResult.getResponse().getContentAsString());
//        System.out.println(mvcResult.getResponse().getStatus());
    }


    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception{
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
        when(surveyService.addNewSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(GENERIC_QUESTION_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String locationHeader = response.getHeader("Location");
        assertEquals(201, response.getStatus());
//        System.out.println(locationHeader);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/SOME_ID"));

    }

    @Test
    void deleteSurveyQuestion() throws Exception {
        when(surveyService.deleteSurveyQuestion("Survey1", "Question1")).thenReturn("Question1");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
        assertTrue(mvcResult.getResponse().getContentAsString().isEmpty());
    }

    @Test
    void updateSurveyQuestion() throws Exception {
        List<String> options = new ArrayList<>(List.of("AwS", "Microsoft Azure", "Google Cloud", "Oracle Cloud"));
        Question question = new Question("updated", "Most Popular Cloud Platform", options, "AWS");
        doNothing().when(surveyService).updateSurveyQuestion("Survey1", "Question1", question);
        String requestBody = """
                {
                        "id": "Question1 - updates",
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
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(SPECIFIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(mvcResult.getResponse().getContentAsString().isEmpty());
    }


}
