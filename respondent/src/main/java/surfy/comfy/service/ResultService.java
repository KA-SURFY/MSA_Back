package surfy.comfy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.result.*;
import surfy.comfy.entity.read.Grid;
import surfy.comfy.entity.read.Option;
import surfy.comfy.entity.read.Question;
import surfy.comfy.entity.read.Survey;
import surfy.comfy.entity.write.Answer;
import surfy.comfy.entity.write.Satisfaction;
import surfy.comfy.exception.ResourceNotFoundException;
import surfy.comfy.repository.read.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    private final ReadResultRepository readResultRepository;

    private final ReadQuestionRepository readQuestionRepository;

    private final ReadIndividualRepository readIndividualRepository;

    private final ReadAnswerRepository readAnswerRepository;

    private final ReadGridRepository readGridRepository;

    private final ReadOptionRepository readOptionRepository;

    @Cacheable(value = "result_survey", key = "#surveyId", cacheManager = "CacheManager")
    public SurveyResultResponse getSurveyById(Long surveyId){
        Survey survey = readResultRepository.findById(surveyId)
                .orElseThrow(()-> new ResourceNotFoundException("Survey not exist with id :" + surveyId));

        SurveyResultResponse surveyResultResponse = new SurveyResultResponse();
        List<Satisfaction> satisfactionList = readIndividualRepository.findAllBySurveyId(surveyId);
        double avg_satisfaction = 0.0;
        Long sum = 0L;

        for(Satisfaction sa : satisfactionList){
            sum = sum + sa.getPercent();
        }
        avg_satisfaction = sum / satisfactionList.size();

        surveyResultResponse.setSatisfaction(avg_satisfaction);
        surveyResultResponse.setId(survey.getId());
        surveyResultResponse.setContents(survey.getContents());
        surveyResultResponse.setTitle(survey.getTitle());
        surveyResultResponse.setType(survey.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            surveyResultResponse.setEnd(objectMapper.writeValueAsString(survey.getEnd()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return surveyResultResponse;
    }

    //질문 내용과 해당하는 답변 가져오기
    @Cacheable(value = "result_question", key = "#surveyId", cacheManager = "CacheManager")
    public List<QuestionAnswerResponse> getQuestionAnswerList(Long surveyId){
        List<Question> questionList = readQuestionRepository.findAllBySurveyId(surveyId);

        List<QuestionResponse> questionResponseList = questionList.stream()
                .map(p -> new QuestionResponse(p))
                .collect(Collectors.toList());

        List<QuestionAnswerResponse> questionAnswerResponseList = new ArrayList<>();

        List<Satisfaction> satisfactionList = readIndividualRepository.findAllBySurveyId(surveyId);
        int user_count = satisfactionList.size();

        for(int i=0; i<questionList.size(); i++){
            QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse();
            questionAnswerResponse.setQuestion(questionResponseList.get(i));

            // 한 질문에 대한 answerList 쫙 가져오기
            List<Answer> answers = new ArrayList<>();
            for(int j=0; j<user_count; j++) {
                List<Answer> answerList1 = readAnswerRepository.findAllByQuestionIdAndSubmit(questionResponseList.get(i).getId(), (j + 1L));
                for(Answer a : answerList1) {
                    answers.add(a);
                }

            }
            questionAnswerResponse.setAnswer(answers);
            questionAnswerResponseList.add(questionAnswerResponse);
        }

        return questionAnswerResponseList;
    }

    @Cacheable(value = "result_option", key = "#questionId", cacheManager = "CacheManager")
    // 문항별 보기에서 객관식 질문이 있으면 옵션 가져오기
    public List<Option> getOptions(Long surveyId, Long questionId){
        List<Option> optionList = readOptionRepository.findAllBySurvey_QuestionId(surveyId, questionId);
        return optionList;
    }

    @Cacheable(value = "result_grid", key = "#questionId", cacheManager = "CacheManager")
    public List<Grid> getGridOptions(Long surveyId, Long questionId){
        List<Grid> gridList = readGridRepository.findAllBySurvey_QuestionId(surveyId, questionId);
        return gridList;
    }

    // 응답자 수 가져오기
    @Cacheable(value = "result_individual", key = "#surveyId", cacheManager = "CacheManager")
    public List<RespondentsResponse> getRespondents(Long surveyId){
        List<Satisfaction> satisfactionList = readIndividualRepository.findAllBySurveyId(surveyId);
        List<RespondentsResponse> responseList = satisfactionList.stream()
                .map(p -> new RespondentsResponse(p))
                .collect(Collectors.toList());

        return responseList;
    }
}