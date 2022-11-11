package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.PostSurveyResponse;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.type.SurveyType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final GridRepository gridRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;

    /**
     * minseo
     * 마음에 드는 설문지 임시 저장
     * @param surveyId
     * @param memberId
     * @return
     */
    @Transactional
    public PostSurveyResponse makeSurvey(Long surveyId, Long memberId){
        List<Question> questionList=questionRepository.findAllBySurvey_Id(surveyId);
        List<Grid> gridList=gridRepository.findAllBySurvey_Id(surveyId);
        List<Option> optionList=optionRepository.findAllBySurvey_Id(surveyId);
        Survey createdSurvey=surveyRepository.findById(surveyId).get(); // 커뮤니티 게시글에서 마음에 들어서 만드려고 하는 설문지

        // 내 설문지로 생성
        Survey survey=new Survey();
        survey.setContents(createdSurvey.getContents());
        survey.setTitle(createdSurvey.getTitle());
        survey.setMember(memberRepository.findById(memberId).get());
        survey.setStatus(SurveyType.notFinish);
        Long newSurveyId=surveyRepository.save(survey).getId();

        for(Question q:questionList){
            Question question=new Question();
            question.setContents(q.getContents());
            question.setSurvey(surveyRepository.findById(newSurveyId).get());
            question.setQuestionType(q.getQuestionType());
            Long newQuestionId=questionRepository.save(question).getId();

            for(Grid g:gridList){
                Grid grid=new Grid();
                grid.setContents(g.getContents());
                grid.setQuestion(questionRepository.findById(newQuestionId).get());
                grid.setSurvey(surveyRepository.findById(newSurveyId).get());

                gridRepository.save(grid);
            }

            for(Option o:optionList){
                Option option=new Option();
                option.setContents(o.getContents());
                option.setSurvey(surveyRepository.findById(newSurveyId).get());
                option.setQuestion(questionRepository.findById(newQuestionId).get());

                optionRepository.save(option);
            }
        }



        return new PostSurveyResponse(newSurveyId,memberId);

    }
}