package surfy.comfy.entity.write;

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

    private Long questionId;

    private Long surveyId;

    private Long optionId;

    private Long gridId;

    @OneToOne
    @JoinColumn(name="essay_id")
    private Essay essay;

    @OneToOne
    @JoinColumn(name="satisfaction_id")
    private Satisfaction satisfaction;

    @Column(name="submit_id")
    private Long submit;

    @OneToOne
    @JoinColumn(name="slider_id")
    private Slider slider;
}