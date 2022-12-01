package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.repository.read.ReadAnswerRepository;
import surfy.comfy.repository.read.ReadSurveyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadSurveyService {

    private final ReadSurveyRepository readSurveyRepository;
    private final ReadAnswerRepository readAnswerRepository;
    @Cacheable(value = "survey", key = "#surveyId", cacheManager = "CacheManager")
    @SneakyThrows
    @Transactional
    public GetSurveyDataResponse getSurveydata(Long surveyId){
        Survey survey= readSurveyRepository.findSurveysById(surveyId);
        GetSurveyDataResponse ret = new GetSurveyDataResponse(survey,false,null,null);
        return ret;
    }

    @Cacheable(value = "answer", key = "#surveyId+':'+#submitId", cacheManager = "CacheManager")
    @SneakyThrows
    @Transactional
    public GetSurveyDataResponse getAnswerdata(Long surveyId,Boolean loadAnswer,Long submitid){
        Survey survey= readSurveyRepository.findSurveysById(surveyId);
        GetSurveyDataResponse ret = new GetSurveyDataResponse(survey,loadAnswer,submitid,readAnswerRepository);
        return ret;
    }
}
