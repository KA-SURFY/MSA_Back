
package surfy.comfy.entity.write;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.type.QuestionType;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="b_question")
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="question_type")
    private QuestionType questionType;

    @Column(name="survey_id")
    private Long surveyId;

    private String contents;
}
