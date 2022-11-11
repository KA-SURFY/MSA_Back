package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="satisfaction")
public class Satisfaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="question_id")
    private Question question;

    private Long percent;

}
