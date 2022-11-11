package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Essay;

import java.util.List;

public interface EssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findAllByQuestion_Id(Long QuestionId);
}
