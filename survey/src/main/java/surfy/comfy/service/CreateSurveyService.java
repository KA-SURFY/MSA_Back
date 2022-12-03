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
import java.awt.desktop.QuitEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Long CreateSurveyDB(GetSurveyDataResponse data, Survey survey, Long memberId){
        if(survey==null){
            survey=new Survey();
        }
        survey.setMemberId(memberId);
        survey.setTitle(data.getIntro0());
        survey.setContents(data.getIntro1());

        if(data.getEnd().equals("not")){
            survey.setStatus(SurveyType.notFinish);
        }
        else{
            survey.setStatus(SurveyType.surveying);
            LocalDate end=LocalDate.parse(data.getEnd());
            survey.setEnd(end.atTime(0,0,0));
            LocalDate start=LocalDate.parse(data.getStart());
            survey.setStart(start.atTime(0,0,0));
        }
        writeSurveyRepository.save(survey);

        List<GetQuestionResponse> ques_list=data.getQues_list();
        List<GetOptionResponse> ans_list=data.getAns_list();
        List<GetGridResponse> choice_list=data.getChoice_list();

        for(int i=0;i<ques_list.size();i++) {
            Question question = new Question();
            GetQuestionResponse ques_item = ques_list.get(i);

            question.setSurveyId(survey.getId());
            question.setContents(ques_item.getQues());

            GetQuestionTypeResponse type = ques_item.getType();

            writeQuestionRepository.save(question);
            if(type.getId()==1||type.getId()==2) {
                if (type.getId() == 1 && !type.getChoice_type()) question.setQuestionType(QuestionType.객관식_단일);
                else if (type.getId() == 1 && type.getChoice_type()) question.setQuestionType(QuestionType.객관식_중복);

                if (type.getId() == 2 && !type.getChoice_type()) question.setQuestionType(QuestionType.객관식_그리드_단일);
                else if (type.getId() == 2 && type.getChoice_type()) question.setQuestionType(QuestionType.객관식_그리드_중복);

                for(int k=0;k<ans_list.size();k++){ //해당 Question의 ans_list 불러오기
                    GetOptionResponse ans_item=ans_list.get(k);
                    if(ans_item.getRootid().equals(ques_item.getId())){
                        Option option=new Option();

                        option.setQuestionId(question.getId());
                        option.setContents(ans_item.getValue());
                        option.setSurveyId(survey.getId());

                        writeOptionRepository.save(option);
                    }
                }
                if(type.getId()==2){
                    for(int k=0;k<choice_list.size();k++){ //해당 Question의 choice_list 불러오기
                        GetGridResponse choice_item=choice_list.get(k);
                        if(choice_item.getRootid().equals(ques_item.getId())){
                            Grid grid=new Grid();

                            grid.setQuestionId(question.getId());
                            grid.setContents(choice_item.getValue());
                            grid.setSurveyId(survey.getId());

                            writeGridRepository.save(grid);
                        }
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
        question.setSurveyId(survey.getId());

        question.setContents("만족도");
        question.setQuestionType(QuestionType.만족도);

        writeQuestionRepository.save(question);

        return survey.getId();
    }

    @Transactional
    public void ResetSurveyDB(Survey survey){
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        writeOptionRepository.deleteAll(writeOptionRepository.findAllBySurveyId(survey.getId()));
        writeGridRepository.deleteAll(writeGridRepository.findAllBySurveyId(survey.getId()));
        writeQuestionRepository.deleteAll(writeQuestionRepository.findAllBySurveyId(survey.getId()));
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}