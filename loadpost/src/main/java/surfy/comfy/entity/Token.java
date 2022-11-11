package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="token")
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String accessToken;
    private String refreshToken;
    private String refreshTokenIdxEncrypted;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="member_id")
    private Member member;
}