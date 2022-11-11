package surfy.comfy.data.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DeleteSurveyResponse {
    private Long surveyId;
    private Long memberId;
}
