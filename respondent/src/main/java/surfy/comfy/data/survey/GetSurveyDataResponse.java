package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
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