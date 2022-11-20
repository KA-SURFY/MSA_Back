package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Member;

import java.util.Optional;

@Repository
public interface WriteMemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
}
