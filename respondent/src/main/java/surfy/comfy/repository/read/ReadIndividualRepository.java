package surfy.comfy.repository.read;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Satisfaction;

import java.util.List;

@Repository
public interface ReadIndividualRepository extends JpaRepository<Satisfaction, Long> {

    List<Satisfaction> findAllBySurveyId(Long surveyId);
}
