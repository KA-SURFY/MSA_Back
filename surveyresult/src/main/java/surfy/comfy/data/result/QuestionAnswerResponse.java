package surfy.comfy.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Answer;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerResponse {

    private QuestionResponse question;
    private List<Answer> answer;

}
