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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/api/tasks")
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(value = "author", required = false) String authorParam) {
        List<TaskDTO> tasksDTO = new ArrayList<>();
        Iterable<Task> listTasks;

        if (authorParam != null && !authorParam.isEmpty()) {
            // If author parameter is provided, filter by author
            String author = authorParam.toLowerCase();
            listTasks = taskRepository.findTaskByAuthor_Email(author);
        } else {
            // If no author parameter, return all tasks
            listTasks = taskRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }

        for (Task task : listTasks) {
            tasksDTO.add(TaskDTO.fromTask(task));
        }
        return ResponseEntity.ok().body(tasksDTO);
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Determine the principal type and extract the username (email)
        String username;
        Object principal = authentication.getPrincipal();

        if (principal instanceof Account) {
            // This case handles Basic Authentication, where the principal is already an Account object
            username = ((Account) principal).getEmail();
        } else if (principal instanceof Jwt) {
            // This case handles JWT Bearer Token authentication
            username = ((Jwt) principal).getSubject(); // The subject of the JWT is the username (email)
        } else {
            // Handle other potential principal types or throw an exception if unknown
            throw new IllegalStateException("Unsupported principal type: " + principal.getClass().getName());
        }

        // Load the Account from the repository using the extracted username (email)
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + username));

        task.setAuthor(account);
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskDTO.fromTask(task));
    }

    @PutMapping("/api/tasks/{taskId}/assign")
    public ResponseEntity<TaskDTO> assignTask(@Valid @RequestBody RequestAssigneeDTO assignee, @Valid @PathVariable Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = ((Jwt) authentication.getPrincipal()).getSubject();

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (assignee.assignee().matches("\\w+([.-]\\w+)*@\\w+(\\.\\w+)+")) {
            Account assigneeAccount = accountRepository.findByEmail(assignee.assignee()).orElseThrow(() -> new AccountNotFoundException("Assignee not found with email: " + assignee.assignee()));
        } else if (!assignee.assignee().equals("none")) {
            throw new AssigneeNotValidException("Assignee isn't a valid email address or \"none\"");
        }

        if (username.equals(task.getAuthor().getEmail()) ) {
            task.setAssignee(assignee.assignee());
        } else {
            throw new AccountNotAuthorException("Account not authorized to assign this task");
        }

        taskRepository.save(task);
        return ResponseEntity.ok().body(TaskDTO.fromTask(task));

    }

    @PutMapping("/api/tasks/{taskId}/status")
    public ResponseEntity<TaskDTO> statusTask(@Valid @RequestBody RequestStatusDTO status, @Valid @PathVariable Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = ((Jwt) authentication.getPrincipal()).getSubject();

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));


        if (username.equals(task.getAuthor().getEmail()) || (username.equals(task.getAssignee())) ) {
            task.setStatus(status.status());
        } else {
            throw new AccountNotAuthorException("Account not authorized to change the status");
        }

        taskRepository.save(task);
        return ResponseEntity.ok().body(TaskDTO.fromTask(task));

    }
}