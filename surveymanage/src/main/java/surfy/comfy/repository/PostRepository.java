package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Override
    Optional<Post> findById(Long aLong);
    List<Post> findAllByMember_Id(Long memberId);
    List<Post> findByTitleContaining(String title);
    Optional<Post> findAllBySurvey_Id(Long surveyId);
}