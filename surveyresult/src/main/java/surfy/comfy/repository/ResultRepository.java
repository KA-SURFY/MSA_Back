package surfy.comfy.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Survey;

@Repository
public interface ResultRepository extends JpaRepository<Survey, Long> {





}
