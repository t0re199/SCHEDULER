package data_structures;

import java.util.Collection;
import java.util.Map;

public interface TaskManager
{
	static final int TASK_NOT_FOUND = -0xa;
	
	boolean taskExists(int taskId);
	
	boolean taskExists(String taskName);
	
	Task newTask(String taskName, int lenght);
	
	Task newTask(String taskData);
	
	Constraint newConstraint(String constraint);
	
	Task getTask(int taskId);
	
	Task getTask(String name);
	
	TaskState getTaskState(int taskId);
	
	TaskState getTaskStateAtInstant(int taskId, int instant);
	
	int getTaskIdByName(String taskName);
	
	int assignTaskId(String taskName);
	
	int getTaskNum();
	
	int getRequiredTime();
	
	boolean scheduleHasBeenFound();
	
	Collection<Constraint> getConstraints();
	
	Collection<Task> getTasks();
	
	Map<String, Integer> getLegend();
	
	void clear();
}
