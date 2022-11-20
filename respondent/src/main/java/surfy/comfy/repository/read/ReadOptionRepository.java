package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import surfy.comfy.entity.read.Option;

import java.util.List;

public interface ReadOptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByQuestion_Id(Long QuestionId);
    List<Option> findAllBySurvey_Id(Long surveyId);

    @Query("select o from Option o where o.survey.id = ?1 and o.question.id =?2")
    List<Option> findAllBySurvey_Question_Id(Long surveyId, Long questionId);
}