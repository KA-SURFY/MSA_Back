package surfy.comfy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter @Setter
@Table(name="token")
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private Long id;

    //private String accessToken;
    private String refreshToken;
    private String refreshTokenIdxEncrypted;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name="member_id")
    private Member member;
}