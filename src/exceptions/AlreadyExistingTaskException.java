package exceptions;

public class AlreadyExistingTaskException extends TaskException
{
	private static final long serialVersionUID = 7195667991971037355L;
	
	public AlreadyExistingTaskException()
	{
		super("Already existing task");
	}
}
