package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.read.Satisfaction;

import java.util.List;
import java.util.Optional;

public interface ReadSatisfactionRepository extends JpaRepository<Satisfaction,Long> {
    List<Satisfaction> findAllBySurveyId(Long surveyId);
    Optional<Satisfaction> findById(Long Id);

}
