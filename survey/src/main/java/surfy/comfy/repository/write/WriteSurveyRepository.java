package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.type.SurveyType;

import java.util.List;
import java.util.Optional;

public interface WriteSurveyRepository extends JpaRepository<Survey,Long> {
    Optional<Survey> findById(Long surveyId);
    Survey findSurveysById(Long surveyId);
    List<Survey> findAllByMemberId(Long memberId);

    List<Survey> findAllByMemberIdAndStatus(Long memberId, SurveyType status);
    List<Survey> findSurveyById(Long surveyId);

}
