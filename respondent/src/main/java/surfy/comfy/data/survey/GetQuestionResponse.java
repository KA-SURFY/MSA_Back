package surfy.comfy.data.survey;

import lombok.Data;

@Data
public class GetQuestionResponse {
    private Long id;
    private String ques;
    private GetQuestionTypeResponse type;
    public GetQuestionResponse(){}
}