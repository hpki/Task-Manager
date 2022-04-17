package tasks;

public class Task {
    private String name;
    private String description;
    private long id;
    private Status status = Status.NEW;
    private TypeTask type = TypeTask.TASK;

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE;
    }

    public enum TypeTask {
        TASK,
        SUBTASK,
        EPIC;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public TypeTask getType() {
        return type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status +
                '\'' + '}';
    }

    public void setType(TypeTask type) {
        this.type = type;
    }
}
