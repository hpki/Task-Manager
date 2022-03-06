package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Long> idSubtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
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

    public ArrayList<Long> getIdSubtaskList() {
        return idSubtaskList;
    }

    public void setIdSubtaskList(long subtaskId) { // для проверки работы программы
        idSubtaskList.add(subtaskId);
    }

    public ArrayList<Long> getSubtaskList() {
        return idSubtaskList;
    }

    public void printArrayList() {               // для проверки работы программы
        for (int i = 0; i < idSubtaskList.size(); i++) {
            System.out.println(idSubtaskList.get(i));
        }
    }

}
