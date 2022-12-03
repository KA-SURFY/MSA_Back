package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Grid;

import java.util.List;

@Repository
public interface ReadGridRepository extends JpaRepository<Grid, Long> {
    List<Grid> findAllByQuestionId(Long QuestionId);
    List<Grid> findAllBySurveyId(Long surveyId);

}