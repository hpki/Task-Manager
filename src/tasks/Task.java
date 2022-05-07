package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private long id;
    private Status status = Status.NEW;
    private TypeTask type = TypeTask.TASK;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

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

    public Task(String name, String description, LocalDateTime startTime, Duration duration) { //конструктор для Task
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(String name, String description) {  //конструктор для Epic
        this.name = name;
        this.description = description;

    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration, Long id) {  //конструктор для Subtask
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.id = id;
        this.endTime = getEndTime();
    }


    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

}
