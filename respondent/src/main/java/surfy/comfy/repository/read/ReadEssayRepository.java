package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.write.Essay;

import java.util.List;

public interface ReadEssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findAllByQuestionId(Long QuestionId);
}
