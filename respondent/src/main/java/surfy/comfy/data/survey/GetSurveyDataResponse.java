package surfy.comfy.data.survey;

import lombok.Data;
import lombok.SneakyThrows;
import surfy.comfy.entity.read.Survey;
import surfy.comfy.entity.write.Answer;
import surfy.comfy.type.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetSurveyDataResponse {
    private String intro0;
    private String intro1;
    private List<GetQuestionResponse> ques_list;
    private List<GetOptionResponse> ans_list;
    private List<GetGridResponse> choice_list;
    private Long satis;
    private String status;
    private String start;
    private String end;
    public GetSurveyDataResponse(){}
}