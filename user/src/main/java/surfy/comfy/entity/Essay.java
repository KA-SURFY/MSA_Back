package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="essay")
public class Essay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="essay_id")
    private Long id;

    private String contents;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="question_id")
    private Question question;

}

