package surfy.comfy.entity.read;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="c_option")
public class Option {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="option_id")
    private Long id;

    @Column(name="question_id")
    private Long questionId;

    @Column(name="survey_id")
    private Long surveyId;

    private String contents;
}