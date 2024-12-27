import managers.TaskManager;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import statuses.Status;

public class Main {

    public static void main(String[] args) {

        System.out.println("Пользовательский сценарий");

        TaskManager taskManager = Managers.getDefault();

        //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        Task task1 = new Task("посуда", "помыть тарелки", Status.NEW);
        taskManager.add (task1);
        Task task2 = new Task("уборка", "помыть полы", Status.NEW);
        taskManager.add (task2);

        Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег");
        taskManager.add (epic1);

        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.IN_PROGRESS,3);
        taskManager.add(subtask1);
        Subtask subtask2 = new Subtask("собеседование", "пройти и его", Status.NEW,3);
        taskManager.add (subtask2);
        Subtask subtask3 = new Subtask("6 спринт", "успешно сдать", Status.IN_PROGRESS,3);
        taskManager.add(subtask3);

        Epic epic2 = new Epic("купить квартиру", "когда заработаешь много денег");
        taskManager.add(epic2);

        //Запросите созданные задачи несколько раз в разном порядке.
        //После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
        taskManager.epicSearch(7);
        System.out.println("История 1");
        System.out.println(taskManager.getHistory());
        taskManager.taskSearch(1);
        System.out.println("История 2");
        System.out.println(taskManager.getHistory());
        taskManager.subtaskSearch(6);
        System.out.println("История 3");
        System.out.println(taskManager.getHistory());
        taskManager.taskSearch(2);
        System.out.println("История 4");
        System.out.println(taskManager.getHistory());
        taskManager.subtaskSearch(6);
        System.out.println("История 5");
        System.out.println(taskManager.getHistory());
        taskManager.epicSearch(7);
        System.out.println("История 6");
        System.out.println(taskManager.getHistory());
        taskManager.subtaskSearch(5);
        System.out.println("История 7");
        System.out.println(taskManager.getHistory());

        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        System.out.println();
        taskManager.taskRemove(1);
        System.out.println("История после удаления задачи id 1");
        System.out.println(taskManager.getHistory());

        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        System.out.println();
        taskManager.epicRemove(3);
        System.out.println("История после удаления эпика с тремя подзадачами");
        System.out.println(taskManager.getHistory());

    }
}
