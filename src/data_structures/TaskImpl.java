package data_structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

public class TaskImpl implements Task
{
	public static final String REGEX = "\\(?\\s*\\w+\\s*,\\s*\\w+\\s*\\)?";
	
	private final String NAME;
	
	private final int ID,
					  LENGHT;
	
	private TaskState state = TaskState.NEW;
	
	private TreeMap<Integer, TaskState> map = new TreeMap<>();
	
	private LinkedList<Integer> assignedInstant = new LinkedList<>();
	
	public TaskImpl(int taskId, String name, int length)
	{
		if(length < 0x0 || name == null)
		{
			throw new IllegalArgumentException();
		}
		this.NAME = name;
		ID = taskId;
		LENGHT = length;
		map.put(-0x1, TaskState.NEW);
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public int lenght()
	{
		return LENGHT;
	}

	@Override
	public int getRemaingTime()
	{
		return LENGHT - assignedInstant.size();
	}

	@Override
	public TaskState getState()
	{
		return state;
	}
	
	@Override
	public void assign(int t)
	{
		assignedInstant.addFirst(t);
		if(assignedInstant.size() == 0x1)
		{
			state = TaskState.RUNNING;
		}
		else if(assignedInstant.size() - 0x1 == LENGHT)
		{
			state = TaskState.TERMINATED;
		}
		map.put(t, state);
	}

	@Override
	public void deassign(int t)
	{
		if(t < 0x0)
		{
			throw new IllegalArgumentException();
		}
		assignedInstant.removeFirst();
		map.remove(t);
		if(assignedInstant.size() == 0x0)
		{
			state = TaskState.NEW;
		}
		else
		{
			state = TaskState.RUNNING;
		}
	}

	@Override
	public int getLastAssignedInstant()
	{
		return assignedInstant.getLast();
	}

	@Override
	public boolean isCompleted()
	{
		return state == TaskState.TERMINATED;
	}

	@Override
	public TaskState getStateAtInstant(int instant)
	{
		int i = -0x1;
		int tmp = i;
		for(Iterator<Integer> it = map.keySet().iterator(); it.hasNext();)
		{
			tmp = it.next();
			if(tmp > instant)
			{
				break;
			}
			i = tmp;
		}
		return map.get(i);
	}

	@Override
	public int getStartInstant()
	{
		return assignedInstant.getLast();
	}

	@Override
	public int getCompletationInstant()
	{
		return assignedInstant.getFirst();
	}

	@Override
	public boolean isStarted()
	{
		return assignedInstant.size() > 0x0;
	}

	
	@Override
	public String toString()
	{
		return String.format("TaskId %d: Name: %s, Lenght %d, State: %s", ID, NAME, LENGHT, state);
	}

	@Override
	public int hashCode()
	{
		return ID * 997;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Task))
		{
			return false;
		}
		if(this == obj)
		{
			return true;
		}
		return ID == ((Task) obj).getId();
	}

}