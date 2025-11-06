package taskmanagement;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Iterable<Task> findAll(Sort id);

    Iterable<Task> findTaskByAuthor_Email(String authorEmail);
}
