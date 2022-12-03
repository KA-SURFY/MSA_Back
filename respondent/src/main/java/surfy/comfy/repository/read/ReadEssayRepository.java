package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Essay;

import java.util.List;
import java.util.Optional;

public interface ReadEssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findAllByQuestionId(Long QuestionId);
    Optional<Essay> findById(Long essayId);
}
