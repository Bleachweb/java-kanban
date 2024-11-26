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

        taskManager.add(new Subtask("практикум", "пройти обучение", Status.NEW),3);
        taskManager.add (new Subtask("собеседование", "пройти и его", Status.NEW),3);

        Epic epic2 = new Epic("купить квартиру", "когда заработаешь много денег");
        taskManager.add(epic2);

        taskManager.add(new Subtask("выбрать квартиру", "прошерстить авито", Status.NEW),6);

        System.out.println();
        System.out.println("Вывод всех задач/подзадач/эпиков");
        taskManager.printTasks();
        taskManager.printSubtasks();
        taskManager.printEpics();

        System.out.println();
        System.out.println("Вывод подзадач определенного эпика");
        taskManager.epicSubtasks(6);

        System.out.println();
        System.out.println("Обновление");
        taskManager.update(new Task("уборка", "помыть полы", Status.IN_PROGRESS));
        taskManager.printTasks();

        taskManager.update(new Epic("купить квартиру", "когда заработаешь ОЧЕНЬ много денег"));
        taskManager.printEpics();

        taskManager.update(new Subtask("выбрать квартиру", "прошерстить авито", Status.DONE));
        taskManager.printSubtasks();
        taskManager.printEpics();

        System.out.println();
        System.out.println("Удаление подзадачи");
        System.out.println("до");
        taskManager.printEpics();
        taskManager.printSubtasks();
        taskManager.subtaskRemove(4);
        System.out.println("после");
        taskManager.printSubtasks();
        taskManager.printEpics();

        System.out.println();
        System.out.println("Удаление эпика");
        System.out.println("до");
        taskManager.printEpics();
        taskManager.printSubtasks();
        taskManager.epicRemove(3);
        System.out.println("после");
        taskManager.printEpics();
        taskManager.printSubtasks();
    }
}
