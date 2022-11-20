package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.write.Answer;
import surfy.comfy.type.QuestionType;

@Data
public class GetChoiceAnswerResponse {
    private Long id;
    private Long temid;
    private Long rootid;
    private Long selectid;
    public GetChoiceAnswerResponse(){}
    public GetChoiceAnswerResponse(Answer answer, QuestionType questionType){
        this.id=answer.getId();

        if (questionType == QuestionType.객관식_단일 ||
                questionType==QuestionType.객관식_중복) {
            this.rootid=answer.getQuestionId();
            this.selectid=answer.getOptionId();
        }
        else{
            this.selectid=answer.getGridId();
            this.rootid=answer.getOptionId();
        }
        this.temid=1L;
    }
}