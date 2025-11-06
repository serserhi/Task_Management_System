package taskmanagement;

public record TaskDTO (
    Long id,
    String title,
    String description,
    String status,
    String author,
    String assignee,
    int totalComments
){
    public static TaskDTO fromTask(Task task){
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAuthor().getEmail().toLowerCase(),
                task.getAssignee(),
                task.getTotalComments()
        );
    }
}

