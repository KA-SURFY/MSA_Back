package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.survey.*;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.repository.read.ReadSurveyRepository;
import surfy.comfy.repository.write.WriteSurveyRepository;
import surfy.comfy.service.CreateSurveyService;
import surfy.comfy.service.SurveyService;

@RestController
@RequiredArgsConstructor
public class CreateSurveyController {

    private final ReadSurveyRepository readSurveyRepository;
    private final CreateSurveyService createSurveyService;
    private final SurveyService surveyService;

    private final Logger logger= LoggerFactory.getLogger(SurveyService.class);
    /**
     * minseo
     * @param request
     * @return
     */
    @PatchMapping("/survey/thumbnail/{surveyId}/{thumb}")
    public BaseResponse<String> postSurveyThumbnail(@PathVariable(name="surveyId")Long surveyId,@PathVariable(name="thumb")Long thumb){
        String response=createSurveyService.postSurveyThumbnail(surveyId,thumb);

        return new BaseResponse<>(response);
    }
    @PostMapping("/survey/{memberId}")
    public BaseResponse<Long> CreateSurvey(@RequestBody GetSurveyDataResponse data, @PathVariable(name="memberId")Long memberId){

        System.out.println("memberId: "+memberId);

        Survey survey=new Survey();
        survey.setMemberId(memberId);

        createSurveyService.CreateSurveyDB(data,survey);
        System.out.println("surveyId"+survey.getId());

        return new BaseResponse<>(survey.getId());
    }

    @CacheEvict(value = "survey", key = "#surveyId", cacheManager = "CacheManager")
    @PostMapping("/survey/{surveyId}/{memberId}")
    public BaseResponse<Long> EditSurvey(@RequestBody GetSurveyDataResponse data,@PathVariable(name="surveyId")Long surveyId, @PathVariable(name="memberId")Long memberId){
        Survey survey = readSurveyRepository.findSurveysById(surveyId);

        createSurveyService.ResetSurveyDB(survey);
        createSurveyService.CreateSurveyDB(data,survey);
        return new BaseResponse<>(survey.getId());
    }

    @PostMapping("/created-survey/{surveyId}/{memberId}")
    public BaseResponse<PostSurveyResponse> postCreatedSurvey(@PathVariable(name="surveyId")Long surveyId,@PathVariable(name="memberId")Long memberId){
        logger.info("survey controller - postCreatedSurvey");

        PostSurveyResponse response=surveyService.makeSurvey(surveyId,memberId);

        return new BaseResponse<>(response);
    }
}