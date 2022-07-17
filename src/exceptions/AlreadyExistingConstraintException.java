package exceptions;

public class AlreadyExistingConstraintException extends ConstraintException
{
	private static final long serialVersionUID = 2282738597546648321L;
	
	public AlreadyExistingConstraintException()
	{
		super("Already existing constraint");
	}
}
