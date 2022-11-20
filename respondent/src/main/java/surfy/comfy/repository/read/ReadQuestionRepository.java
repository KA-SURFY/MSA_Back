package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.read.Question;

import java.util.List;

public interface ReadQuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySurvey_Id(Long surveyId);
    List<Question> findAllBySurvey_id(Long surveyId);
}
