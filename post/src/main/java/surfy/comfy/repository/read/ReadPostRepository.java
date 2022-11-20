package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Post;

import java.util.List;
import java.util.Optional;

public interface ReadPostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findById(Long aLong);
    List<Post> findAllByMemberId(Long memberId);
    List<Post> findByTitleContaining(String title);
}