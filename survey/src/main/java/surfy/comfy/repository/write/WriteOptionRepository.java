package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import surfy.comfy.entity.write.Option;

import java.util.List;

public interface WriteOptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByQuestion_Id(Long QuestionId);
    List<Option> findAllBySurvey_Id(Long surveyId);

    @Query("select o from Option o where o.survey.id = ?1 and o.question.id =?2")
    List<Option> findAllBySurvey_Question_Id(Long surveyId, Long questionId);
}