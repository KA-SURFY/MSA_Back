package surfy.comfy.data.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import net.minidev.json.parser.JSONParser;
import surfy.comfy.entity.write.Question;
import surfy.comfy.repository.read.ReadAnswerRepository;

@Data
public class GetQuestionResponse {
    private Long id;
    private String ques;
    private GetQuestionTypeResponse type;
    public GetQuestionResponse(){}
    @SneakyThrows
    public GetQuestionResponse(Question question, Boolean loadAnswer, Long submitid, ReadAnswerRepository readAnswerRepository){

        this.id=question.getId();
        this.ques=question.getContents();
        this.type=new GetQuestionTypeResponse(question,loadAnswer,submitid,readAnswerRepository);

    }
}