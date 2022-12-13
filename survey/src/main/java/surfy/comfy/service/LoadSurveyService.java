package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.*;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.entity.write.Grid;
import surfy.comfy.entity.write.Option;
import surfy.comfy.entity.write.Question;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.repository.read.*;
import surfy.comfy.repository.write.WriteGridRepository;
import surfy.comfy.repository.write.WriteOptionRepository;
import surfy.comfy.repository.write.WriteQuestionRepository;
import surfy.comfy.repository.write.WriteSurveyRepository;
import surfy.comfy.type.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadSurveyService {

    private final ReadSurveyRepository readSurveyRepository;
    private final ReadAnswerRepository readAnswerRepository;

    private final ReadQuestionRepository readQuestionRepository;
    private final ReadOptionRepository readOptionRepository;
    private final ReadGridRepository readGridRepository;
    private final ReadSatisfactionRepository readSatisfactionRepository;
    private final ReadSliderRepository readSliderRepository;
    private final ReadEssayRepository readEssayRepository;


    @SneakyThrows
    @Transactional
    @Cacheable(value = "survey", key = "#surveyId.toString().concat(':').concat(#submitId.toString())", cacheManager = "CacheManager")
    public GetSurveyDataResponse getAnswerData(Long surveyId,Long submitid){

        return getData(surveyId,submitid);
    }
    @SneakyThrows
    @Transactional
    @Cacheable(value = "survey", key = "#surveyId", cacheManager = "CacheManager")
    public GetSurveyDataResponse getSurveyData(Long surveyId){
        return getData(surveyId,null);
    }
    @SneakyThrows
    @Transactional
    public GetSurveyDataResponse getData(Long surveyId,Long submitid){
        Survey survey= readSurveyRepository.findSurveysById(surveyId);

        GetSurveyDataResponse getSurveyDataResponse=new GetSurveyDataResponse();
        getSurveyDataResponse.setStatus(survey.getStatus().toString());
        getSurveyDataResponse.setStart(String.valueOf(survey.getStart()));
        getSurveyDataResponse.setEnd(String.valueOf(survey.getEnd()));

        getSurveyDataResponse.setIntro0(survey.getTitle());
        getSurveyDataResponse.setIntro1(survey.getContents());

        List<GetQuestionResponse> ques_list=new ArrayList<>();
        List<GetOptionResponse> ans_list=new ArrayList<>();
        List<GetGridResponse> choice_list=new ArrayList<>();

        List<Question> read_ques=readQuestionRepository.findAllBySurveyId(surveyId);

        for(int i=0;i<read_ques.size();i++){
            List<Option> read_option=readOptionRepository.findAllByQuestionId(read_ques.get(i).getId());
            List<Grid> read_grid=readGridRepository.findAllByQuestionId(read_ques.get(i).getId());

            if(read_ques.get(i).getQuestionType()== QuestionType.만족도){
                try{
                    if(submitid!=null){
                        List<Answer> answers=readAnswerRepository.findAllByQuestionId(read_ques.get(i).getId());
                        for(int k=0;k<answers.size();k++){
                            if(answers.get(k).getSubmit()==submitid) {
                                getSurveyDataResponse.setSatis(readSatisfactionRepository.findById(answers.get(k).getSatisfactionId()).get().getPercent());
                                break;
                            }
                        }
                    }
                }
                catch(Exception e){
                    getSurveyDataResponse.setSatis(null);
                }
                continue;
            }
            GetQuestionResponse getQuestionResponse=new GetQuestionResponse();
            getQuestionResponse.setId(read_ques.get(i).getId());
            getQuestionResponse.setQues(read_ques.get(i).getContents());
            getQuestionResponse.setType(getQuestionType(read_ques.get(i),submitid));
            ques_list.add(getQuestionResponse);

            for(int k=0;k<read_option.size();k++){
                GetOptionResponse opt = new GetOptionResponse();

                opt.setId(read_option.get(k).getId());
                opt.setTemid(0L);
                opt.setRootid(read_ques.get(i).getId());
                opt.setValue(read_option.get(k).getContents());

                int t=0;
                for(;t<ans_list.size();t++){
                    if(ans_list.get(t).getId()>opt.getId()){
                        ans_list.add(t,opt);
                        break;
                    }
                }
                if(t==ans_list.size()){
                    ans_list.add(opt);
                }
            }
            for(int k=0;k<read_grid.size();k++){
                GetGridResponse grid = new GetGridResponse();

                grid.setId(read_grid.get(k).getId());
                grid.setTemid(0L);
                grid.setRootid(read_ques.get(i).getId());
                grid.setValue(read_grid.get(k).getContents());

                int t=0;
                for(;t<choice_list.size();t++){
                    if(choice_list.get(t).getId()>grid.getId()){
                        choice_list.add(t,grid);
                        break;
                    }
                }
                if(t==choice_list.size()){
                    choice_list.add(grid);
                }
            }
        }
        getSurveyDataResponse.setQues_list(ques_list);
        getSurveyDataResponse.setAns_list(ans_list);
        getSurveyDataResponse.setChoice_list(choice_list);

        return getSurveyDataResponse;
    }
    @SneakyThrows
    @Transactional
    public GetQuestionTypeResponse getQuestionType(Question question, Long submitid){
        GetQuestionTypeResponse getQuestionTypeResponse=new GetQuestionTypeResponse();
        if(question.getQuestionType()== QuestionType.객관식_단일 ||
                question.getQuestionType()== QuestionType.객관식_중복){
            getQuestionTypeResponse.setId(1L);
            getQuestionTypeResponse.setName("객관식");
            if(question.getQuestionType()==QuestionType.객관식_단일){
                getQuestionTypeResponse.setChoice_type(false);
            }
            else{
                getQuestionTypeResponse.setChoice_type(true);
            }
            List<GetChoiceAnswerResponse> choice_value=new ArrayList<>();
            if(submitid!=null){
                List<Answer> answers= readAnswerRepository.findAllByQuestionId(question.getId());
                for(int i=0;i<answers.size();i++){
                    if(answers.get(i).getSubmit()==submitid){
                        choice_value.add(new GetChoiceAnswerResponse(answers.get(i),question.getQuestionType()));
                    }
                }
            }
            getQuestionTypeResponse.setChoice_value(choice_value);

        }
        else if(question.getQuestionType()== QuestionType.객관식_그리드_단일 ||
                question.getQuestionType()== QuestionType.객관식_그리드_중복){
            getQuestionTypeResponse.setId(2L);
            getQuestionTypeResponse.setName("객관식 Grid");
            if(question.getQuestionType()==QuestionType.객관식_그리드_단일){
                getQuestionTypeResponse.setChoice_type(false);
            }
            else{
                getQuestionTypeResponse.setChoice_type(true);
            }
            List<GetChoiceAnswerResponse> choice_value=new ArrayList<>();
            if(submitid!=null){
                List<Answer> answers= readAnswerRepository.findAllByQuestionId(question.getId());
                for(int i=0;i<answers.size();i++){
                    if(answers.get(i).getSubmit()==submitid){
                        choice_value.add(new GetChoiceAnswerResponse(answers.get(i),question.getQuestionType()));
                    }
                }
            }
            getQuestionTypeResponse.setChoice_value(choice_value);
        }
        else if(question.getQuestionType()==QuestionType.주관식){
            getQuestionTypeResponse.setId(3L);
            getQuestionTypeResponse.setName("주관식");
            try{
                if(submitid!=null){
                    List<Answer> answers= readAnswerRepository.findAllByQuestionId(question.getId());
                    for(int i=0;i<answers.size();i++){
                        if(answers.get(i).getSubmit()==submitid) {
                            getQuestionTypeResponse.setAnswer(readEssayRepository.findById(answers.get(i).getEssayId()).get().getContents());
                            break;
                        }
                    }
                }
            }
            catch(Exception e){
                getQuestionTypeResponse.setAnswer("");
            }
        }
        else if(question.getQuestionType()==QuestionType.슬라이더){
            getQuestionTypeResponse.setId(4L);
            getQuestionTypeResponse.setName("슬라이더");
            try{
                if(submitid!=null){
                    List<Answer> answers= readAnswerRepository.findAllByQuestionId(question.getId());
                    for(int i=0;i<answers.size();i++){
                        if(answers.get(i).getSubmit()==submitid) {
                            getQuestionTypeResponse.setAnswer(readSliderRepository.findById(answers.get(i).getSliderId()).get().getValue().toString());
                            break;
                        }
                    }
                }
            }
            catch(Exception e){
                getQuestionTypeResponse.setAnswer("0");
            }
        }
        return getQuestionTypeResponse;
    }
}
