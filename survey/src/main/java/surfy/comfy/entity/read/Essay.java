package surfy.comfy.entity.read;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.write.Question;
import surfy.comfy.entity.write.Survey;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="essay")
public class Essay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="essay_id")
    private Long id;

    private String contents;

    private Long questionId;

    private Long surveyId;

}

