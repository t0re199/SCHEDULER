package documents;

public interface SchedulerResultBuilderFactory
{
	static enum Type
	{
		JSON
	}
	
	ScheduleResultBuilder newBuilder(Type type);
}
