package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToOne
    @JoinColumn(name="option_id")
    private Option option;

    @OneToOne
    @JoinColumn(name="essay_id")
    private Essay essay;

    @OneToOne
    @JoinColumn(name="grid_id")
    private Grid grid;

    @OneToOne
    @JoinColumn(name="satisfaction_id")
    private Satisfaction satisfaction;

    private String filePath;

    @Column(name="submit_id")
    private Long submit;

    @OneToOne
    @JoinColumn(name="slider_id")
    private Slider slider;
}