package surfy.comfy.data.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import net.minidev.json.parser.JSONParser;
import surfy.comfy.entity.write.Question;
import surfy.comfy.repository.read.ReadAnswerRepository;

@Data
@AllArgsConstructor
public class GetQuestionResponse {
    private Long id;
    private String ques;
    private GetQuestionTypeResponse type;
    public GetQuestionResponse(){}
}