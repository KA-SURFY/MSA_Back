package surfy.comfy.entity.write;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.read.Question;
import surfy.comfy.entity.read.Survey;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="essay")
public class Essay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="essay_id")
    private Long id;

    private String contents;

    @Column(name="question_id")
    private Long questionId;

    @Column(name="survey_id")
    private Long surveyId;

}

