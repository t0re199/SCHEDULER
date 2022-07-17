package data_structures;

public class UnmodifiableTask implements Task
{
	private Task task;
	
	private int COMPLETATION_INSTANT = -0x1,
					  START_INSTANT = 0x1;
	
	public UnmodifiableTask(Task task)
	{
		this.task = task;
	}
	
	public UnmodifiableTask(Task task, int completationInstant, int startInstant)
	{
		this.task = task;
		this.COMPLETATION_INSTANT = completationInstant;
		this.START_INSTANT = startInstant;
	}

	@Override
	public int getId()
	{
		return task.getId();
	}

	@Override
	public int getRemaingTime()
	{
		return task.getRemaingTime();
	}

	@Override
	public TaskState getState()
	{
		return task.getState();
	}

	@Override
	public TaskState getStateAtInstant(int instant)
	{
		return task.getStateAtInstant(instant);
	}

	@Override
	public void assign(int t)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void deassign(int t)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLastAssignedInstant()
	{
		return task.getLastAssignedInstant();
	}

	@Override
	public boolean isCompleted()
	{
		return task.isCompleted();
	}

	@Override
	public int getStartInstant()
	{
		return START_INSTANT == 0x1 ? task.getStartInstant() : START_INSTANT;
	}

	@Override
	public int getCompletationInstant()
	{
		return COMPLETATION_INSTANT == -0x1 ? task.getCompletationInstant() : COMPLETATION_INSTANT;
	}

	@Override
	public boolean isStarted()
	{
		return task.isStarted();
	}

	@Override
	public String getName()
	{
		return task.getName();
	}
	
	@Override
	public int lenght()
	{
		return task.lenght();
	}
	
	@Override
	public String toString()
	{
		return task.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return task.equals(obj);
	}
}
