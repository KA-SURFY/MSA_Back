package surfy.comfy.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Satisfaction;

import java.util.List;

@Repository
public interface IndividualRepository extends JpaRepository<Satisfaction, Long> {

    @Query("select s from Satisfaction s where s.survey.id = ?1")
    List<Satisfaction> findAll(Long surveyId);
}
