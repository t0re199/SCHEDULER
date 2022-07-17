package exceptions;

public class ConstraintContainsInvalidDataException extends ConstraintException
{
	private static final long serialVersionUID = -2304993179017558349L;
	
	public ConstraintContainsInvalidDataException()
	{
		super("Constraint contains invalid data");
	}
}
