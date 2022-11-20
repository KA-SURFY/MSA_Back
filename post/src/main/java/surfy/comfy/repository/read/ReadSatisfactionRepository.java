package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.read.Satisfaction;

import java.math.BigInteger;
import java.util.List;

public interface ReadSatisfactionRepository extends JpaRepository<Satisfaction,Long> {
    List<Satisfaction> findAllBySurvey_Id(Long surveyId);


}
