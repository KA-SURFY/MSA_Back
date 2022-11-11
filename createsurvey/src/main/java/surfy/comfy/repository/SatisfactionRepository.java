package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Satisfaction;

import java.util.List;

public interface SatisfactionRepository extends JpaRepository<Satisfaction,Long> {
    List<Satisfaction> findAllBySurvey_Id(Long surveyId);

}
