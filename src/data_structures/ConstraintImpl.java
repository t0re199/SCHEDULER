package data_structures;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import exceptions.ConstraintContainsInvalidDataException;
import exceptions.MalformedConstraintException;

class ConstraintImpl implements Constraint
{
	private static final String REGEX = "[if]\\s*,\\s*\\w+\\s*,\\s*[pd]\\s*,\\s*[if]\\s*,\\s*\\w+";
	
	private static enum Order
	{
		BEFORE,
		AFTER;
		
		public static Order parseOrder(String order)
		{
			if(!order.matches("[pd]") || order.length() > 0x1)
			{
				throw new RuntimeException("Unable to parse:" + order);
			}
			return order.charAt(0x0) == 'p' ? BEFORE : AFTER; 
		}
	}
	
	private TaskManager taskManager;
	
	private int[] taskIds = new int[0x2];
	private TaskState[] states = new TaskState[0x2];
	private Order order = null;
	
	private String output;
	
	public ConstraintImpl(TaskManager taskManager, String constraint)
	{
		this.taskManager = taskManager;
		constraint = constraint.toLowerCase();
		if(!constraint.matches(REGEX))
		{
			throw new MalformedConstraintException();
		}
		parse(constraint);
	}
	
	private final void parse(String constraint)
	{
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(constraint, ",");
		int index = 0x0;
		boolean flag = true;
		try
		{
			for(int extracedData = 0x0; extracedData <= 0x4 && flag; extracedData++)
			{
				String token = st.nextToken().trim();
				sb.append(token);
				if(extracedData < 0x4)
				{
					sb.append(", ");
				}
				switch (extracedData)
				{
					case 0:
					case 3:
						index = extracedData == 0x0 ? 0x0 : 0x1;
						states[index] = token.charAt(0x0) == 'i' ? TaskState.NEW : TaskState.TERMINATED;
					break;

					case 1:
					case 4:
						index = extracedData == 0x1 ? 0x0 : 0x1;
						int id = taskManager.getTaskIdByName(token);
						if(id == TaskManager.TASK_NOT_FOUND)
						{
							flag = false;
							break;
						}
						taskIds[index] = id;
					break;
						
					case 2:
						order = Order.parseOrder(token);
					break;
				}
			}
		}
		catch (NoSuchElementException e) 
		{
			throw new MalformedConstraintException();
		}
		if(!flag)
		{
			throw new ConstraintContainsInvalidDataException();
		}
		if(order == Order.AFTER)
		{
			int tmp = taskIds[0x0];
			taskIds[0x0] = taskIds[0x1];
			taskIds[0x1] = tmp;
			TaskState sTmp = states[0x0];
			states[0x0] = states[0x1];
			states[0x1] = sTmp;
			order = Order.BEFORE;
		}
		if(states[0x0] == states[0x1] && states[0x0] == TaskState.TERMINATED)
		{
			/*
			 * sono nel caso: f,t1,[p|d],f,t2 tale caso e' =
			 * 	1: f,t1,p,i,t2
			 * 		oppure
			 *  2: f,t1,d,i,t2
			 */
			states[0x1] = TaskState.NEW;
		}
		output = sb.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 997;
		int result = 1;
		result = prime * result + Arrays.hashCode(states);
		result = prime * result + Arrays.hashCode(taskIds);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ConstraintImpl))
		{
			return false;
		}
		if(this == obj)
		{
			return true;
		}
		ConstraintImpl other = (ConstraintImpl) obj;
		if (!Arrays.equals(states, other.states))
		{
			return false;
		}
		if (!Arrays.equals(taskIds, other.taskIds))
		{
			return false;
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		return output + ": [taskIds=" + Arrays.toString(taskIds)
				+ ", states=" + Arrays.toString(states) + ", order=" + order
				+ "]";
	}

	public boolean isVerified(int instant) 
	{
		TaskState nextState1Task = states[0x0] != TaskState.TERMINATED ? TaskState.values()[states[0x0].ordinal() + 0x1] : TaskState.TERMINATED;
		TaskState nextState2Task = states[0x1] != TaskState.TERMINATED ? TaskState.values()[states[0x1].ordinal() + 0x1] : TaskState.TERMINATED;
		Task t = taskManager.getTask(taskIds[0x0]);
		if(states[0x0] == TaskState.NEW)
		{
			if(!t.isStarted())
			{
				return taskManager.getTaskState(taskIds[0x0]).ordinal() >= nextState1Task.ordinal() ||						
					   taskManager.getTaskState(taskIds[0x1]).ordinal() < nextState2Task.ordinal();
			}
			else
			{
				return (t.getStartInstant() != instant && taskManager.getTaskState(taskIds[0x0]).ordinal() >= nextState1Task.ordinal()) ||
					   taskManager.getTaskState(taskIds[0x1]).ordinal() < nextState2Task.ordinal();
			}
		}
		else //states[0x0] = TERMINATED
		{
			if(!t.isCompleted())
			{
				return taskManager.getTaskState(taskIds[0x0]).ordinal() >= nextState1Task.ordinal() ||
					   taskManager.getTaskState(taskIds[0x1]).ordinal() < nextState2Task.ordinal();
			}
			else
			{
				return (t.getCompletationInstant() != instant && taskManager.getTaskState(taskIds[0x0]).ordinal() >= nextState1Task.ordinal()) ||
					   taskManager.getTaskState(taskIds[0x1]).ordinal() < nextState2Task.ordinal();
			}
		}
	}
	
	@Override
	public boolean isVerified()
	{

		TaskState nextState1Task = states[0x0] != TaskState.TERMINATED ? TaskState.values()[states[0x0].ordinal() + 0x1] : TaskState.TERMINATED;
		TaskState nextState2Task = states[0x1] != TaskState.TERMINATED ? TaskState.values()[states[0x1].ordinal() + 0x1] : TaskState.TERMINATED;
		return taskManager.getTaskState(taskIds[0x0]).ordinal() >= nextState1Task.ordinal() ||
				(
				  (taskManager.getTaskState(taskIds[0x0]).ordinal() >= TaskState.NEW.ordinal()) &&
				  (taskManager.getTaskState(taskIds[0x1]).ordinal() < nextState2Task.ordinal())
				); 
	}
}
