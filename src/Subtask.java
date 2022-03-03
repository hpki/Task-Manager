import java.util.ArrayList;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name,description);
            this.epicId = epicId;
        }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status +
                '\'' + '}';
    }

    public int getEpicId() {
        return epicId;
    }

}
