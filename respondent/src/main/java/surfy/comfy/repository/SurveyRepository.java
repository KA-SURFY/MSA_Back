package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Survey;
import surfy.comfy.type.SurveyType;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey,Long> {
    Optional<Survey> findById(Long surveyId);
    Survey findSurveysById(Long surveyId);
    List<Survey> findAllByMember_Id(Long memberId);

    List<Survey> findAllByMember_IdAndStatus(Long memberId, SurveyType status);
    List<Survey> findSurveyById(Long surveyId);

}
