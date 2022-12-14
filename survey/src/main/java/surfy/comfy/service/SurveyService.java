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
     * ????????? ?????? ????????? ?????? ??????
     * @param surveyId
     * @param memberId
     * @return
     */
    @Transactional
    public PostSurveyResponse makeSurvey(Long surveyId, Long memberId){
        List<Question> questionList= readQuestionRepository.findAllBySurvey_Id(surveyId);
        List<Grid> gridList= readGridRepository.findAllBySurvey_Id(surveyId);
        List<Option> optionList= readOptionRepository.findAllBySurvey_Id(surveyId);
        Survey createdSurvey= readSurveyRepository.findById(surveyId).get(); // ???????????? ??????????????? ????????? ????????? ???????????? ?????? ?????????

        // ??? ???????????? ??????
        Survey survey=new Survey();
        survey.setContents(createdSurvey.getContents());
        survey.setTitle(createdSurvey.getTitle());
        survey.setMemberId(memberId);
        survey.setStatus(SurveyType.notFinish);
        Long newSurveyId= writeSurveyRepository.save(survey).getId();

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

    //????????? ?????? ????????????
    @Transactional
    public List<SurveyResponse> getAllSurveys() {
        List<Survey> SurveyList = readSurveyRepository.findAll();
        List<SurveyResponse> Surveys = SurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return Surveys;
    }

    //????????? ??????
    @Transactional
    public DeleteSurveyResponse deleteSurvey(Long surveyId, String memberId){
        Survey survey = readSurveyRepository.findById(surveyId).get();
        Optional<Post> post = readPostRepository.findAllBySurveyId(survey.getId());

        if(post.isPresent()) {
            logger.info("?????? ?????????");
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
     * ??????????????? ????????? ?????? ????????????
     */
//    @Scheduled(cron = "0 00 5 * * *") // ?????? ?????? 5:00??? ???????????? ????????????
    @Scheduled(cron = "0 0 0/2 * * *") // 2???????????? ??????
    public void updateSurveyStatus(){
//        long time = System.currentTimeMillis();
//        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss E??????");
//        String str = dayTime.format(new Date(time));
        LocalDate now= LocalDate.now();

        logger.info("?????? ??????: {}",now);

        List<Survey> surveyList= readSurveyRepository.findAll();
        for(Survey survey:surveyList){
            if(LocalDate.from(survey.getEnd()).isBefore(now)) survey.setStatus(SurveyType.finish);
        }
    }



    //????????? ?????? update
    @Transactional
    public FinishSurveyResponse finishSurvey(Long surveyId){
        Survey survey = readSurveyRepository.findById(surveyId).get();

        survey.setStatus(SurveyType.finish);
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