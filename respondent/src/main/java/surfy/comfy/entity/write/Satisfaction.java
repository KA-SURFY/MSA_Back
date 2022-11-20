package surfy.comfy.entity.write;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.read.Question;
import surfy.comfy.entity.read.Survey;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="satisfaction")
public class Satisfaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private Long surveyId;

    private Long percent;

}
