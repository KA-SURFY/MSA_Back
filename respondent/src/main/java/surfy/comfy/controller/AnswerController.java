package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.read.Survey;
import surfy.comfy.repository.read.ReadSurveyRepository;
import surfy.comfy.service.AnswerService;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final ReadSurveyRepository readSurveyRepository;
    private final AnswerService answerService;
    @SneakyThrows
    @Transactional
    @PostMapping("/respondent/{surveyId}")
    public void ResponseAnswer(@RequestBody GetSurveyDataResponse data, @PathVariable(name="surveyId")Long surveyId){

        Survey survey= readSurveyRepository.findSurveysById(surveyId);

        answerService.CreateAnswerDB(data,survey);
    }

    @SneakyThrows
    @Transactional
    @GetMapping("/api/deletesurvey/{surveyId}")
    public Boolean DeleteRespondent(@PathVariable(name="surveyId")Long surveyId){
        try{
            answerService.DeleteRespondentDB(surveyId);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}