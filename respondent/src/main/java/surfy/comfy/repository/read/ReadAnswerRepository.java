package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Answer;

import java.util.List;

@Repository
public interface ReadAnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllBySurveyId(Long SurveyId);

    List<Answer> findAllByQuestionId(Long questionId);

    List<Answer> findAllByQuestionIdAndSubmit(Long questionId, Long submitId);
}