package surfy.comfy.data.survey;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.repository.read.ReadAnswerRepository;

import java.util.List;

@Data
public class GetAnswerResponse {
    private List<Answer> answers;
    private ReadAnswerRepository readAnswerRepository;

    @Autowired
    public void callreadrepo(ReadAnswerRepository readAnswerRepository) {
        this.readAnswerRepository=readAnswerRepository;
    }
    public GetAnswerResponse(Long questionId){
        this.answers=readAnswerRepository.findAllByQuestionId(questionId);
    }

}
