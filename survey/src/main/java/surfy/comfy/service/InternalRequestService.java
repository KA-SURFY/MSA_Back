package surfy.comfy.service;

import org.springframework.stereotype.Service;

@Service
public class InternalRequestService {
    private String url="http://172.16.1.245";

    private final InternalApiService<Boolean> checkbool;

    public InternalRequestService(InternalApiService<Boolean> checkbool) {
        this.checkbool = checkbool;
    }

    public Boolean DeleteSurvey_Respondent(Long surveyId){
        return checkbool.get(url+"/api/deletesurvey/"+surveyId).getBody();
    }
}