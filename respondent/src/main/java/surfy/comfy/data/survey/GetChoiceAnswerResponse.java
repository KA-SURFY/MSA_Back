package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import surfy.comfy.entity.write.Answer;
import surfy.comfy.type.QuestionType;

@Data
@AllArgsConstructor
public class GetChoiceAnswerResponse {
    private Long id;
    private Long temid;
    private Long rootid;
    private Long selectid;
    public GetChoiceAnswerResponse(){}
}