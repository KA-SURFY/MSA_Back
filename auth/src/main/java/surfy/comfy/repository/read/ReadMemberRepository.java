package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Member;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ReadMemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
}
