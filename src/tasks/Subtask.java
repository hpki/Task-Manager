package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private long epicId;
    private TypeTask type = TypeTask.SUBTASK;

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, long epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id='" + getId() + '\'' +
                ", status='" + getStatus() +
                '\'' + '}';
    }

    public long getEpicId() {
        return epicId;
    }

}
