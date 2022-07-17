package documents;

public final class SchedulerResultBuilderFactoryImpl implements SchedulerResultBuilderFactory
{
	private static SchedulerResultBuilderFactory instance;
	
	private SchedulerResultBuilderFactoryImpl()
	{
	}
	
	public static synchronized SchedulerResultBuilderFactory getInstance()
	{
		if(instance == null)
		{
			instance = new SchedulerResultBuilderFactoryImpl();
		}
		return instance;
	}

	@Override
	public ScheduleResultBuilder newBuilder(Type type)
	{
		switch (type)
		{
			case JSON:
				return new JSONScheduleResultBuilder();

			default:
				return null;
		}
	}
}
