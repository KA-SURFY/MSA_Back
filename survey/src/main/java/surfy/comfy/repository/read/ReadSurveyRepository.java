package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.type.SurveyType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadSurveyRepository extends JpaRepository<Survey,Long> {
    Optional<Survey> findById(Long surveyId);
    Survey findSurveysById(Long surveyId);

    List<Survey> findAllByMemberIdAndStatus(Long memberId, SurveyType status);

    List<Survey> findAllByMemberId(Long memberId);
}
