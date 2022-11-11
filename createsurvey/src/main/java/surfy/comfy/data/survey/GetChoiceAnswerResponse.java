package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.Answer;
import surfy.comfy.type.QuestionType;
@Data
public class GetChoiceAnswerResponse {
    private Long id;
    private Long temid;
    private Long rootid;
    private Long selectid;
    public GetChoiceAnswerResponse(){}
    public GetChoiceAnswerResponse(Answer answer){
        this.id=answer.getId();

        if (answer.getQuestion().getQuestionType() == QuestionType.객관식_단일 ||
                answer.getQuestion().getQuestionType()==QuestionType.객관식_중복) {
            this.rootid=answer.getQuestion().getId();
            this.selectid=answer.getOption().getId();
        }
        else{
            this.selectid=answer.getGrid().getId();
            this.rootid=answer.getOption().getId();
        }
        this.temid=1L;
    }
}