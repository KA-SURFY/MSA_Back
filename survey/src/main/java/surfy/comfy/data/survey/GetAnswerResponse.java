package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.repository.read.ReadAnswerRepository;

import java.util.List;

@Data
public class GetAnswerResponse {
    List<Answer> answers;
    ReadAnswerRepository writeAnswerRepository;

    public GetAnswerResponse(Long questionId){
        this.answers=writeAnswerRepository.findAllByQuestionId(questionId);
    }
}
