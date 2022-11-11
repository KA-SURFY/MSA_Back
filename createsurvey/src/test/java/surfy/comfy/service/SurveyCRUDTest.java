package surfy.comfy.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import surfy.comfy.entity.Member;
import surfy.comfy.entity.Survey;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.SurveyRepository;
import surfy.comfy.type.SurveyType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SurveyCRUDTest {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void create(){
        Member member = new Member();

        member.setName("hojin");
        member.setEmail("aaa@aaa");
        member.setProvider("제공");

        memberRepository.save(member);

        Survey survey = new Survey();

        survey.setTitle("abdc");
        survey.setContents("bcab");
        survey.setStatus(SurveyType.surveying);
        survey.setMember(member);

        surveyRepository.save(survey);

        assertThat(member.getId()).isEqualTo(5L);
        assertThat(survey.getId()).isEqualTo(5L);
    }

    @Test
    public void read(){
        Optional<Survey> survey = surveyRepository.findById(1L);
        Optional<Member> member = memberRepository.findById(1L);

        assertThat(survey.get().getTitle()).isEqualTo("설문지1");
        assertThat(member.get().getName()).isEqualTo("aaa");
    }

    @Test
    public void update(){
        //initDB에서 3번 설문지는 surveying 상태
        //finish로 바꾼 후 기존 상태와 같은지 비교
        Optional<Survey> survey = surveyRepository.findById(3L);

        survey.get().setStatus(SurveyType.finish);

        assertThat(survey.get().getStatus()).isEqualTo(SurveyType.finish);
    }

    @Test
    public void delete() {
        //멤버 데이터 추가 후 존재하는지 확인
        //존재한다면 삭제하고
        //다시 db에서 추가했던 데이터를 가져온 후 존재하는지 확인
        Member member = new Member();
        member.setName("hojin");
        member.setEmail("aaa@aaa");
        member.setProvider("제공");
        memberRepository.save(member);

        Optional<Member> member123 = memberRepository.findById(5L);

        assertThat(member123.isPresent()).isTrue();

        member123.ifPresent(selectMember->{
            memberRepository.delete(selectMember);
        });

        Optional<Member> deleteMember = memberRepository.findById(5L);
        assertThat(deleteMember.isPresent()).isFalse();
    }
}