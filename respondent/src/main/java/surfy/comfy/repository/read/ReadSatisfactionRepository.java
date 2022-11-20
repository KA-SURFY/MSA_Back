package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Satisfaction;

import java.util.List;

public interface ReadSatisfactionRepository extends JpaRepository<Satisfaction,Long> {
    List<Satisfaction> findAllBySurveyId(Long surveyId);

}
