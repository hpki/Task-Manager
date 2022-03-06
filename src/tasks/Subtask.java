package tasks;

public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, long epicId) {
        super(name, description);
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
