package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Bookmark;

import java.util.List;

@Repository
public interface WriteBookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark> findAllByMemberId(Long memberId);
    Bookmark findByMemberIdAndPost_Id(Long memberId,Long postId);
    List<Bookmark> findAllByPost_Id(Long postId);
}
