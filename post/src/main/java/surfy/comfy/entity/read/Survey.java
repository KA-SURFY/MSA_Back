package surfy.comfy.entity.read;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import surfy.comfy.type.SurveyType;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="a_survey")
public class Survey {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="survey_id")
    private Long id;

    @Column(name="member_id")
    private Long memberId; // 설문 제작자

//    @JsonManagedReference
//    @OneToMany(mappedBy = "survey",cascade = CascadeType.ALL)
//    private List<Satisfaction> satisfactions;

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