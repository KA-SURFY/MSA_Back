package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Question;

import java.util.List;

public interface WriteQuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySurveyId(Long surveyId);
}
