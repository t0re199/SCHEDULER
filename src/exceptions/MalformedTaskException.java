package exceptions;

public class MalformedTaskException extends TaskException
{
	private static final long serialVersionUID = 7769282404512401623L;
	
	public MalformedTaskException()
	{
		super("Malformed Task");
	}
}
