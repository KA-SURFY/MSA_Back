package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class GetQuestionTypeResponse {
    private Long id;
    private String name;
    private Boolean choice_type;
    private List<GetChoiceAnswerResponse> choice_value;
    private String answer;
    public GetQuestionTypeResponse(){}
}