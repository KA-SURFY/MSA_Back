package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.GetChoiceAnswerResponse;
import surfy.comfy.data.survey.GetQuestionResponse;
import surfy.comfy.data.survey.GetQuestionTypeResponse;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.type.QuestionType;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRespository answerRepository;
    private final SatisfactionRepository satisfactionRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final GridRepository gridRepository;
    private final EssayRepository essayRepository;
    private final SliderRepository sliderRepository;
    @Transactional
    public void CreateAnswerDB(GetSurveyDataResponse data, Survey survey){
        List<GetQuestionResponse> ques_list=data.getQues_list();
        List<Question> Ques_list=questionRepository.findAllBySurvey_Id(survey.getId());

        Long submitid;
        if(answerRepository.getAnswerBySurveyId(survey.getId()).size() == 0){
            submitid=1L;
        }else {
            List<Answer> survey_ans_list = answerRepository.getAnswerBySurveyId(survey.getId());
            submitid=survey_ans_list.get(survey_ans_list.size()-1).getSubmit()+1L;
        }
        System.out.println(submitid);

        int t=0;
        for(int i=0;i<Ques_list.size();i++){
            Question question= Ques_list.get(i);

            if(question.getQuestionType()== QuestionType.만족도){
                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);
                answer.setSubmit(submitid);

                Satisfaction satisfaction=new Satisfaction();
                satisfaction.setQuestion(question);
                satisfaction.setPercent(data.getSatis());
                satisfaction.setSurvey(survey);

                satisfactionRepository.save(satisfaction);
                answer.setSatisfaction(satisfaction);
                answerRepository.save(answer);
                continue;
            }

            List<Grid> gridList=gridRepository.findAllByQuestion_Id(question.getId());
            List<Option> optionList=optionRepository.findAllByQuestion_Id(question.getId());

            GetQuestionResponse ques_item=ques_list.get(i);
            t++;

            GetQuestionTypeResponse type=ques_item.getType();

            if(type.getId()==1 || type.getId()==2){ //객관식 답변
                List<GetChoiceAnswerResponse> ans_list = type.getChoice_value();
                for(int k=0;k<ans_list.size();k++){
                    GetChoiceAnswerResponse ans_item=ans_list.get(k);

                    Answer answer=new Answer();
                    answer.setSurvey(survey);
                    answer.setQuestion(question);
                    answer.setSubmit(submitid);

                    if(type.getId()==2){ //객관식 Grid 답변
                        Option select_opt=optionList.stream().filter(s->s.getId()==ans_item.getRootid()).findFirst().get();
                        Grid select_grid=gridList.stream().filter(s->s.getId()==ans_item.getSelectid()).findFirst().get();
                        answer.setGrid(select_grid);
                        answer.setOption(select_opt);
                    }
                    else{
                        Option select_opt=optionList.stream().filter(s->s.getId()==ans_item.getSelectid()).findFirst().get();
                        answer.setOption(select_opt);
                    }
                    answerRepository.save(answer);
                }
            }
            else if(type.getId()==3){ //주관식
                Essay essay=new Essay();
                essay.setQuestion(question);
                essay.setSurvey(survey);
                essay.setContents(type.getAnswer());
                essayRepository.save(essay);

                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);
                answer.setSubmit(submitid);
                answer.setEssay(essay);
                answerRepository.save(answer);
            }
            else if(type.getId()==4){
                Slider slider=new Slider();
                slider.setQuestion(question);
                slider.setSurvey(survey);
                slider.setValue(Long.parseLong(type.getAnswer()));
                sliderRepository.save(slider);

                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);
                answer.setSubmit(submitid);
                answer.setSlider(slider);
                answerRepository.save(answer);
            }
        }
    }
}