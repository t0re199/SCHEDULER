package data_structures;

public final class DataStructuresFactoryImpl implements DataStructuresFactory
{
	private static DataStructuresFactoryImpl instance; 
	
	private DataStructuresFactoryImpl()
	{
	}
	
	public static synchronized DataStructuresFactoryImpl getInstance()
	{
		if(instance == null)
		{
			instance = new DataStructuresFactoryImpl();
		}
		return instance;
	}

	@Override
	public Task newTask(int id, String name, int length)
	{
		return new TaskImpl(id, name, length);
	}

	@Override
	public Constraint newConstraint(TaskManager taskManager, String data)
	{
		return new ConstraintImpl(taskManager, data);
	}
}
