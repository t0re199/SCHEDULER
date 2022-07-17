package backtracking;

import data_structures.AbstractTaskManager;
import data_structures.Constraint;
import data_structures.DataStructuresFactory;
import data_structures.DataStructuresFactoryImpl;
import data_structures.Task;
import data_structures.UnmodifiableTask;
import exceptions.AlreadyExistingConstraintException;
import exceptions.AlreadyExistingTaskException;
import exceptions.ScheduleNotExistingException;
import util.log.Log;
import util.log.LogFactoryImpl;

public class TaskManagerBacktracking extends AbstractTaskManager implements TemplateMethod<Task, Integer>
{
	private static final Log log = LogFactoryImpl.getInstance().logOnConsole();

	private static DataStructuresFactory factory = DataStructuresFactoryImpl.getInstance();
	
	private static final Task lastChoisePoint = factory.newTask(-0x1, "", 0x0);
	private Task firstChoisePoint = null;
	private int maxTaskId = 0x0;
	private int lastChoise = 0x0;
	
	private int currentChoise = 0x0;
	
	@Override
	public synchronized Task newTask(String taskName, int length)
	{
		if(taskName == null || taskName.length() <= 0x0 || length <= 0x0)
		{
			throw new IllegalArgumentException();
		}
		int taskId = getTaskIdByName(taskName);
		if(taskId != TASK_NOT_FOUND)
		{
			throw new AlreadyExistingTaskException();
		}
		taskId = assignTaskId(taskName);
		Task t = factory.newTask(taskId, taskName, length);
		createdTask.put(taskId, t);
		lastChoise += length;
		return new UnmodifiableTask(t);
	}

	@Override
	public synchronized Constraint newConstraint(String str)
	{
		Constraint constraint = factory.newConstraint(this, str);
		if(constraints.contains(constraint))
		{
			throw new AlreadyExistingConstraintException();
		}
		constraints.add(constraint);
		return constraint;
	}
	
	@Override
	public Task firstChoisePoint()
	{
		if(firstChoisePoint != null)
		{
			return firstChoisePoint;
		}
		Task t = createdTask.get(0x0);
		if(taskCanBeStarted(t))
		{
			firstChoisePoint = t;
		}
		else
		{
			firstChoisePoint = nextChoisePoint(t);
		}
		return firstChoisePoint;
	}

	@Override
	public Task nextChoisePoint(Task ps)
	{
		if(tasksCompleted())
		{
			return lastChoisePoint;
		}
		Task t = null;
		boolean flag = true;
		for(int i = ps.getId() + 0x1; flag && currentChoise <= lastChoise; i++)
		{
			if(i % maxTaskId == 0x0)
			{
				currentChoise++;
			}
			t = createdTask.get(i % maxTaskId);
			if(!t.isCompleted() && taskCanBeStarted(t))
			{
				flag = false;
			}
		}
		if(currentChoise > lastChoise)
		{
			throw new ScheduleNotExistingException();
		}
		return t;
	}

	@Override
	public Task lastChoisePoint()
	{
		return lastChoisePoint;
	}

	@Override
	public Integer firstChoise(Task ps)
	{
		return Integer.valueOf(currentChoise);
	}

	@Override
	public Integer nextChoise(Integer s)
	{
		return Integer.valueOf(++s + currentChoise);
	}

	@Override
	public Integer lastChoise(Task ps)
	{
		return Integer.valueOf(lastChoise);
	}

	@Override
	public boolean assignable(Integer choise, Task choisePoint)
	{
		return true;
	}

	@Override
	public void assign(Integer choise, Task choisePoint)
	{
		choisePoint.assign(choise);
	}

	@Override
	public void deassign(Integer choise, Task choisePoint)
	{
		choisePoint.deassign(choise);
	}

	@Override
	public Task previousChoisePoint(Task choisePoint)
	{
		return null;
	}

	@Override
	public Integer lastChoiseAssignedTo(Task choisePoint)
	{
		return Integer.valueOf(choisePoint.getLastAssignedInstant());
	}

	@Override
	public void writeSolution(int sol)
	{
		scheduleHasBeenFound = true;
		log.println("SOLUTION FOUND:");
		for(Task task : createdTask.values())
		{
			log.println(task);
		}
	}
	
	private boolean taskCanBeStarted(Task t)
	{
		t.assign(currentChoise);
		boolean flag = true;
		for(Constraint constraint : constraints)
		{
			if(!constraint.isVerified(currentChoise))
			{
				flag = false;
				break;
			}
		}
		t.deassign(currentChoise);
		return flag;
	}
	
	private boolean tasksCompleted()
	{
		for(Task task : createdTask.values())
		{
			if(!task.isCompleted())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void solve(int maxSol)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void solve()
	{
		maxTaskId = getTaskNum();
		lastChoise += maxTaskId;
		maxTaskId = getTaskNum();
		TemplateMethod.super.solve(0x1);
	}
	
	
	@Override
	public int getRequiredTime()
	{
		return currentChoise + 0x1;
	}
	
	
	@Override
	public void clear()
	{
		firstChoisePoint = null;
		maxTaskId = 0x0;
		lastChoise = 0x0;
		
		currentChoise = 0x0;
		super.clear();
	}
	
	
	public static void main(String[] args)
	{
		TaskManagerBacktracking taskManager = new TaskManagerBacktracking();
		taskManager.newTask("0", 0x4);
		taskManager.newTask("1", 0x4);
		taskManager.newTask("2", 0x4);
		taskManager.newTask("3", 0x4);
		
		taskManager.newConstraint("i,1,d,f,0");
		taskManager.newConstraint("i,2,d,f,1");
		taskManager.newConstraint("i,3,d,f,2");
		
		taskManager.solve();
	}
}
