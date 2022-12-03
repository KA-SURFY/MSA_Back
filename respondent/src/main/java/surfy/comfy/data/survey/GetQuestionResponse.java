package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetQuestionResponse {
    private Long id;
    private String ques;
    private GetQuestionTypeResponse type;
    public GetQuestionResponse(){}
}