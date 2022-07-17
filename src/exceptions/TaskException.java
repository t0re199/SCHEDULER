package exceptions;

public class TaskException extends RuntimeException
{
	private static final long serialVersionUID = -674429964134461529L;
	
	public TaskException()
	{
		super("Task Exception");
	}
	
	public TaskException(String msg)
	{
		super(msg);
	}
}
