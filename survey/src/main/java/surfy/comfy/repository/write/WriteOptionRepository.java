package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Option;

import java.util.List;

@Repository
public interface WriteOptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByQuestionId(Long QuestionId);
    List<Option> findAllBySurveyId(Long surveyId);

}