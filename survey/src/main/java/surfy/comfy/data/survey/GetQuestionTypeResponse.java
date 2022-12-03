package surfy.comfy.data.survey;

import lombok.Data;
import lombok.SneakyThrows;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.entity.write.Question;
import surfy.comfy.repository.read.ReadAnswerRepository;
import surfy.comfy.type.QuestionType;

import java.util.ArrayList;
import java.util.List;


@Data
public class GetQuestionTypeResponse {
    private Long id;
    private String name;
    private Boolean choice_type;
    private List<GetChoiceAnswerResponse> choice_value;
    private String answer;
    public GetQuestionTypeResponse(){}
}