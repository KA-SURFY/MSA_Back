package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.manage.DeleteSurveyResponse;
import surfy.comfy.data.manage.FinishSurveyResponse;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.service.SurveyService;
import surfy.comfy.type.SurveyType;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final Logger logger= LoggerFactory.getLogger(SurveyController.class);

    /**
     * minseo
     * 마음에 드는 설문지 임시 저장
     * @param surveyId
     * @param memberId
     * @return
     */
    @GetMapping("/surveys/{memberId}")
    public BaseResponse<List<SurveyResponse>> getSurvey(@PathVariable(name = "memberId") Long memberId){
        logger.info("[Survey Controller] - getSurvey : {}",memberId);
        List<SurveyResponse> surveyList = surveyService.getMysurvey(memberId);

        List<SurveyResponse> result = new ArrayList<>();
        result.addAll(surveyList);

        return new BaseResponse<>(result);
    }

    @GetMapping("/surveys")
    public BaseResponse<List<SurveyResponse>> getAllSurvey() {
        List<SurveyResponse> surveyList = surveyService.getAllSurveys();
        return new BaseResponse<>(surveyList);
    }

    //설문지 삭제 api
    @DeleteMapping("/survey/{surveyId}/{memberId}")
    public BaseResponse<DeleteSurveyResponse> deleteSurvey (@PathVariable(name = "surveyId") Long surveyId, @PathVariable(name = "memberId") String memberId){
        DeleteSurveyResponse response = surveyService.deleteSurvey(surveyId, memberId);

        logger.info("[delete Survey]", response);
        return new BaseResponse<>(response);
    }

    //    설문지 상태에 따라서 가져오기
    // 임시저장 설문지 가져오기
    @GetMapping("/surveys/notFinish/{memberId}")
    public BaseResponse<List<SurveyResponse>> getSurveyByStatusNotFinish(@PathVariable(name="memberId")Long memberId){
        List<SurveyResponse> surveyList = surveyService.getSurveyByStatus(memberId,SurveyType.notFinish);
        return new BaseResponse<>(surveyList);
    }

    // 설문 완료 된 설문지 가져오기
    @GetMapping("/surveys/finish/{memberId}")
    public BaseResponse<List<SurveyResponse>> getSurveyByStatusFinish(@PathVariable(name="memberId")Long memberId){
        List<SurveyResponse> surveyList = surveyService.getSurveyByStatus(memberId,SurveyType.finish);
        return new BaseResponse<>(surveyList);
    }

    // 설문 중인 설문지 가져오기
    @GetMapping("/surveys/surveying/{memberId}")
    public BaseResponse<List<SurveyResponse>> getSurveyByStatusSurveying(@PathVariable(name="memberId")Long memberId){
        List<SurveyResponse> surveyList = surveyService.getSurveyByStatus(memberId,SurveyType.surveying);
        return new BaseResponse<>(surveyList);
    }

    /**
     * 정규
     * 설문지 상태바꾸기 api
     * @param surveyId
     * @return
     */
    @PatchMapping("/survey/{surveyId}")
    public BaseResponse<FinishSurveyResponse> finishSurvey(@PathVariable(name = "surveyId") Long surveyId){

        FinishSurveyResponse response = surveyService.finishSurvey(surveyId);

        return new BaseResponse<>(response);
    }

}
