package data_structures;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import exceptions.AlreadyExistingTaskException;
import exceptions.MalformedTaskException;
import exceptions.TaskNotFoundException;

public abstract class AbstractTaskManager implements TaskManager
{	
	protected HashMap<Integer, Task> createdTask = new HashMap<>();
	
	protected HashMap<String, Integer> legend = new HashMap<>();
	
	protected HashSet<Constraint> constraints = new HashSet<>();
	
	private int idProgressive = 0x0;
	
	protected boolean scheduleHasBeenFound = false;
	
	@Override
	public Task newTask(String data)
	{
		data = data.toLowerCase();
		if(!data.matches(TaskImpl.REGEX))
		{
			throw new MalformedTaskException();
		}
		data = data.charAt(0x0) == '(' ? data.substring(0x1, data.length()) : data;
		data = data.charAt(data.length() - 0x1) == ')' ? data.substring(0x0, data.length() - 0x1) : data;
		try
		{
			StringTokenizer st = new StringTokenizer(data, ",");
			String name = st.nextToken().trim();
			int lenght = Integer.parseInt(st.nextToken().trim());
			return newTask(name, lenght);
		}
		catch (AlreadyExistingTaskException e) 
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new MalformedTaskException();
		}
	}
	
	@Override
	public TaskState getTaskState(int taskId)
	{
		Task t = createdTask.get(taskId);
		if(t == null)
		{
			throw new TaskNotFoundException();
		}
		return t.getState();
	}
	
	@Override
	public TaskState getTaskStateAtInstant(int taskId, int instant)
	{
		Task t = createdTask.get(taskId);
		if(t == null)
		{
			throw new TaskNotFoundException();
		}
		return t.getStateAtInstant(instant);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CreatedTasks:\n");
		for(Task task : createdTask.values())
		{
			sb.append(task + "\n");
		}
		sb.append("\n\n");
		for(Constraint constraint : constraints)
		{
			sb.append(constraint + "\n");
		}
		return sb.toString();
	}
	
	@Override
	public Task getTask(int taskId)
	{
		return new UnmodifiableTask(createdTask.get(taskId));
	}
	
	@Override
	public Task getTask(String taskName)
	{
		int taskId = getTaskIdByName(taskName);
		if(taskId == TASK_NOT_FOUND)
		{
			throw new TaskNotFoundException();
		}
		return getTask(taskId);
	}
	
	@Override
	public boolean taskExists(int taskId)
	{
		return createdTask.containsKey(taskId);
	}
	
	@Override
	public boolean taskExists(String taskName)
	{
		return legend.containsKey(taskName);
	}
	
	@Override
	public int assignTaskId(String taskName)
	{
		legend.put(taskName, idProgressive);
		return idProgressive++;
	}
	
	@Override
	public int getTaskNum()
	{
		return idProgressive;
	}
	
	@Override
	public int getTaskIdByName(String taskName)
	{
		Integer i = legend.get(taskName);
		return i != null ? i : TASK_NOT_FOUND;
	}
	
	@Override
	public boolean scheduleHasBeenFound()
	{
		return scheduleHasBeenFound;
	}
	
	@Override
	public Map<String, Integer> getLegend()
	{
		return Collections.unmodifiableMap(legend);
	}
	
	@Override
	public Collection<Task> getTasks()
	{
		return Collections.unmodifiableCollection(createdTask.values());
	}
	
	@Override
	public Collection<Constraint> getConstraints()
	{
		return Collections.unmodifiableSet(constraints);
	}
	
	@Override
	public void clear()
	{
		createdTask.clear();
		legend.clear();
		constraints.clear();
		idProgressive = 0x0;
		scheduleHasBeenFound = false;
	}
}
