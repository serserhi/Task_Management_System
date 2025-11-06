package taskmanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable=false)
    @JsonIgnore
    private Task task;

    @NotBlank(message="Text cannot be blank")
    private String text;

    private String author;

    public Comment() {}

    public Comment(Task task, String text, String author) {
        this.task = task;
        this.text = text;
        this.author = author;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Task getTask() {return task;}
    public void setTask(Task task) {this.task = task;}
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
}
