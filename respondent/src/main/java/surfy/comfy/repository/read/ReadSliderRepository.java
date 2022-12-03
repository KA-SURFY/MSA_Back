package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Slider;

import java.util.List;
import java.util.Optional;

public interface ReadSliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findAllByQuestionId(Long QuestionId);
    List<Slider> findAllBySurveyId(Long surveyId);
    Optional<Slider> findById(Long Id);
}