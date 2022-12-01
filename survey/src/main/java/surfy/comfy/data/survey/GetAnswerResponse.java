package surfy.comfy.data.survey;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.repository.read.ReadAnswerRepository;

import java.util.List;

@Data
public class GetAnswerResponse {
    private List<Answer> answers;
    private ReadAnswerRepository readAnswerRepository=new GetRepo().getReadAnswerRepository();

    public GetAnswerResponse(Long questionId){
        this.answers=readAnswerRepository.findAllByQuestionId(questionId);
    }
}
@Service
@Data
class GetRepo{
    @Autowired
    private ReadAnswerRepository readAnswerRepository;
    public GetRepo(){}
}
