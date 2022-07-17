package exceptions;

public class ConstraintException extends RuntimeException
{
	private static final long serialVersionUID = -674429964134461529L;
	
	public ConstraintException()
	{
		super("Constraint Exception");
	}
	
	public ConstraintException(String msg)
	{
		super(msg);
	}
}
