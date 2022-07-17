package documents;

public enum TaskDataKey
{
	COMPLETATION_INSTANT,
	
	START_INSTANT,
	
	LENGHT,
	
	NAME,
	
	ID;
	
	public static String getKeyString(TaskDataKey key)
	{
		switch (key)
		{
			case ID:
			return "id";
			
			case START_INSTANT:
			return "start_instant";
			
			case COMPLETATION_INSTANT:
			return "completation_instant";
			
			case LENGHT:
			return "lenght";
				
			default:
			return "name";
		}
	}
	
	public static TaskDataKey parseKey(String key)
	{
		switch(key)
		{
			case "id":
				return ID;
			
			case "start_instant":
				return START_INSTANT;
			
			case "completation_instant":
				return COMPLETATION_INSTANT;
				
			case "name":
				return TaskDataKey.NAME;
			
			case "lenght":
				return TaskDataKey.LENGHT;
				
			default:
				return null;
		}
	}
}
