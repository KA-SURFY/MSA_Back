package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Answer;

import java.util.List;

@Repository
public interface AnswerRespository extends JpaRepository<Answer, Long> {

    @Query("select a from Answer a where a.survey.id = ?1")
    List<Answer> getAnswerBySurveyId(Long SurveyId);

    @Query("select a from Answer a where a.question.id = ?1 and a.submit = ?2")
    List<Answer> getAnswerByQuestion_Submit_Id(Long questionId, Long submitId);
}