package surfy.comfy.repository.read;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.read.Essay;
import surfy.comfy.entity.read.Satisfaction;

import java.util.List;
import java.util.Optional;

public interface ReadEssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findAllByQuestionId(Long QuestionId);
    Optional<Essay> findById(Long Id);
}
