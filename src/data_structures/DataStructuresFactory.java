package data_structures;

public interface DataStructuresFactory
{
	Task newTask(int id, String name, int length);
	
	Constraint newConstraint(TaskManager taskManager, String data);
}
