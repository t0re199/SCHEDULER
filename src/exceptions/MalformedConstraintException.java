package exceptions;

public class MalformedConstraintException extends ConstraintException
{
	private static final long serialVersionUID = -5852936516250991317L;
	
	public MalformedConstraintException()
	{
		super("Malformed constraint!");
	}
}
