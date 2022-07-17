package exceptions;

public class TaskParsingException extends TaskException
{
	private static final long serialVersionUID = 7984849971442240199L;
	
	public TaskParsingException()
	{
		super("Task parsing failed");
	}
}
