package surfy.comfy.repository.read;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.read.Survey;

@Repository
public interface ReadResultRepository extends JpaRepository<Survey, Long> {





}
