package com.cristian.springboot.firstrestapi.survey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SurveyResource {

    private SurveyService surveyService;

    SurveyResource(SurveyService surveyService) {
        super();
        this.surveyService = surveyService;
    }

//@desc     Get all the surveys
//@route    GET /surveys
//@access   PUBLIC
    @RequestMapping("/surveys")
    public List<Survey> retrieveAllSurveys() {
        return surveyService.retrieveAllSurveys();
    }

//@desc     Get a single survey
//@route    GET /surveys/{surveyId}
//@access   PUBLIC
    @RequestMapping("/surveys/{surveyId}")
    public Survey retrieveSurveyById(@PathVariable String surveyId) {
        Survey survey =  surveyService.retrieveSurveyById(surveyId);
        if(survey == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return survey;
    }

//@desc     Get the survey questions
//@route    GET /surveys/{surveyId}/questions
//@access   PUBLIC
    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveAllSurveyQuestions(@PathVariable String surveyId) {
        List<Question> questions = surveyService.retrieveAllSurveyQuestions(surveyId);
        if(questions == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return questions;
    }

//@desc     Get a single survey question
//@route    GET /surveys/{surveyId}/questions/{questionId}
//@access   PUBLIC
    @RequestMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveOneSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        Question question = surveyService.retrieveOneSurveyQuestion(surveyId, questionId);
        if(question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else {
            System.out.println(question);
            return question;
        }
    }

//@desc     Add a question to a survey
//@route    POST /surveys/{surveyId}/questions
    @RequestMapping(value = "/surveys/{surveyId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId, @RequestBody Question question) {
        String questionId = surveyService.addNewSurveyQuestion(surveyId, question);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{questionId}")
                .buildAndExpand(questionId).toUri();
        return ResponseEntity.created(location).build();
    }

//@desc     Delete a question from a survey
//@route    DELETE /surveys/{surveyId}/questions/{questionId}
    @RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
         surveyService.deleteSurveyQuestion(surveyId, questionId);
         return ResponseEntity.noContent().build();
    }

//@desc     Update a question from a survey
//@route    PUT /surveys/{surveyId}/questions/{questionId}
    @RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)
    public void updateSurveyQuestion(@PathVariable String surveyId,
                                                       @PathVariable String questionId, @RequestBody Question question) {
        surveyService.updateSurveyQuestion(surveyId, questionId, question);
    }
}
