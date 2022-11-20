package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Slider;

import java.util.List;

public interface WriteSliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findAllByQuestionId(Long QuestionId);
    List<Slider> findAllBySurveyId(Long surveyId);
}