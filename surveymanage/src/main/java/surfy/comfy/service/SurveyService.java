package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.manage.DeleteSurveyResponse;
import surfy.comfy.data.manage.FinishSurveyResponse;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.entity.*;
import surfy.comfy.exception.post.CannotDeleteSurvey;
import surfy.comfy.repository.*;
import surfy.comfy.type.SurveyType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final PostRepository postRepository;
    private final Logger logger= LoggerFactory.getLogger(SurveyService.class);

    //멤버id - 설문지 가져오기
    @Transactional
    public List<SurveyResponse> getMysurvey(Long memberId){

        List<Survey> mySurveyList = surveyRepository.findAllByMember_Id(memberId);
        logger.info("survey[0] : {}",mySurveyList.get(0).getThumbnail());
        List<SurveyResponse> mySurvey = mySurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return mySurvey;
    }

    //설문지 전체 가져오기
    @Transactional
    public List<SurveyResponse> getAllSurveys() {
        List<Survey> SurveyList = surveyRepository.findAll();
        List<SurveyResponse> Surveys = SurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return Surveys;
    }

    //설문지 삭제
    @Transactional
    public DeleteSurveyResponse deleteSurvey(Long surveyId, String memberId){
        Survey survey = surveyRepository.findById(surveyId).get();
        Optional<Post> post = postRepository.findAllBySurvey_Id(survey.getId());

        if(post.isPresent()) {
            logger.info("삭제 불가능");
            throw new CannotDeleteSurvey();
        }

        surveyRepository.delete(survey);
        return new DeleteSurveyResponse(surveyId, Long.parseLong(memberId));

    }


    /**
     * minseo
     * 주기적으로 설문지 상태 업데이트
     */
//    @Scheduled(cron = "0 00 5 * * *") // 매일 오전 5:00에 실행되는 스케줄링
    @Scheduled(cron = "0 0 0/2 * * *") // 2시간마다 실행
    public void updateSurveyStatus(){
//        long time = System.currentTimeMillis();
//        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss E요일");
//        String str = dayTime.format(new Date(time));
        LocalDate now= LocalDate.now();

        logger.info("현재 시각: {}",now);

        List<Survey> surveyList=surveyRepository.findAll();
        for(Survey survey:surveyList){
            if(LocalDate.from(survey.getEnd()).isBefore(now)) survey.setStatus(SurveyType.finish);
        }
    }



    //설문지 상태 update
    @Transactional
    public FinishSurveyResponse finishSurvey(Long surveyId){
        Survey survey = surveyRepository.findById(surveyId).get();

        survey.setStatus(SurveyType.finish);
        return new FinishSurveyResponse(surveyId);
    }

    @Transactional
    public List<SurveyResponse> getSurveyByStatus(Long memberId,SurveyType status) {
        logger.info("[getSurveyByStatus] - memberId: {}",memberId);
        logger.info("[getSurveyByStatus] - status: {}",status);

        List<Survey> SurveyList = surveyRepository.findAllByMember_IdAndStatus(memberId,status);
        logger.info("SurveyList size: {}",SurveyList.size());
        List<SurveyResponse> Surveys = SurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());
        logger.info("[getSurveyByStatus] - result: {}",Surveys);
        return Surveys;
    }
}