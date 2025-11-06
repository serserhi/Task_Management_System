package taskmanagement;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;


public interface  CommentRepository  extends CrudRepository<Comment, Long> {


    Iterable<Comment> findAllByTask(Task task, Sort sort);
}
