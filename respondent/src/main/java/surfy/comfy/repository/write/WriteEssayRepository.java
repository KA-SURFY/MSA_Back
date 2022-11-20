package surfy.comfy.repository.write;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Essay;

import java.util.List;

public interface WriteEssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findAllByQuestionId(Long QuestionId);
}
