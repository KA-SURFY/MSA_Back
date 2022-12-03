package surfy.comfy.entity.read;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.write.Grid;
import surfy.comfy.entity.write.Option;
import surfy.comfy.entity.write.Question;
import surfy.comfy.entity.write.Survey;

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

    @Column(name="submit_id")
    private Long submit;
}