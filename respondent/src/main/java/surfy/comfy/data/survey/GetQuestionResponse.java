package surfy.comfy.data.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import net.minidev.json.parser.JSONParser;
import surfy.comfy.entity.read.Question;
@Data
public class GetQuestionResponse {
    private Long id;
    private String ques;
    private GetQuestionTypeResponse type;
    public GetQuestionResponse(){}
}