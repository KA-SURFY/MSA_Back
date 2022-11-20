package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Satisfaction;

import java.util.List;

public interface WriteSatisfactionRepository extends JpaRepository<Satisfaction,Long> {
    List<Satisfaction> findAllBySurveyId(Long surveyId);

}
