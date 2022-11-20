package surfy.comfy.entity.write;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.read.Question;
import surfy.comfy.entity.read.Survey;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="slider_option")
public class Slider {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="slider_id")
    private Long id;

    private Long value;

    private Long questionId;

    private Long surveyId;
}