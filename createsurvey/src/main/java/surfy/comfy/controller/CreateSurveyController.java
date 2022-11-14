package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.survey.*;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.service.CreateSurveyService;
import surfy.comfy.service.SurveyService;
import surfy.comfy.type.SurveyType;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CreateSurveyController {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final CreateSurveyService createSurveyService;
    private final SurveyService surveyService;

    private final Logger logger= LoggerFactory.getLogger(SurveyService.class);
    /**
     * minseo
     * @param request
     * @return
     */
//    @PatchMapping("/thumbnail")
//    public BaseResponse<String> postThumbnail(@RequestBody ThumbnailRequest request){
//        System.out.println("email: "+request.getEmail());
//        System.out.println("imgSrc: "+request.getImgSrc());
//        System.out.println("bgColor: "+request.getBgColor());
//        System.out.println("surveyId: "+request.getSurveyId());
//
//        surveyService.patchSurveyThumbnail(request);
//
//        return new BaseResponse<>("ggg");
//    }

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
    public BaseResponse<Long> CreateSurvey(@RequestBody GetSurveyDataResponse data, @PathVariable(name="memberId")String memberId){

        System.out.println("memberId: "+memberId);
        Optional<Member> loadmember= memberRepository.findById(Long.parseLong(memberId));
        Member member=loadmember.get();

        Survey survey=new Survey();
        survey.setMember(member);

        createSurveyService.CreateSurveyDB(data,survey,member);
        System.out.println("surveyId"+survey.getId());

        return new BaseResponse<>(survey.getId());
    }

    @CacheEvict(value = "survey", key = "#surveyId", cacheManager = "CacheManager")
    @PostMapping("/survey/{surveyId}/{memberId}")
    public BaseResponse<Long> EditSurvey(@RequestBody GetSurveyDataResponse data,@PathVariable(name="surveyId")Long surveyId, @PathVariable(name="memberId")String memberId){
        Optional<Member> loadmember= memberRepository.findById(Long.parseLong(memberId));
        Member member=loadmember.get();
        Survey survey = surveyRepository.findSurveysById(surveyId);

        createSurveyService.ResetSurveyDB(survey);
        createSurveyService.CreateSurveyDB(data,survey,member);
        return new BaseResponse<>(survey.getId());
    }

    @PostMapping("/created-survey/{surveyId}/{memberId}")
    public BaseResponse<PostSurveyResponse> postCreatedSurvey(@PathVariable(name="surveyId")Long surveyId,@PathVariable(name="memberId")Long memberId){
        logger.info("survey controller - postCreatedSurvey");
        PostSurveyResponse response=surveyService.makeSurvey(surveyId,memberId);

        return new BaseResponse<>(response);
    }
}