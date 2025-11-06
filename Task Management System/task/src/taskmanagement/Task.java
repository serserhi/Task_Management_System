package taskmanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Title field cannot be blank")
    @NotNull(message="Title field is mandatory")
    private String title;

    @NotBlank(message="Description field cannot be blank")
    @NotNull(message="Description field is mandatory")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.CREATED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id", nullable=false)
    @JsonIgnore
    private Account author;

    private String assignee = "none";

    private int totalComments = 0;

    public Task() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getStatus() {return status.name();}
    public void setStatus(TaskStatus status) {this.status = status;}
    public Account getAuthor() {return author;}
    public void setAuthor(Account author) {this.author = author;}
    public String getAssignee() {return assignee;}
    public void setAssignee(String assignee) {this.assignee = assignee;}
    public int getTotalComments() {return totalComments;}
    public void setTotalComments(int totalComments) {this.totalComments = totalComments;}

    public enum TaskStatus {
        CREATED,
        IN_PROGRESS,
        COMPLETED
    }

}
