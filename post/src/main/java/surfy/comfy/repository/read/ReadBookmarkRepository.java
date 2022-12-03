package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.write.Bookmark;

import java.util.List;

@Repository
public interface ReadBookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark> findAllByMemberId(Long memberId);
    Bookmark findByMemberIdAndPostId(Long memberId,Long postId);
    List<Bookmark> findAllByPostId(Long postId);

}
