package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PostSurveyResponse {
    private Long surveyId;
    private Long memberId;
}
