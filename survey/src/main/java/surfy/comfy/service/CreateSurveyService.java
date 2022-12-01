package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.*;
import surfy.comfy.entity.write.*;
import surfy.comfy.repository.read.*;
import surfy.comfy.repository.write.*;
import surfy.comfy.type.QuestionType;
import surfy.comfy.type.SurveyType;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateSurveyService {
    private final EntityManager em;
    private final ReadQuestionRepository readQuestionRepository;
    private final ReadOptionRepository readOptionRepository;
    private final ReadGridRepository readGridRepository;
    private final WriteSurveyRepository writeSurveyRepository;
    private final WriteQuestionRepository writeQuestionRepository;
    private final WriteOptionRepository writeOptionRepository;
    private final WriteGridRepository writeGridRepository;

    @Transactional
    public String postSurveyThumbnail(Long surveyId,Long thumb){
        Survey survey= writeSurveyRepository.findById(surveyId).get();
        survey.setThumbnail(thumb);
        writeSurveyRepository.save(survey);
        return "썸네일 저장 성공";
    }
    @Transactional
    public void CreateSurveyDB(GetSurveyDataResponse data, Survey survey){
        survey.setTitle(data.getIntro0());
        survey.setContents(data.getIntro1());

        if(data.getEnd().equals("not")){
            survey.setStatus(SurveyType.notFinish);
        }
        else{
            survey.setStatus(SurveyType.surveying);
            LocalDate end=LocalDate.parse(data.getEnd());
            survey.setEnd(end);
            LocalDate start=LocalDate.parse(data.getStart());
            survey.setStart(start);
        }
        writeSurveyRepository.save(survey);

        List<GetQuestionResponse> ques_list=data.getQues_list();
        List<GetOptionResponse> ans_list=data.getAns_list();
        List<GetGridResponse> choice_list=data.getChoice_list();
        for(int i=0;i<ques_list.size();i++){
            Question question=new Question();

            GetQuestionResponse ques_item=ques_list.get(i);

            question.setSurvey(survey);
            question.setContents(ques_item.getQues());

            GetQuestionTypeResponse type=ques_item.getType();
            if(type.getId()==1 || type.getId()==2){
                for(int k=0;k<ans_list.size();k++){ //해당 Question의 ans_list 불러오기
                    GetOptionResponse ans_item=ans_list.get(k);

                    if(ans_item.getRootid()==ques_item.getId()){
                        Option option=new Option();

                        option.setQuestion(question);
                        option.setContents(ans_item.getValue());
                        option.setSurvey(survey);
                        writeOptionRepository.save(option);
                    }
                }
            }
            if(type.getId()==1){ //객관식
                if(!type.getChoice_type()){
                    question.setQuestionType(QuestionType.객관식_단일);
                }
                else{
                    question.setQuestionType(QuestionType.객관식_중복);
                }
            }
            else if(type.getId()==2){ //객관식 Grid
                if(!type.getChoice_type()){
                    question.setQuestionType(QuestionType.객관식_그리드_단일);
                }
                else {
                    question.setQuestionType(QuestionType.객관식_그리드_중복);
                }
                for(int k=0;k<choice_list.size();k++){ //해당 Question의 choice_list 불러오기
                    GetGridResponse choice_item=choice_list.get(k);
                    if(choice_item.getRootid()==ques_item.getId()){
                        Grid grid=new Grid();

                        grid.setQuestion(question);
                        grid.setContents(choice_item.getValue());
                        grid.setSurvey(survey);
                        writeGridRepository.save(grid);
                    }
                }
            }
            else if(type.getId()==3){
                question.setQuestionType(QuestionType.주관식);
            }
            else if(type.getId()==4) {
                question.setQuestionType(QuestionType.슬라이더);
            }
            writeQuestionRepository.save(question);
        }
        Question question=new Question();

        question.setSurvey(survey);
        question.setContents("만족도");
        question.setQuestionType(QuestionType.만족도);
        writeQuestionRepository.save(question);


    }

    @Transactional
    public void ResetSurveyDB(Survey survey){
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        List<Question> ques_list= writeQuestionRepository.findAllBySurveyId(survey.getId());
        for(int i=0;i<ques_list.size();i++){
            List<Option> opt_list= writeOptionRepository.findAllByQuestionId(ques_list.get(i).getId());
            List<Grid> grid_list= writeGridRepository.findAllByQuestionId(ques_list.get(i).getId());

            writeOptionRepository.deleteAllInBatch(opt_list);
            writeGridRepository.deleteAllInBatch(grid_list);
        }
        writeQuestionRepository.deleteAllInBatch(ques_list);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}