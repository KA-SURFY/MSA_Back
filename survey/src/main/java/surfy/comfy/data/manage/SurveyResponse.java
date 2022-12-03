package surfy.comfy.data.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.type.SurveyType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SurveyResponse {

    private String title;
    private String content;
    private LocalDateTime start;
    private Long surveyId;
    private SurveyType status; // 설문 상태
    private Long thumbnail;
    private int satisfaction;

    public SurveyResponse(Survey survey){
        this.title = survey.getTitle();
        this.surveyId = survey.getId();
        this.content = survey.getContents();
        this.status = survey.getStatus();
        this.start = survey.getStart();
        this.thumbnail= survey.getThumbnail();
    }


}