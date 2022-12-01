package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.manage.DeleteSurveyResponse;
import surfy.comfy.data.manage.FinishSurveyResponse;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.data.survey.PostSurveyResponse;
import surfy.comfy.entity.read.Post;
import surfy.comfy.entity.write.Grid;
import surfy.comfy.entity.write.Option;
import surfy.comfy.entity.write.Question;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.exception.post.CannotDeleteSurvey;
import surfy.comfy.repository.read.*;
import surfy.comfy.repository.write.WriteGridRepository;
import surfy.comfy.repository.write.WriteOptionRepository;
import surfy.comfy.repository.write.WriteQuestionRepository;
import surfy.comfy.repository.write.WriteSurveyRepository;
import surfy.comfy.type.SurveyType;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {
    private final CreateSurveyService createSurveyService;
    private final InternalRequestService internalRequestService;
    private final EntityManager em;

    private final Logger logger= LoggerFactory.getLogger(SurveyService.class);
    private final ReadPostRepository readPostRepository;
    private final ReadSurveyRepository readSurveyRepository;
    private final ReadQuestionRepository readQuestionRepository;
    private final ReadGridRepository readGridRepository;
    private final ReadOptionRepository readOptionRepository;

    private final WriteSurveyRepository writeSurveyRepository;
    private final WriteQuestionRepository writeQuestionRepository;
    private final WriteGridRepository writeGridRepository;
    private final WriteOptionRepository writeOptionRepository;

    /**
     * minseo
     * 마음에 드는 설문지 임시 저장
     * @param surveyId
     * @param memberId
     * @return
     */
    @Transactional
    public PostSurveyResponse makeSurvey(Long surveyId, Long memberId){
        List<Question> questionList= readQuestionRepository.findAllBySurveyId(surveyId);
        List<Grid> gridList= readGridRepository.findAllBySurveyId(surveyId);
        List<Option> optionList= readOptionRepository.findAllBySurveyId(surveyId);
        Survey createdSurvey= readSurveyRepository.findById(surveyId).get(); // 커뮤니티 게시글에서 마음에 들어서 만드려고 하는 설문지

        // 내 설문지로 생성
        Survey survey=new Survey();
        survey.setContents(createdSurvey.getContents());
        survey.setTitle(createdSurvey.getTitle());
        survey.setMemberId(memberId);
        survey.setStatus(SurveyType.notFinish);
        Long newSurveyId= writeSurveyRepository.save(survey).getId();
        logger.info("[SurveyService] - makeSurvey - newSurveyId: {}",writeSurveyRepository.findById(newSurveyId).get().getId());
//        logger.info("[SurveyService] - makeSurvey - newSurveyId: {}",newSurveyId);
        for(Question q:questionList){
            Question question=new Question();
            question.setContents(q.getContents());
            question.setSurvey(readSurveyRepository.findById(newSurveyId).get());
            question.setQuestionType(q.getQuestionType());
            Long newQuestionId= writeQuestionRepository.save(question).getId();

            for(Grid g:gridList){
                Grid grid=new Grid();
                grid.setContents(g.getContents());
                grid.setQuestion(readQuestionRepository.findById(newQuestionId).get());
                grid.setSurvey(readSurveyRepository.findById(newSurveyId).get());

                writeGridRepository.save(grid);
            }

            for(Option o:optionList){
                Option option=new Option();
                option.setContents(o.getContents());
                option.setSurvey(readSurveyRepository.findById(newSurveyId).get());
                option.setQuestion(readQuestionRepository.findById(newQuestionId).get());

                writeOptionRepository.save(option);
            }
        }

        return new PostSurveyResponse(newSurveyId,memberId);

    }
    @Transactional
    public List<SurveyResponse> getMysurvey(Long memberId){

        List<Survey> mySurveyList = readSurveyRepository.findAllByMemberId(memberId);
        logger.info("survey[0] : {}",mySurveyList.get(0).getThumbnail());
        List<SurveyResponse> mySurvey = mySurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return mySurvey;
    }

    //설문지 전체 가져오기
    @Transactional
    public List<SurveyResponse> getAllSurveys() {
        List<Survey> SurveyList = readSurveyRepository.findAll();
        List<SurveyResponse> Surveys = SurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return Surveys;
    }

    //설문지 삭제
    @Transactional
    public DeleteSurveyResponse deleteSurvey(Long surveyId, String memberId){
        Survey survey = writeSurveyRepository.findById(surveyId).get();
        Optional<Post> post = readPostRepository.findAllBySurveyId(survey.getId());

        if(post.isPresent()) {
            logger.info("삭제 불가능");
            throw new CannotDeleteSurvey();
        }

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        createSurveyService.ResetSurveyDB(survey);
        writeSurveyRepository.delete(survey);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

        logger.info("Respondent Delete is {}",internalRequestService.DeleteSurvey_Respondent(survey.getId()));

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

        List<Survey> surveyList= readSurveyRepository.findAll();
        for(Survey survey:surveyList){
            if(LocalDate.from(survey.getEnd()).isBefore(now)) survey.setStatus(SurveyType.finish);
        }
    }



    //설문지 상태 update
    @Transactional
    public FinishSurveyResponse finishSurvey(Long surveyId){
        Survey survey = writeSurveyRepository.findById(surveyId).get();
        survey.setStatus(SurveyType.finish);
        writeSurveyRepository.save(survey);
        return new FinishSurveyResponse(surveyId);
    }

    @Transactional
    public List<SurveyResponse> getSurveyByStatus(Long memberId,SurveyType status) {
        logger.info("[getSurveyByStatus] - memberId: {}",memberId);
        logger.info("[getSurveyByStatus] - status: {}",status);

        List<Survey> SurveyList = readSurveyRepository.findAllByMemberIdAndStatus(memberId,status);
        logger.info("SurveyList size: {}",SurveyList.size());
        List<SurveyResponse> Surveys = SurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());
        logger.info("[getSurveyByStatus] - result: {}",Surveys);
        return Surveys;
    }
}