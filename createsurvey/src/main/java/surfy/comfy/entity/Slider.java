package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="slider_option")
public class Slider {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="slider_id")
    private Long id;

    private Long value;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;
}