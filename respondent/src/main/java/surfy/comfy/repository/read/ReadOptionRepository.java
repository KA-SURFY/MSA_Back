package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import surfy.comfy.entity.read.Option;

import java.util.List;

public interface ReadOptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByQuestionId(Long QuestionId);
    List<Option> findAllBySurveyId(Long surveyId);

}