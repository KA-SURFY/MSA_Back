package surfy.comfy.entity.read;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="grid")
// 소주제
public class Grid {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grid_id")
    private Long id;

    @Column(name="question_id")
    private Long questionId;

    @Column(name="survey_id")
    private Long surveyId;

    private String contents;
}