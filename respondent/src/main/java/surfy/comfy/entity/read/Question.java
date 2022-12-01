
package surfy.comfy.entity.read;

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
    @JsonBackReference(value = "survey-question")
    @JoinColumn(name="survey_id")
    private Survey survey;

    @JsonManagedReference(value ="question-option")
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Option> options;

    @JsonManagedReference(value = "question-grid")
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Grid> grids;

    private String contents;

    public void setGrid(Grid grid){
        grids.add(grid);
    }
}
