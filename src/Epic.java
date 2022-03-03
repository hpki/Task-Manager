import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> idSubtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status +
                '\'' + '}';
    }

    public void setIdSubtaskList(int subtaskId) { // для проверки работы программы
        idSubtaskList.add(subtaskId);
    }

    public ArrayList<Integer> getSubtaskList() {
        return idSubtaskList;
    }

    public void printArrayList() {               // для проверки работы программы
        for (int i = 0; i < idSubtaskList.size(); i++) {
            System.out.println(idSubtaskList.get(i));
        }
    }

}
