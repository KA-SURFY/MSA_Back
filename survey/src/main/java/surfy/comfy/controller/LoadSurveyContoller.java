package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.repository.read.ReadAnswerRepository;
import surfy.comfy.service.LoadSurveyService;

@RestController
@RequiredArgsConstructor
public class LoadSurveyContoller {

    private final LoadSurveyService loadSurveyService;
    private final Logger logger= LoggerFactory.getLogger(LoadSurveyContoller.class);
    private final ReadAnswerRepository readAnswerRepository;
    @SneakyThrows
    @GetMapping("/load/survey/{surveyId}")
    @Cacheable(value = "survey", key = "#surveyId", cacheManager = "CacheManager")
    public BaseResponse<GetSurveyDataResponse> SendEditSurveyData(@PathVariable(name="surveyId")Long surveyId){
        GetSurveyDataResponse result= loadSurveyService.getSurveyData(surveyId,null);
        logger.info("editSurvey - surveyId: {}",surveyId);
        return new BaseResponse<>(result);
    }


    @SneakyThrows
    @GetMapping("/load/respondent/{surveyId}")
    @Cacheable(value = "survey", key = "#surveyId", cacheManager = "CacheManager")
    public BaseResponse<GetSurveyDataResponse> SendRespondentSurveyData(@PathVariable(name="surveyId")Long surveyId){
        GetSurveyDataResponse result= loadSurveyService.getSurveyData(surveyId,null);
        logger.info("respondentSurvey - surveyId: {}",surveyId);
        return new BaseResponse<>(result);
    }


    @SneakyThrows
    @GetMapping("/load/respondent/answer/{surveyId}/{submitId}")
    @Cacheable(value = "survey", key = "#surveyId+':'+#submitId", cacheManager = "CacheManager")
    public BaseResponse<GetSurveyDataResponse> SendSurveyAnswerData(@PathVariable(name="surveyId")Long surveyId,@PathVariable(name="submitId")Long submitId){
        GetSurveyDataResponse result= loadSurveyService.getSurveyData(surveyId,submitId);
        logger.info("respondentSurveyAnswer - surveyId: {}, submitId: {}",surveyId,submitId);
        return new BaseResponse<>(result);
    }
}