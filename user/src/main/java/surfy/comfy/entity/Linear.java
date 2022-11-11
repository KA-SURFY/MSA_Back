package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="linear_option")
public class Linear {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="linear_id")
    private Long id;

    private Long size;

    private String start;
    private String end;

    @OneToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;
}