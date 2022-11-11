package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Slider;

import java.util.List;

public interface SliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findAllByQuestion_Id(Long QuestionId);
    List<Slider> findAllBySurvey_Id(Long surveyId);
}