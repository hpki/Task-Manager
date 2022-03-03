public class Task {
    String name;
    String description;
    int id;
    String status = "NEW"; //0-NEW, 1-IN_PROGRESS, 2-DONE

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task() {
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



}
