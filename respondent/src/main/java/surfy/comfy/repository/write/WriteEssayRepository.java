package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Essay;

import java.util.List;
import java.util.Optional;

public interface WriteEssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findAllBySurveyId(Long SurveyId);
    List<Essay> findAllByQuestionId(Long QuestionId);
    Optional<Essay> findById(Long Id);
}
