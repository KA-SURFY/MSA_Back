package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.GetChoiceAnswerResponse;
import surfy.comfy.data.survey.GetQuestionResponse;
import surfy.comfy.data.survey.GetQuestionTypeResponse;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.read.Grid;
import surfy.comfy.entity.read.Option;
import surfy.comfy.entity.read.Question;
import surfy.comfy.entity.read.Survey;
import surfy.comfy.entity.write.Answer;
import surfy.comfy.entity.write.Essay;
import surfy.comfy.entity.write.Satisfaction;
import surfy.comfy.entity.write.Slider;
import surfy.comfy.repository.read.*;
import surfy.comfy.repository.write.WriteAnswerRespository;
import surfy.comfy.repository.write.WriteEssayRepository;
import surfy.comfy.repository.write.WriteSatisfactionRepository;
import surfy.comfy.repository.write.WriteSliderRepository;
import surfy.comfy.type.QuestionType;

import javax.persistence.EntityManager;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AnswerService {
    private final EntityManager em;
    private final ReadAnswerRepository readAnswerRepository;
    private final WriteAnswerRespository writeAnswerRepository;
    private final WriteSatisfactionRepository writeSatisfactionRepository;
    private final ReadSatisfactionRepository readSatisfactionRepository;
    private final ReadQuestionRepository readQuestionRepository;
    private final ReadOptionRepository readOptionRepository;
    private final ReadGridRepository readGridRepository;
    private final WriteEssayRepository writeEssayRepository;
    private final WriteSliderRepository writeSliderRepository;
    private final ReadEssayRepository readEssayRepository;
    private final ReadSliderRepository readSliderRepository;

    @Transactional
    public void DeleteRespondentDB(Long surveyId){
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        List<Answer> ans_list= writeAnswerRepository.findAllBySurveyId(surveyId);
        for(int i=0;i<ans_list.size();i++){
            writeEssayRepository.delete(ans_list.get(i).getEssay());
            writeSliderRepository.delete(ans_list.get(i).getSlider());
            writeSatisfactionRepository.delete(ans_list.get(i).getSatisfaction());
        }
        writeAnswerRepository.deleteAll(ans_list);

        writeEssayRepository.flush();
        writeSliderRepository.flush();
        writeSatisfactionRepository.flush();
        writeAnswerRepository.flush();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
    @Transactional
    public void CreateAnswerDB(GetSurveyDataResponse data, Survey survey){
        List<GetQuestionResponse> ques_list=data.getQues_list();
        List<Question> Ques_list= readQuestionRepository.findAllBySurveyId(survey.getId());

        Long submitid;
        if(readAnswerRepository.findAllBySurveyId(survey.getId()).size() == 0){
            submitid=1L;
        }else {
            List<Answer> survey_ans_list = readAnswerRepository.findAllBySurveyId(survey.getId());
            submitid=survey_ans_list.get(survey_ans_list.size()-1).getSubmit()+1L;
        }
        System.out.println(submitid);

        int t=0;
        for(int i=0;i<Ques_list.size();i++){
            Question question= Ques_list.get(i);

            if(question.getQuestionType()== QuestionType.만족도){
                Answer answer=new Answer();
                answer.setSurveyId(survey.getId());
                answer.setQuestionId(question.getId());
                answer.setSubmit(submitid);

                writeAnswerRepository.save(answer);
                Satisfaction satisfaction=new Satisfaction();
                satisfaction.setQuestionId(question.getId());
                satisfaction.setPercent(data.getSatis());
                satisfaction.setSurveyId(survey.getId());

                writeSatisfactionRepository.save(satisfaction);
                answer.setSatisfaction(satisfaction);
                continue;
            }

            List<Grid> gridList= readGridRepository.findAllByQuestionId(question.getId());
            List<Option> optionList= readOptionRepository.findAllByQuestionId(question.getId());

            GetQuestionResponse ques_item=ques_list.get(i);
            t++;

            GetQuestionTypeResponse type=ques_item.getType();

            if(type.getId()==1 || type.getId()==2){ //객관식 답변
                List<GetChoiceAnswerResponse> ans_list = type.getChoice_value();
                for(int k=0;k<ans_list.size();k++){
                    GetChoiceAnswerResponse ans_item=ans_list.get(k);

                    Answer answer=new Answer();
                    answer.setSurveyId(survey.getId());
                    answer.setQuestionId(question.getId());
                    answer.setSubmit(submitid);

                    writeAnswerRepository.save(answer);

                    if(type.getId()==2){ //객관식 Grid 답변
                        Option select_opt=optionList.stream().filter(s->s.getId()==ans_item.getRootid()).findFirst().get();
                        Grid select_grid=gridList.stream().filter(s->s.getId()==ans_item.getSelectid()).findFirst().get();
                        answer.setGridId(select_grid.getId());
                        answer.setOptionId(select_opt.getId());
                    }
                    else{
                        Option select_opt=optionList.stream().filter(s->s.getId()==ans_item.getSelectid()).findFirst().get();
                        answer.setOptionId(select_opt.getId());
                    }
                }
            }
            else if(type.getId()==3){ //주관식
                Essay essay=new Essay();
                essay.setQuestionId(question.getId());
                essay.setSurveyId(survey.getId());
                essay.setContents(type.getAnswer());
                writeEssayRepository.save(essay);

                Answer answer=new Answer();
                answer.setSurveyId(survey.getId());
                answer.setQuestionId(question.getId());
                answer.setSubmit(submitid);
                answer.setEssay(essay);
                writeAnswerRepository.save(answer);
            }
            else if(type.getId()==4){
                Slider slider=new Slider();
                slider.setQuestionId(question.getId());
                slider.setSurveyId(survey.getId());
                slider.setValue(Long.parseLong(type.getAnswer()));
                writeSliderRepository.save(slider);

                Answer answer=new Answer();
                answer.setSurveyId(survey.getId());
                answer.setQuestionId(question.getId());
                answer.setSubmit(submitid);
                answer.setSlider(slider);
                writeAnswerRepository.save(answer);
            }
        }
    }
}