package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import surfy.comfy.entity.read.Grid;

import java.util.List;

public interface ReadGridRepository extends JpaRepository<Grid, Long> {
    List<Grid> findAllByQuestionId(Long QuestionId);
    List<Grid> findAllBySurveyId(Long surveyId);

}