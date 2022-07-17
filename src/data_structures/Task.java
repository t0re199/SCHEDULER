package data_structures;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import documents.TaskDataKey;
import exceptions.TaskParsingException;

public interface Task
{	
	String getName();
	
	int getId();
	
	int lenght();
	
	int getRemaingTime();
	
	TaskState getState();
	
	TaskState getStateAtInstant(int instant);
	
	void assign(int t);
	
	void deassign(int t);
	
	int getLastAssignedInstant();
	
	int getStartInstant();
	
	int getCompletationInstant();
	
	default boolean isCompleted()
	{
		return getState() == TaskState.TERMINATED;
	}
	
	default boolean isStarted()
	{
		return getState().ordinal() > TaskState.NEW.ordinal();
	}
	
	static Task parseJSONTask(JSONObject taskObj)
	{
		final int NULL = -199;
		String name = null;
		int id = NULL,
			lenght = NULL,
			startInstant = NULL,
			completationInstant = NULL;
		
		for(String field : taskObj.keySet())
		{
			JSONArray taskData = taskObj.getJSONArray(field);
			for(Iterator<Object> it =  taskData.iterator(); it.hasNext();)
			{
				JSONObject data = (JSONObject) it.next();
				for(String key : data.keySet())
				{
					switch(TaskDataKey.parseKey(key))
					{
						case ID:
							if(id != NULL)
							{
								throw new TaskParsingException();
							}
							id = data.getInt(key);
						break;
						
						case NAME:
							if(name != null)
							{
								throw new TaskParsingException();
							}
							name = data.getString(key);
						break;
							
						case START_INSTANT:
							if(startInstant != NULL)
							{
								throw new TaskParsingException();
							}
							startInstant = data.getInt(key);
						break;
						
						case COMPLETATION_INSTANT:
							if(completationInstant != NULL)
							{
								throw new TaskParsingException();
							}
							completationInstant = data.getInt(key);
						break;
						
						case LENGHT:
							if(lenght != NULL)
							{
								throw new TaskParsingException();
							}
							lenght = data.getInt(key);
						break;
							
						default:
							throw new TaskParsingException();
					}
				}
			}
		}
		try 
		{
			return new UnmodifiableTask(DataStructuresFactoryImpl.getInstance().newTask(id, name, lenght), 
										completationInstant, startInstant);
		}
		catch (Exception e) 
		{
			throw new TaskParsingException();
		}
	}
}
