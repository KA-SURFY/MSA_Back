package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.read.Member;

@Repository
public interface ReadMemberRepository extends JpaRepository<Member,Long> {
}
