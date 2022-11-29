package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import surfy.comfy.entity.write.Grid;

import java.util.List;

public interface ReadGridRepository extends JpaRepository<Grid, Long> {
    List<Grid> findAllByQuestionId(Long QuestionId);
    List<Grid> findAllBySurveyId(Long surveyId);

    @Query("select g from Grid g where g.survey.id = ?1 and g.question.id = ?2")
    List<Grid> findAllBySurvey_QuestionId (Long surveyId, Long questionId);
}