package surfy.comfy.entity.read;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.write.Question;
import surfy.comfy.entity.write.Survey;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="satisfaction")
public class Satisfaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="question_id")
    private Long questionId;

    @Column(name="survey_id")
    private Long surveyId;

    private Long percent;

}
