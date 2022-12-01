package surfy.comfy.data.survey;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.repository.read.ReadAnswerRepository;

import java.util.List;


@Service
public class GetAnswerResponse {

    private ReadAnswerRepository readAnswerRepository;

    public GetAnswerResponse(){}

    @Autowired
    public void GetRepo(ReadAnswerRepository repo){
        this.readAnswerRepository=repo;
    }
    public List<Answer> GetAnswers(Long questionId){
        return readAnswerRepository.findAllByQuestionId(questionId);
    }
}
