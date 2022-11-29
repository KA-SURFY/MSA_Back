package surfy.comfy.entity.write;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name="post")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    private String name;

    @Column(name="survey_id")
    private Long surveyId;

    private LocalDate uploadDate;
    private String title;
    private String contents;

    private Long thumbnail;
}