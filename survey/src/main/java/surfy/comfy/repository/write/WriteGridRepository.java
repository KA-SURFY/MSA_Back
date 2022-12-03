package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Grid;

import java.util.List;

@Repository
public interface WriteGridRepository extends JpaRepository<Grid, Long> {
    List<Grid> findAllByQuestionId(Long QuestionId);
    List<Grid> findAllBySurveyId(Long surveyId);
}