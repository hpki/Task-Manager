package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Long> idSubtaskList = new ArrayList<>();
    private TypeTask type = TypeTask.EPIC;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id='" + getId() + '\'' +
                ", status='" + getStatus() +
                '\'' + '}';
    }

    public ArrayList<Long> getIdSubtaskList() {  // для проверки работы программы
        return idSubtaskList;
    }

    public void setIdSubtaskList(long subtaskId) {
        idSubtaskList.add(subtaskId);
    }

    public ArrayList<Long> getSubtaskList() {
        return idSubtaskList;
    }

    public void removeIdSubtask(long id) {
        idSubtaskList.remove(id);
    }


    public void printArrayList() {               // для проверки работы программы
        for (int i = 0; i < idSubtaskList.size(); i++) {
            System.out.println(idSubtaskList.get(i));
        }
    }

}
