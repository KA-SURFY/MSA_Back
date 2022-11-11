
package surfy.comfy.entity;

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
@Table(name="question")
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="survey_id")
    private Survey survey;

    @JsonManagedReference
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Option> options;

    @JsonManagedReference
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Grid> grids;

    @JsonManagedReference
    @OneToMany(mappedBy="question",cascade = CascadeType.ALL)
    private List<Answer> answers;

    @JsonManagedReference
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Essay> essays;

    @JsonManagedReference
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Satisfaction> satisfactions;

    @JsonManagedReference
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Slider> sliders;

    private String contents;

    //==연관관계 편의 메소드==//
    public void setOption(Option option){
        options.add(option);
    }
    public void setAnswer(Answer answer){
        answers.add(answer);
    }
    public void setGrid(Grid grid){
        grids.add(grid);
    }
}
