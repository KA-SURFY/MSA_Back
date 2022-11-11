package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.result.*;
import surfy.comfy.entity.*;
import surfy.comfy.exception.ResourceNotFoundException;
import surfy.comfy.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    private final ResultRepository resultRepository;

    private final QuestionRepository questionRepository;

    private final IndividualRepository individualRepository;

    private final AnswerRespository answerRespository;

    private final GridRepository gridRepository;

    private final OptionRepository optionRepository;

    public SurveyResultResponse getSurveyById(Long surveyId){
        Survey survey = resultRepository.findById(surveyId)
                .orElseThrow(()-> new ResourceNotFoundException("Survey not exist with id :" + surveyId));

        SurveyResultResponse surveyResultResponse = new SurveyResultResponse();
        List<Satisfaction> satisfactionList = individualRepository.findAll(surveyId);
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
        surveyResultResponse.setEnd(survey.getEnd());

        return surveyResultResponse;
    }

    //질문 내용과 해당하는 답변 가져오기
    public List<QuestionAnswerResponse> getQuestionAnswerList(Long surveyId){
        List<Question> questionList = questionRepository.findAllBySurvey_id(surveyId);

        List<QuestionResponse> questionResponseList = questionList.stream()
                .map(p -> new QuestionResponse(p))
                .collect(Collectors.toList());

        List<QuestionAnswerResponse> questionAnswerResponseList = new ArrayList<>();

        List<Satisfaction> satisfactionList = individualRepository.findAll(surveyId);
        int user_count = satisfactionList.size();

        for(int i=0; i<questionList.size(); i++){
            QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse();
            questionAnswerResponse.setQuestion(questionResponseList.get(i));

            // 한 질문에 대한 answerList 쫙 가져오기
            List<Answer> answers = new ArrayList<>();
            for(int j=0; j<user_count; j++) {
                List<Answer> answerList1 = answerRespository.getAnswerByQuestion_Submit_Id(questionResponseList.get(i).getId(), (j + 1L));
                for(Answer a : answerList1) {
                    answers.add(a);
                }

            }
            questionAnswerResponse.setAnswer(answers);
            questionAnswerResponseList.add(questionAnswerResponse);
        }

        return questionAnswerResponseList;
    }

    // 문항별 보기에서 객관식 질문이 있으면 옵션 가져오기
    public List<Option> getOptions(Long surveyId, Long questionId){
        List<Option> optionList = optionRepository.findAllBySurvey_Question_Id(surveyId, questionId);
        return optionList;
    }

    public List<Grid> getGridOptions(Long surveyId, Long questionId){
        List<Grid> gridList = gridRepository.findAllBySurvey_Question_Id(surveyId, questionId);
        return gridList;
    }

    // 응답자 수 가져오기
    public List<RespondentsResponse> getRespondents(Long surveyId){
        List<Satisfaction> satisfactionList = individualRepository.findAll(surveyId);
        List<RespondentsResponse> responseList = satisfactionList.stream()
                .map(p -> new RespondentsResponse(p))
                .collect(Collectors.toList());

        return responseList;
    }
}