import TaskManager.TaskManager;
import TaskManager.epic.Epic;
import TaskManager.subtask.Subtask;
import TaskManager.task.Task;
import TaskManager.Status;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("посуда", "помыть тарелки", Status.NEW);
        taskManager.add (task1);
        Task task2 = new Task("уборка", "помыть полы", Status.NEW);
        taskManager.add (task2);

        Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег");
        taskManager.add (epic1);

        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,3);
        taskManager.add(subtask1);
        Subtask subtask2 = new Subtask("собеседование", "пройти и его", Status.NEW,3);
        taskManager.add (subtask2);

        Epic epic2 = new Epic("купить квартиру", "когда заработаешь много денег");
        taskManager.add(epic2);

        Subtask subtask3 = new Subtask("выбрать квартиру", "прошерстить авито", Status.NEW,6);
        taskManager.add(subtask3);

        System.out.println();
        System.out.println("Вывод всех задач/подзадач/эпиков");
        System.out.println(taskManager.printTasks());
        System.out.println(taskManager.printSubtasks());
        System.out.println(taskManager.printEpics());

        System.out.println();
        System.out.println("Вывод подзадач определенного эпика");
        System.out.println(taskManager.epicSubtasks(6));

        System.out.println();
        System.out.println("Обновление");
        Task updatedTask = new Task("уборка", "помыть полы", Status.IN_PROGRESS, 2);
        taskManager.update(updatedTask);
        System.out.println(taskManager.printTasks());

        Epic updatedEpic = new Epic("купить квартиру", "когда заработаешь ОЧЕНЬ много денег", epic2.getId());
        taskManager.update(updatedEpic);
        System.out.println(taskManager.printEpics());

        Subtask updatedSubtask = new Subtask("выбрать квартиру", "прошерстить авито", Status.DONE, 6, 7);
        taskManager.update(updatedSubtask);
        System.out.println(taskManager.printSubtasks());
        System.out.println(taskManager.printEpics());

        System.out.println();
        System.out.println("Удаление подзадачи");
        System.out.println("до");
        System.out.println(taskManager.printEpics());
        System.out.println(taskManager.printSubtasks());
        taskManager.subtaskRemove(4);
        System.out.println("после");
        System.out.println(taskManager.printSubtasks());
        System.out.println(taskManager.printEpics());

        System.out.println();
        System.out.println("Удаление эпика");
        System.out.println("до");
        System.out.println(taskManager.printEpics());
        System.out.println(taskManager.printSubtasks());
        taskManager.epicRemove(3);
        System.out.println("после");
        System.out.println(taskManager.printEpics());
        System.out.println(taskManager.printSubtasks());
    }
}
