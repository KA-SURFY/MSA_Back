package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="answer_option")
public class Option {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="option_id")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;

    private String contents;
}