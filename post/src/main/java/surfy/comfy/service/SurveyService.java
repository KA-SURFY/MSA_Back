package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.entity.read.Survey;
import surfy.comfy.repository.read.ReadSurveyRepository;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {
    private ReadSurveyRepository readSurveyRepository;
    private Logger logger= LoggerFactory.getLogger(SurveyService.class); 
    //Survey id - 설문지 가져오기
    @Transactional
    public SurveyResponse getSurvey(Long surveyId){
        logger.info("[getSurvey] - surveyId: {}",surveyId);
        Survey survey = readSurveyRepository.findById(surveyId).get();
        SurveyResponse response =new SurveyResponse(survey);
        return response;
    }
}