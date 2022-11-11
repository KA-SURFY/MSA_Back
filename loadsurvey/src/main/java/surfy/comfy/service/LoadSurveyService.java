package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.Survey;
import surfy.comfy.repository.SurveyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadSurveyService {

    private final SurveyRepository surveyRepository;

    @SneakyThrows
    @Transactional
    public GetSurveyDataResponse getSurveydata(Long surveyId,Boolean loadAnswer,Long submitid){
        Survey survey= surveyRepository.findSurveysById(surveyId);
        GetSurveyDataResponse ret = new GetSurveyDataResponse(survey,loadAnswer,submitid);
        return ret;
    }
}
