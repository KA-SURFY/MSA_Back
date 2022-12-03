package surfy.comfy.entity.write;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name="answer")
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="answer_id")
    private Long id;

    @Column(name="question_id")
    private Long questionId;

    @Column(name="survey_id")
    private Long surveyId;

    @Column(name="option_id")
    private Long optionId;

    @Column(name="grid_id")
    private Long gridId;

    @Column(name="essay_id")
    private Long essayId;

    @Column(name="satisfaction_id")
    private Long satisfactionId;

    @Column(name="slider_id")
    private Long sliderId;

    @Column(name="submit_id")
    private Long submit;
}