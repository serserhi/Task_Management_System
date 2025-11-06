package taskmanagement;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
public class CommentController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<List<Comment>> getComments (@Valid @PathVariable("taskId") Long taskId) {
        List<Comment> Comments = new ArrayList<>();
        Iterable<Comment> listComments;

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        listComments = commentRepository.findAllByTask(task, Sort.by("id").descending());


        for (Comment comment : listComments) {
            Comments.add(comment);
        }
        return ResponseEntity.ok().body(Comments);
    }


    @PostMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<Void> createComment(@Valid @RequestBody RequestTextDTO text, @Valid @PathVariable Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = ((Jwt) authentication.getPrincipal()).getSubject();

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        Comment comment = new Comment(task, text.text(), username);

        commentRepository.save(comment);
        task.setTotalComments(task.getTotalComments() + 1);
        taskRepository.save(task);
        return ResponseEntity.ok().build();

    }


}