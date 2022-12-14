package surfy.comfy.entity.write;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.entity.read.Essay;
import surfy.comfy.entity.read.Satisfaction;
import surfy.comfy.type.SurveyType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="survey")
public class Survey {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="survey_id")
    private Long id;

    private Long memberId; // 설문 제작자

    @JsonManagedReference
    @OneToMany(mappedBy="survey",cascade = CascadeType.ALL)
    private List<Question> questions;

    private LocalDate start;
    private LocalDate end;

    private String title;
    private String contents;

    @Enumerated(EnumType.STRING)
    private SurveyType status;

    private Long satisfaction;
    private String bgColor;
    private Long thumbnail;
}