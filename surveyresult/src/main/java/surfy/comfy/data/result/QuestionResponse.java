package surfy.comfy.data.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Question;
import surfy.comfy.type.QuestionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {

    private Long id;
    private String contents;

    private Long surveyId;
    private QuestionType type;
    public QuestionResponse(Question question){
        this.surveyId = question.getSurvey().getId();
        this.id = question.getId();
        this.contents = question.getContents();
        this.type = question.getQuestionType();
    }
}