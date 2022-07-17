package exceptions;

public class TaskNotFoundException extends TaskException
{
	private static final long serialVersionUID = 4499162920252100159L;
	
	public TaskNotFoundException()
	{
		super("Task not found");
	}
}
